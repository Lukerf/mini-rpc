package org.example.enums;

import java.io.Serializable;

public enum MsgStatus implements Serializable {

    SUCCESS((byte)1, "success"),
    FAIL((byte)2, "fail");

    private byte code;
    private String desc;

    MsgStatus(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static MsgStatus getByCode(byte code) {
        for (MsgStatus msgStatus : MsgStatus.values()) {
            if (msgStatus.getCode() == code) {
                return msgStatus;
            }
        }
        return null;
    }

    public byte getCode() {
        return code;
    }
}
