package com.forever.study;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Paths;

/**
 * @author: Forever丶诺
 * @date: 2018/4/27 10:00
 */
@Slf4j
public class WriteLuceneTest {

    /**
     * 索引库的位置
     *
     * @return
     * @throws IOException
     */
    public Directory getIndexDirectory() throws IOException {
        return FSDirectory.open(new File("D:\\temp\\index"));
    }

    @Test
    public void testCreateIndex() throws IOException {

        //读取原始数据
        IndexWriter indexWriter = new IndexWriter(getIndexDirectory(), new IndexWriterConfig(Version.LUCENE_47,new IKAnalyzer()));

        File fileDir = new File("D:\\temp\\files");
        File[] files = fileDir.listFiles();
        for (File file : files) {
            Document document = new Document();

            String fileName = file.getName();
            Field fileNameField = new TextField("fileName", fileName, Field.Store.YES);

            document.add(fileNameField);

            String path = file.getPath();
            Field pathField = new StoredField("filePath", path);
            document.add(pathField);

            long fileSize = FileUtils.sizeOf(file);
            Field fileSizeField = new LongField("fileSize", fileSize, Field.Store.YES);
            document.add(fileSizeField);

            String context = FileUtils.readFileToString(file, Charset.defaultCharset());
            Field contextField = new TextField("fileContext", context, Field.Store.YES);
            document.add(contextField);

            indexWriter.addDocument(document);
        }

        indexWriter.commit();
        indexWriter.close();

    }

    @Test
    public void testReadIndex() throws IOException, ParseException {
        //创建indexSearcher对象

        IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(getIndexDirectory()));
        //  Query query = new TermQuery(new Term("fileContext", "prize"));

        QueryParser queryParser = new QueryParser(Version.LUCENE_47,"fileContext", new IKAnalyzer());
        Query query = queryParser.parse("篮球");
        TopDocs docs = searcher.search(query, 50);


        log.info("TopDocs对象.totalHits:命中的个数:{}", docs.totalHits);
        log.info("TopDocs对象.命中比率:{}", docs.getMaxScore());


        ScoreDoc[] scoreDocs = docs.scoreDocs;


        int length = scoreDocs.length;
        System.out.println(length);
        for (ScoreDoc scoreDoc : scoreDocs) {
            log.info("ScoreDoc对象.shardIndex={}", scoreDoc.shardIndex);
            int docId = scoreDoc.doc;
            log.info("ScoreDoc对象.id={}", docId);
            Document doc = searcher.doc(docId);
            String fileName = doc.get("fileName");
            String fileContext = doc.get("fileContext");
            float score = scoreDoc.score;
            System.out.println(score);
            System.out.println(fileContext);
            System.out.println(fileName);
        }

    }

}
