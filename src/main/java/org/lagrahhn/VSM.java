package org.lagrahhn;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class VSM {

    public static double calCosSim(Map<String, Double> v1, Map<String, Double> v2) {
        double sclar = 0.0, norm1 = 0.0, norm2 = 0.0, similarity = 0.0;
        Set<String> v1keys = v1.keySet(); // keySet表示获取v1，v2字典中的键的值
        Set<String> v2keys = v2.keySet();
        Set<String> both = new HashSet<>();
        both.addAll(v1keys); // addAll 将键的值加入进去
        both.retainAll(v2keys); // retailAll 原先both的键值与v2keys的键值相同的部分将会被保留
        //[css, Hello, Lucene]
        //[css, Word, java, Hello, Hadoop, html]
        // 公共的部分 css Hello
        System.out.println(both);
        for (String str1 : both) {
            sclar += v1.get(str1) * v2.get(str1); // 将二者字典的值进行相乘
            // css v1 2 v2 2 相乘后为4
            // Hello v1 1 v2 1 相乘为1
        }
        for (String str1 : v1.keySet()) {
            norm1 += Math.pow(v1.get(str1), 2);
            // css 2 -> 2^2=4
            // Hello 1 -> 1^2=1
            // Lucene 3 -> 3^2=9
        }
        for (String str2 : v2.keySet()) {
            norm2 += Math.pow(v2.get(str2), 2);
            // 同上
            // css 4 Word 4 java 16 Hello 1 Hadoop 9 html 1
        }
        similarity += sclar / Math.sqrt(norm1 * norm2);
        System.out.println("sclar:" + sclar);
        System.out.println("norm1:" + norm1);
        System.out.println("norm2:" + norm2);
        System.out.println("similarity:" + similarity);
        return similarity;
    }

    public static void main(String[] args) {
        Map<String, Double> m1 = new HashMap<>();
        m1.put("Hello", 1.0);
        m1.put("css", 2.0);
        m1.put("Lucene", 3.0);

        Map<String, Double> m2 = new HashMap<>();
        m2.put("Hello", 1.0);
        m2.put("Word", 2.0);
        m2.put("Hadoop", 3.0);
        m2.put("java", 4.0);
        m2.put("html", 1.0);
        m2.put("css", 2.0);
        calCosSim(m1, m2);
    }
}
