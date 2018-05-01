package com.forever.study;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author: Forever丶诺
 * @date: 2018/4/27 16:37
 */
@Slf4j
public class DirectoryTest {

    /**
     * 创建Directory对象
     *
     * @return
     */
    public static Directory directory() throws IOException {
        return FSDirectory.open(new File("D:\\temp\\index"));
    }

}
