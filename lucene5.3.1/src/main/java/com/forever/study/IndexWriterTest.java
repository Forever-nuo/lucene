package com.forever.study;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Paths;

/**
 * @author: Forever丶诺
 * @date: 2018/4/27 16:20
 */

@Slf4j
public class IndexWriterTest {

    @Test
    public void testAddDocument() throws IOException {

        /**
         *创建Document对象
         *存放Field对象
         */
        Document document = new Document();
        File file = new File("D:\\temp\\files\\prize2.txt");
        String context = FileUtils.readFileToString(file, Charset.defaultCharset());
        document.add(new TextField("fileContext", context, Field.Store.YES));

        /**
         * 创建IndexWriter对象
         */
        IndexWriter indexWriter = createIndexWrite();
        indexWriter.addDocument(document);
        indexWriter.commit();
        indexWriter.close();
    }

    /**
     * 删除索引Document
     */
    @Test
    public void test() throws IOException {
        //indexWriter

        IndexWriter indexWriter = createIndexWrite();
        //indexWriter.deleteDocuments(new Term("fileContext", "price"));
        indexWriter.deleteAll();
        indexWriter.commit();
        indexWriter.close();
        System.out.println(11);
    }

    /**
     * 创建IndexWriter
     *
     * @return
     * @throws IOException
     */
    public IndexWriter createIndexWrite() throws IOException {
        return new IndexWriter(DirectoryTest.directory(), new IndexWriterConfig(new StandardAnalyzer()));
    }

}
