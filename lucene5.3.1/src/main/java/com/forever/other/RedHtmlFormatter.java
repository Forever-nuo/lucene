package com.forever.other;

import org.apache.lucene.search.highlight.SimpleHTMLFormatter;

/**
 * @author: Forever丶诺
 * @date: 2018/4/27 15:10
 */
public class RedHtmlFormatter extends SimpleHTMLFormatter {

    private static final String DEFAULT_PRE_TAG = "<B><font color=red>";
    private static final String DEFAULT_POST_TAG = "</font></B>";

    public RedHtmlFormatter(String preTag, String postTag) {
        super(preTag, postTag);
    }

    public RedHtmlFormatter() {
        this(DEFAULT_PRE_TAG, DEFAULT_POST_TAG);
    }

}
