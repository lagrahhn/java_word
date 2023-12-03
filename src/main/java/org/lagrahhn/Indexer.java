package org.lagrahhn;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

public class Indexer {
    private IndexWriter writer;

    public Indexer(String indexDir) throws IOException {
        Directory directory = FSDirectory.open(Paths.get(indexDir));
        Analyzer analyzer = new StandardAnalyzer();

        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        writer = new IndexWriter(directory, config);
    }

    public void close() throws IOException {
        writer.close();
    }

    public int indexAll(String dataDir) throws IOException {
        File[] files = new File(dataDir).listFiles();
        for (File file : files) {
            indexFile(file);
        }
        return writer.numRamDocs();
    }

    private void indexFile(File file) throws IOException {
        System.out.println("索引文件的路径：" + file.getCanonicalPath());
        Document document = getDocument(file);
        writer.addDocument(document);
    }

    private Document getDocument(File file) throws IOException {
        Document document = new Document();
        document.add(new TextField("contents", new FileReader(file)));
        document.add(new TextField("fileName", file.getName(), Field.Store.YES));
        document.add(new TextField("fullPath", file.getCanonicalPath(), Field.Store.YES));
        return document;
    }

    public static void main(String[] args) {
        String indexDir = "F:\\项目\\Java项目\\words\\src\\main\\resources\\index";
        String dataDir = "F:\\项目\\Java项目\\words\\src\\main\\resources\\data";
        Indexer indexer = null;
        int indexedNum = 0;
        long startTime = System.currentTimeMillis();
        try {
            indexer = new Indexer(indexDir);
            indexedNum = indexer.indexAll(dataDir);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                indexer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("索引耗时：" + (endTime - startTime) + "毫秒");
        System.out.println("共索引了：" + indexedNum + "个文件");
    }

}
