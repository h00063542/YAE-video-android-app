package com.yilos.nailstar.util;

import com.alibaba.sdk.android.oss.model.AccessControlList;
import com.alibaba.sdk.android.oss.model.AuthenticationType;
import com.alibaba.sdk.android.oss.model.ClientConfiguration;
import com.alibaba.sdk.android.oss.model.TokenGenerator;

/**
 * Created by yilos on 2015-11-12.
 */
public class OssInfo {
    private String accessKey;
    private String screctKey;
    private String bucketName;
    private String globalDefaultHostId;
    private AccessControlList accessControl;
    private AuthenticationType authenticationType;
    private TokenGenerator globalDefaultTokenGenerator;
    private long customStandardTimeWithEpochSec;
    private ClientConfiguration conf;

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getScrectKey() {
        return screctKey;
    }

    public void setScrectKey(String screctKey) {
        this.screctKey = screctKey;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getGlobalDefaultHostId() {
        return globalDefaultHostId;
    }

    public void setGlobalDefaultHostId(String globalDefaultHostId) {
        this.globalDefaultHostId = globalDefaultHostId;
    }

    public AccessControlList getAccessControl() {
        return accessControl;
    }

    public void setAccessControl(AccessControlList accessControl) {
        this.accessControl = accessControl;
    }

    public AuthenticationType getAuthenticationType() {
        return authenticationType;
    }

    public void setAuthenticationType(AuthenticationType authenticationType) {
        this.authenticationType = authenticationType;
    }

    public TokenGenerator getGlobalDefaultTokenGenerator() {
        return globalDefaultTokenGenerator;
    }

    public void setGlobalDefaultTokenGenerator(TokenGenerator globalDefaultTokenGenerator) {
        this.globalDefaultTokenGenerator = globalDefaultTokenGenerator;
    }

    public long getCustomStandardTimeWithEpochSec() {
        return customStandardTimeWithEpochSec;
    }

    public void setCustomStandardTimeWithEpochSec(long customStandardTimeWithEpochSec) {
        this.customStandardTimeWithEpochSec = customStandardTimeWithEpochSec;
    }

    public ClientConfiguration getConf() {
        return conf;
    }

    public void setConf(ClientConfiguration conf) {
        this.conf = conf;
    }
}
