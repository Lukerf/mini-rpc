package org.example.enums;

import java.io.Serializable;

public enum MsgType implements Serializable {
    REQUEST((byte)1, "request"),
    RESPONSE((byte)2, "response");

    private byte type;
    private String desc;

    MsgType(byte type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static MsgType findByType(byte type) {
        for (MsgType msgType : MsgType.values()) {
            if (msgType.getType() == type) {
                return msgType;
            }
        }
        return null;
    }

    public byte getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
