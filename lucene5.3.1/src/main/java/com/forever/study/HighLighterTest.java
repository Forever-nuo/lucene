package com.forever.study;

import com.forever.other.RedHtmlFormatter;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.junit.Test;

import java.io.IOException;

/**
 * @author: Forever丶诺
 * @date: 2018/4/27 15:03
 */
@Slf4j
public class HighLighterTest {

    @Test
    public void testHighLight() throws IOException, InvalidTokenOffsetsException {

        Query query = new TermQuery(new Term("fileContext", "prize"));

        /**
         * 创建高亮对象
         * @param Fragmenter:对象
         * @param Scorer:对象
         */
        QueryScorer queryScorer = new QueryScorer(query);
        Highlighter highlighter = new Highlighter(new RedHtmlFormatter(), queryScorer);

        IndexSearcher indexSearcher = new IndexSearcher(DirectoryReader.open(LuceneUtils.DIRECTORY));

        TopDocs topDocs = indexSearcher.search(query, 20);

        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document doc = indexSearcher.doc(scoreDoc.doc);
            String fileContext = doc.get("fileContext");

            highlighter.setTextFragmenter(new SimpleFragmenter(fileContext.length()));
            String bestFragment = highlighter.getBestFragment(new StandardAnalyzer(), "fileContext", doc.get("fileContext"));
            log.info(bestFragment);
        }

    }

}
