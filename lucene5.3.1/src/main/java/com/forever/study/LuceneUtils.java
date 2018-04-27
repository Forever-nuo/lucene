package com.forever.study;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author: Forever丶诺
 * @date: 2018/4/27 15:33
 */
public class LuceneUtils {

    public static Directory DIRECTORY;


    static {
        try {
            DIRECTORY = FSDirectory.open(Paths.get("D:\\temp\\index"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}
