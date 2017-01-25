package com.annosearch.model;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Tsvetan Dimitrov <tsvetan.dimitrov23@gmail.com>
 * @since 25-Jan-2017
 */
@ConfigurationProperties(prefix = "annosearch")
public class JsonDataIndexConfiguration {

    private String jsonDataSourcePath;

    private String jsonIndexPath;


    public String getJsonIndexPath() {
        return jsonIndexPath;
    }

    public void setJsonIndexPath(String jsonIndexPath) {
        this.jsonIndexPath = jsonIndexPath;
    }

    public String getJsonDataSourcePath() {
        return jsonDataSourcePath;
    }

    public void setJsonDataSourcePath(String jsonDataSourcePath) {
        this.jsonDataSourcePath = jsonDataSourcePath;
    }
}
