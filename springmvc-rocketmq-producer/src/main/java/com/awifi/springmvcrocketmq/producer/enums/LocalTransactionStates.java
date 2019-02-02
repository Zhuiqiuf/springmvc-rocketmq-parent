package com.awifi.springmvcrocketmq.producer.enums;

import org.apache.rocketmq.client.producer.LocalTransactionState;

public enum LocalTransactionStates {
    UNKNOW(0,LocalTransactionState.UNKNOW),
    COMMIT_MESSAGE(1, LocalTransactionState.COMMIT_MESSAGE),
    ROLLBACK_MESSAGE(2, LocalTransactionState.ROLLBACK_MESSAGE);

    public Integer code;
    public LocalTransactionState desc;

    LocalTransactionStates(Integer code, LocalTransactionState desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public LocalTransactionState getDesc() {
        return desc;
    }

    public void setDesc(LocalTransactionState desc) {
        this.desc = desc;
    }

    public static LocalTransactionStates getTypeByCode(Integer code) {
        LocalTransactionStates defaultType = LocalTransactionStates.UNKNOW;
        for (LocalTransactionStates ftype : LocalTransactionStates.values()) {
            if (ftype.code == code) {
                return ftype;
            }
        }
        return defaultType;
    }

    public static LocalTransactionState getDescByCode(Integer code) {
        return getTypeByCode(code).desc;
    }
}
