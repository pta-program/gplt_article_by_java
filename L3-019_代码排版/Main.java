import java.io.*;
import java.util.*;

// L3-019 代码排版
// 算法：手写递归下降 + 括号匹配，手写格式化（保留字符串内字符，括号内原样保留）
// 额外处理：if/for/while 体若非块则自动补大括号；else-if 展开为 else { if ... }
// 时间复杂度 O(L)，L<=331
public class Main {
    static String s;
    static int n;
    static int pos;
    static StringBuilder out;
    static int indent;

    static String indentStr(int k) {
        StringBuilder sb = new StringBuilder(k * 2);
        for (int i = 0; i < k; i++) sb.append("  ");
        return sb.toString();
    }

    static void skipSpaces() {
        while (pos < n && Character.isWhitespace(s.charAt(pos))) pos++;
    }

    static boolean startsWithKw(String kw) {
        if (pos + kw.length() > n) return false;
        if (!s.regionMatches(pos, kw, 0, kw.length())) return false;
        if (pos + kw.length() < n) {
            char c = s.charAt(pos + kw.length());
            if (Character.isLetterOrDigit(c) || c == '_') return false;
        }
        return true;
    }

    // 寻找不在字符串内的匹配 ')'
    static String captureParen() {
        // 调用前 pos 指向 '('
        int start = pos;
        int depth = 0;
        boolean inD = false, inS = false;
        boolean esc = false;
        for (int i = start; i < n; i++) {
            char c = s.charAt(i);
            if (esc) { esc = false; continue; }
            if (inD) {
                if (c == '\\') esc = true;
                else if (c == '"') inD = false;
                continue;
            }
            if (inS) {
                if (c == '\\') esc = true;
                else if (c == '\'') inS = false;
                continue;
            }
            if (c == '"') { inD = true; continue; }
            if (c == '\'') { inS = true; continue; }
            if (c == '(') depth++;
            else if (c == ')') {
                depth--;
                if (depth == 0) {
                    String ret = s.substring(start, i + 1);
                    pos = i + 1;
                    return ret;
                }
            }
        }
        // 非法
        String ret = s.substring(start);
        pos = n;
        return ret;
    }

    static int findNextSemi(int from) {
        boolean inD = false, inS = false;
        boolean esc = false;
        int depth = 0;
        for (int i = from; i < n; i++) {
            char c = s.charAt(i);
            if (esc) { esc = false; continue; }
            if (inD) {
                if (c == '\\') esc = true;
                else if (c == '"') inD = false;
                continue;
            }
            if (inS) {
                if (c == '\\') esc = true;
                else if (c == '\'') inS = false;
                continue;
            }
            if (c == '"') { inD = true; continue; }
            if (c == '\'') { inS = true; continue; }
            if (c == '(') depth++;
            else if (c == ')') { if (depth > 0) depth--; }
            else if (c == ';' && depth == 0) {
                return i;
            } else if (c == '{' || c == '}') {
                // 遇到块边界，说明语句结束不应跨块，但题目保证简单语句以;结尾
                // 继续找分号；这里不返回
            }
        }
        return n - 1;
    }

    static int findNextBrace(int from) {
        boolean inD = false, inS = false;
        boolean esc = false;
        for (int i = from; i < n; i++) {
            char c = s.charAt(i);
            if (esc) { esc = false; continue; }
            if (inD) {
                if (c == '\\') esc = true;
                else if (c == '"') inD = false;
                continue;
            }
            if (inS) {
                if (c == '\\') esc = true;
                else if (c == '\'') inS = false;
                continue;
            }
            if (c == '"') { inD = true; continue; }
            if (c == '\'') { inS = true; continue; }
            if (c == '{') return i;
        }
        return -1;
    }

    static void parseStatements(int curIndent) {
        while (true) {
            skipSpaces();
            if (pos >= n) break;
            char c = s.charAt(pos);
            if (c == '}') break;
            if (startsWithKw("if")) {
                parseIf(curIndent);
            } else if (startsWithKw("for")) {
                parseFor(curIndent);
            } else if (startsWithKw("while")) {
                parseWhile(curIndent);
            } else if (c == '{') {
                // 裸块
                out.append(indentStr(curIndent)).append("{\n");
                pos++;
                parseStatements(curIndent + 1);
                skipSpaces();
                if (pos < n && s.charAt(pos) == '}') {
                    out.append(indentStr(curIndent)).append("}\n");
                    pos++;
                }
            } else {
                // 简单语句
                int semi = findNextSemi(pos);
                String stmt = s.substring(pos, semi + 1);
                // 去掉首尾空白，但保留内部原样
                stmt = stmt.trim();
                out.append(indentStr(curIndent)).append(stmt).append("\n");
                pos = semi + 1;
            }
        }
    }

    // 解析单条语句作为块体（用于 if/for/while 的非块体）
    static void parseSingleAsBody(int curIndent) {
        skipSpaces();
        if (pos >= n) return;
        if (startsWithKw("if")) {
            parseIf(curIndent);
        } else if (startsWithKw("for")) {
            parseFor(curIndent);
        } else if (startsWithKw("while")) {
            parseWhile(curIndent);
        } else if (s.charAt(pos) == '{') {
            // 理论上此分支不应在 parseSingleAsBody 中出现（块体已在外层处理）
            out.append(indentStr(curIndent)).append("{\n");
            pos++;
            parseStatements(curIndent + 1);
            skipSpaces();
            if (pos < n && s.charAt(pos) == '}') {
                out.append(indentStr(curIndent)).append("}\n");
                pos++;
            }
        } else {
            int semi = findNextSemi(pos);
            String stmt = s.substring(pos, semi + 1).trim();
            out.append(indentStr(curIndent)).append(stmt).append("\n");
            pos = semi + 1;
        }
    }

