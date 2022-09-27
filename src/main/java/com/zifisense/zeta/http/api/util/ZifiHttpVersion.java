package com.zifisense.zeta.http.api.util;

/**
 * @desc zeta sdk http version
 * @author PomZWJ
 * @date 2022-09-27
 */
public enum ZifiHttpVersion {
    @Deprecated V1("v1"),
    V2("v2");
    String version;

    ZifiHttpVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
