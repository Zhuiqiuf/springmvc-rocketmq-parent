package com.awifi.springmvcrocketmq.consumer.enums;

import org.springframework.stereotype.Component;

public enum BusinessCase {
    PULL_1("001","testPull"),
    PULL_2("002","testPul2");
    private String code;
    private String desc;
    BusinessCase(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static BusinessCase getTypeByCode(String code) {
        BusinessCase defaultType=BusinessCase.PULL_1;
        for (BusinessCase ftype : BusinessCase.values()) {
            if (ftype.code == code) {
                return ftype;
            }
        }
        return defaultType;
    }

    public static String getDescByCode(String code) {
        return getTypeByCode(code).desc;
    }
}
