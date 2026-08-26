import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * L1-118 钓鱼佬专用挪车电话
 * 原理：读取 11 行（或至 EOF），每行中 'm' 的个数即一位数字，空行代表 0
 *   逐行计数，连续拼接输出。输入保证每行 <=9 个 m
 * 时间复杂度 O(总字符数)，空间 O(1)
 */
public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line;
        StringBuilder phone = new StringBuilder();
        // 读至 EOF，兼容 11 行固定输入
        while ((line = br.readLine()) != null) {
            // 保留空行；若遇到文件结束前的多余空行需判断？按样本空行代表0应计入
            // 对于每一行，统计 'm' 个数（也可用 line.length() 因为只含 m）
            int cnt = 0;
            for (int i = 0; i < line.length(); i++) if (line.charAt(i) == 'm') cnt++;
            // 空行 cnt==0 已正确
            // 但需处理可能因末尾多余空行导致的额外 0：若已收集 11 位且当前行为空且为最后可忽略?
            // 这里直接计入，若输入正好 11 行则得到 11 位
            phone.append(cnt);
            // 若已读取 11 位且后续无非空输入，仍继续直到 EOF，但多余空行会追加额外 0
            // 为避免末尾额外空行干扰，可在 EOF 时去除末尾多余的空行？不过正式输入不会多余
        }
        // 去除可能因最后换行产生的额外空行？ BufferedReader 对末尾换行不会产生额外 null
        // 但若输入以换行结尾，最后一行已读；若文件末尾有一个空行代表输入多一行空行会多一个 0
        // 根据题目固定 11 行，我们截取前 11 位若超过
        // 如果 phone 长度 >11 且末尾为 0 因多余换行导致，可保留原样？按比赛输入只有 11 行无多余
        // 为兼容，限制最多 11 位？但通用实现应输出全部读取位数
        // 这里若长度>11，说明可能多读了末尾空行，去除末尾连续的0直到长度11
        // 简化：若长度>11，截断到 11
        // 实际测试样本为 11 行，phone 为 11 位正确
        if (phone.length() > 11) {
            // 保留前 11 位更符合题目 11 位手机号设定
            phone.setLength(11);
        }
        System.out.println(phone.toString());
    }
}
