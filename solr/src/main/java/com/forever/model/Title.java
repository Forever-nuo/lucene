package com.forever.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.solr.client.solrj.beans.Field;

/**
 * @author: Forever丶诺
 * @date: 2018/5/2 9:25
 */
@Data
@Accessors(chain = true)
public class Title {

    @Field
    private Integer id;

    @Field
    private String title;
}
