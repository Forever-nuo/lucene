package com.forever.study;

import com.forever.model.Title;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SolrPingResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author: Forever丶诺
 * @createTime: 2018-5-1.21:24
 */
@Slf4j
public class SolrManagerTest {
    HttpSolrClient solrClient = new HttpSolrClient("http://localhost:8080/solr/");

    @Test
    public void testConnect() throws IOException, SolrServerException {
        //连接solr服务器
        SolrPingResponse response = solrClient.ping();
        long elapsedTime = response.getElapsedTime();
        log.info("消逝的时间" + elapsedTime);
    }

    /**
     * 新增索引
     * 方式1:使用AddBean方法
     *
     * @throws IOException
     * @throws SolrServerException
     */
    @Test
    public void testAddIndex() throws IOException, SolrServerException {
        Title title = new Title().setId(6).setTitle("第三个文章");
        solrClient.addBean(title);
        UpdateResponse updateResponse = solrClient.commit();
        int status = updateResponse.getStatus();
        if (status == 0) {
            log.info("********新增索引成功");
        }
    }

    /***
     * 新增索引: 使用SolrInputDocument对象
     * @throws IOException
     * @throws SolrServerException
     */
    @Test
    public void testAddIndex1() throws IOException, SolrServerException {
        Title title = new Title().setId(5).setTitle("第5个文章");
        SolrInputDocument inputDocument = new SolrInputDocument();
        //使用反射 将javaBean对象转成 SolrInputDocument对象
        inputDocument.addField("title", title.getTitle());
        inputDocument.addField("id", title.getId());
        solrClient.add(inputDocument);
        solrClient.commit();
    }

    /**
     * 更新索引
     * 主键一致就可以了
     */
    @Test
    public void testUpdateIndex() throws IOException, SolrServerException {
        solrClient.addBean(new Title().setId(3).setTitle("更新后的标题"));
        UpdateResponse updateResponse = solrClient.commit();
        int status = updateResponse.getStatus();
        log.info(String.valueOf(status));
    }

    /**
     * 删除所有索引
     *
     * @throws IOException
     * @throws SolrServerException
     */
    @Test
    public void testDeleteIndexAll() throws IOException, SolrServerException {
        solrClient.deleteByQuery("*:*");
        solrClient.commit();
    }

    /**
     * 根据 条件删除索引
     *
     * @throws IOException
     * @throws SolrServerException
     */
    @Test
    public void testDeleteIndexByQuery() throws IOException, SolrServerException {
        solrClient.deleteByQuery("id:2");
        solrClient.commit();
    }

    /**
     * 根据id 删除索引
     *
     * @throws IOException
     * @throws SolrServerException
     */
    @Test
    public void testDeleteById() throws IOException, SolrServerException {
        solrClient.deleteById("2");
        solrClient.commit();
    }

    /**
     * 简单查询
     *
     * @throws IOException
     * @throws SolrServerException
     */
    @Test
    public void testQuery() throws IOException, SolrServerException {
        SolrQuery query = new SolrQuery();
        query.setQuery("*:*");
        QueryResponse response = solrClient.query(query);
        SolrDocumentList documentList = response.getResults();
        for (SolrDocument document : documentList) {
            log.info("id主键:{}", document.getFieldValue("id"));
            log.info("标题:{}", document.getFirstValue("title"));
            log.info("*********************************************************");
        }
    }

    /***
     * 分页查询
     * @throws IOException
     * @throws SolrServerException
     */
    @Test
    public void testQuery2() throws IOException, SolrServerException {
        SolrQuery query = new SolrQuery();
        query.setQuery("*:*");
        //进行分页
        query.setStart(0);
        query.setRows(1);

        for (SolrDocument document : solrClient.query(query).getResults()) {
            log.info("主键id的值:{}", document.get("id"));
            log.info("title的值:{}", document.get("title"));
        }
    }

    /**
     * 设置高亮的字段
     */
    @Test
    public void testQuery3_light() throws IOException, SolrServerException {
        SolrQuery query = new SolrQuery();
        //设置要高亮的字段
        query.setQuery("title:标题");

        //设置高亮
        query.setHighlight(true).setHighlightSimplePre("<font color='red'>").setHighlightSimplePost("</front>");
        query.addHighlightField("title");
        QueryResponse response = solrClient.query(query);
        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
        SolrDocumentList results = response.getResults();
        for (SolrDocument document : results) {
            String id = (String) document.getFieldValue("id");
            String title = highlighting.get(id).get("title").get(0);
            log.info(title);
        }


        

    }

}