    static void parseIf(int curIndent) {
        // if 关键字
        pos += 2; // "if"
        skipSpaces();
        String cond = "";
        if (pos < n && s.charAt(pos) == '(') cond = captureParen();
        out.append(indentStr(curIndent)).append("if ").append(cond).append(" {\n");
        skipSpaces();
        if (pos < n && s.charAt(pos) == '{') {
            pos++;
            parseStatements(curIndent + 1);
            skipSpaces();
            if (pos < n && s.charAt(pos) == '}') {
                out.append(indentStr(curIndent)).append("}\n");
                pos++;
            }
        } else {
            parseSingleAsBody(curIndent + 1);
            out.append(indentStr(curIndent)).append("}\n");
        }
        // 检查 else
        int saved = pos;
        skipSpaces();
        if (pos < n && startsWithKw("else")) {
            pos += 4;
            skipSpaces();
            if (pos < n && startsWithKw("if")) {
                // else if -> else { if ... }
                out.append(indentStr(curIndent)).append("else {\n");
                parseIf(curIndent + 1);
                out.append(indentStr(curIndent)).append("}\n");
            } else if (pos < n && s.charAt(pos) == '{') {
                out.append(indentStr(curIndent)).append("else {\n");
                pos++;
                parseStatements(curIndent + 1);
                skipSpaces();
                if (pos < n && s.charAt(pos) == '}') {
                    out.append(indentStr(curIndent)).append("}\n");
                    pos++;
                }
            } else if (pos < n && (startsWithKw("for") || startsWithKw("while"))) {
                out.append(indentStr(curIndent)).append("else {\n");
                if (startsWithKw("for")) parseFor(curIndent + 1);
                else parseWhile(curIndent + 1);
                out.append(indentStr(curIndent)).append("}\n");
            } else {
                // else 后简单语句
                out.append(indentStr(curIndent)).append("else {\n");
                // 此时 pos 指向语句开头
                int semi = findNextSemi(pos);
                // 防止 else 后无语句（如空）？
                if (pos <= semi && semi < n) {
                    String stmt = s.substring(pos, semi + 1).trim();
                    // 避免空语句（如 else 后误判）
                    if (!stmt.isEmpty()) {
                        out.append(indentStr(curIndent + 1)).append(stmt).append("\n");
                    }
                    pos = semi + 1;
                }
                out.append(indentStr(curIndent)).append("}\n");
            }
        } else {
            pos = saved; // 回退 skipSpaces 造成的空隙（但不影响，因为我们已判断无 else，需要把 pos 恢复到 skip 前？实际上 skipSpaces 已跳过空白，无 else 时需要保留 pos 在空白后也无所谓）
            // 保持 pos 在 else 判断前的空白后位置（已跳过）
            // 若没有 else，我们已经通过 skipSpaces 移动 pos，保留即可
            // 若回退会导致多余空白，但不影响解析（parseStatements 会再 skip）
            // 统一：不恢复
        }
    }

    static void parseWhile(int curIndent) {
        pos += 5; // while
        skipSpaces();
        String cond = "";
        if (pos < n && s.charAt(pos) == '(') cond = captureParen();
        out.append(indentStr(curIndent)).append("while ").append(cond).append(" {\n");
        skipSpaces();
        if (pos < n && s.charAt(pos) == '{') {
            pos++;
            parseStatements(curIndent + 1);
            skipSpaces();
            if (pos < n && s.charAt(pos) == '}') {
                out.append(indentStr(curIndent)).append("}\n");
                pos++;
            }
        } else {
            parseSingleAsBody(curIndent + 1);
            out.append(indentStr(curIndent)).append("}\n");
        }
    }

    static void parseFor(int curIndent) {
        pos += 3; // for
        skipSpaces();
        String cond = "";
        if (pos < n && s.charAt(pos) == '(') cond = captureParen();
        out.append(indentStr(curIndent)).append("for ").append(cond).append(" {\n");
        skipSpaces();
        if (pos < n && s.charAt(pos) == '{') {
            pos++;
            parseStatements(curIndent + 1);
            skipSpaces();
            if (pos < n && s.charAt(pos) == '}') {
                out.append(indentStr(curIndent)).append("}\n");
                pos++;
            }
        } else {
            parseSingleAsBody(curIndent + 1);
            out.append(indentStr(curIndent)).append("}\n");
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            if (sb.length() > 0) sb.append('\n');
            sb.append(line);
        }
        s = sb.toString();
        // 处理全空
        if (s.trim().isEmpty()) return;
        n = s.length();
        pos = 0;
        out = new StringBuilder();

        skipSpaces();
        // 处理开头的 int main() 头
        int bracePos = findNextBrace(pos);
        if (bracePos != -1) {
            String header = s.substring(pos, bracePos).trim();
            out.append(header).append("\n");
            out.append("{\n");
            pos = bracePos + 1;
            parseStatements(1);
            skipSpaces();
            if (pos < n && s.charAt(pos) == '}') {
                out.append("}\n");
                pos++;
            }
        } else {
            // 无块情况
            parseStatements(0);
        }
        System.out.print(out.toString());
    }
}
