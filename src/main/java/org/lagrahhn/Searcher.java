package org.lagrahhn;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

public class Searcher {
    public static void search(String indexDir, String q) throws IOException, ParseException {
        Directory directory = FSDirectory.open(Paths.get(indexDir));
        IndexReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);
        Analyzer analyzer = new StandardAnalyzer();
        QueryParser parser = new QueryParser("contents", analyzer);
        Query query = parser.parse(q);
        long startTime = System.currentTimeMillis();
        TopDocs docs = searcher.search(query, 10);
        long endTime = System.currentTimeMillis();
        System.out.println("匹配:" + q + "共耗时" + (endTime - startTime) + "毫秒");
        System.out.println("查询到" + docs.totalHits + "条记录");
        for (ScoreDoc scoreDoc : docs.scoreDocs) { //取出每条查询结果
            Document doc = searcher.doc(scoreDoc.doc); //scoreDoc.doc相当于docID,根据这个docID来获取文档
            System.out.println(doc.get("fullPath")); //fullPath是刚刚建立索引的时候我们定义的一个字段
        }
        reader.close();
    }

    public static void main(String[] args) {
        String indexDir = "F:\\项目\\Java项目\\words\\src\\main\\resources\\index";
        String q = "layoff"; //查询这个字符串
        try {
            search(indexDir, q);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
