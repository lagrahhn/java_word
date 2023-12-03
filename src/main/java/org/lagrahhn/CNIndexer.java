package org.lagrahhn;

import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Paths;

// 建立中文索引
public class CNIndexer {

    private Directory directory;

    private Integer ids[] = {1, 2, 3}; //用来标识文档
    private String citys[] = {"上海", "南京", "青岛"};
    private String descs[] = {
            "上海是个繁华的城市。",
            "南京是一个有文化的城市。",
            "青岛是一个美丽的城市。"
    };

    public void index(String indexDir) throws Exception {
        directory = FSDirectory.open(Paths.get(indexDir));
        IndexWriter writer = getWriter();
        for (int i = 0; i < ids.length; i++) {
            Document doc = new Document();
            doc.add(new IntField("id", ids[i], Field.Store.YES));
            doc.add(new StringField("city", citys[i], Field.Store.YES));
            doc.add(new TextField("desc", descs[i], Field.Store.YES));
            writer.addDocument(doc); //添加文档
        }
        writer.close(); //close了才真正写到文档中
    }

    private IndexWriter getWriter() throws Exception {
        SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();//使用中文分词器
        IndexWriterConfig config = new IndexWriterConfig(analyzer); //将标准分词器配到写索引的配置中
        IndexWriter writer = new IndexWriter(directory, config); //实例化写索引对象
        return writer;
    }

    public static void main(String[] args) throws Exception {
        new CNIndexer().index("F:\\项目\\Java项目\\words\\src\\main\\resources\\index\\CNindex");
    }


}
