package com.tencent.backstage.common.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/5/15
 * Time:9:28
 */
@Component
@ConfigurationProperties(ignoreUnknownFields = true)
public class Config {
    private String uploadFileTempPath;
    private String smMsUrl;
    private String uploadPath;

    public String getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public String getUploadFileTempPath() {
        return uploadFileTempPath;
    }

    public void setUploadFileTempPath(String uploadFileTempPath) {
        this.uploadFileTempPath = uploadFileTempPath;
    }

    public String getSmMsUrl() {
        return smMsUrl;
    }

    public void setSmMsUrl(String smMsUrl) {
        this.smMsUrl = smMsUrl;
    }
}
