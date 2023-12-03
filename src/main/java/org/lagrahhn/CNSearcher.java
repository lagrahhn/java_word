package org.lagrahhn;

import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Paths;

public class CNSearcher {
    public static void search(String indexDir, String q) throws Exception {

        Directory dir = FSDirectory.open(Paths.get(indexDir)); //获取要查询的路径，也就是索引所在的位置
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);
        SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer(); //使用中文分词器
        QueryParser parser = new QueryParser("desc", analyzer); //查询解析器
        Query query = parser.parse(q); //通过解析要查询的String，获取查询对象

        long startTime = System.currentTimeMillis(); //记录索引开始时间
        TopDocs docs = searcher.search(query, 10);//开始查询，查询前10条数据，将记录保存在docs中
        long endTime = System.currentTimeMillis(); //记录索引结束时间
        System.out.println("匹配" + q + "共耗时" + (endTime - startTime) + "毫秒");
        System.out.println("查询到" + docs.totalHits + "条记录");

        for (ScoreDoc scoreDoc : docs.scoreDocs) { //取出每条查询结果
            Document doc = searcher.doc(scoreDoc.doc); //scoreDoc.doc相当于docID,根据这个docID来获取文档
            System.out.println(doc.get("city"));
            System.out.println(doc.get("desc"));
            String desc = doc.get("desc");
        }
        reader.close();
    }

    public static void main(String[] args) {
        String indexDir = "F:\\项目\\Java项目\\words\\src\\main\\resources\\index\\CNindex";
        String q = "上海繁华"; //查询这个字符
        try {
            search(indexDir, q);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
