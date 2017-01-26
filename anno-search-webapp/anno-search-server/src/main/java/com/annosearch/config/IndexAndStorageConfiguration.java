package com.annosearch.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Tsvetan Dimitrov <tsvetan.dimitrov23@gmail.com>
 * @since 25-Jan-2017
 */
@ConfigurationProperties(prefix = "annosearch")
public class IndexAndStorageConfiguration {

    private String dataSourcePath;
    private String storageRootPath;


    public String getDataSourcePath() {
        return dataSourcePath;
    }

    public void setDataSourcePath(String dataSourcePath) {
        this.dataSourcePath = dataSourcePath;
    }

    public String getStorageRootPath() {
        return storageRootPath;
    }

    public void setStorageRootPath(String storageRootPath) {
        this.storageRootPath = storageRootPath;
    }
}
