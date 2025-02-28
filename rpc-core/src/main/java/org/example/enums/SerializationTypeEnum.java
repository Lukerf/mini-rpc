package org.example.enums;

import java.io.Serializable;

public enum SerializationTypeEnum implements Serializable {
    HESSIAN((byte)1, "Hessian"),
    JSON((byte)2, "JSON"),
    ;

    private static final long serialVersionUID = 1L;
    private byte type;
    private String desc;

    SerializationTypeEnum(byte type,String desc){
        this.type = type;
        this.desc = desc;
    }

    public static SerializationTypeEnum findByType(byte type){
        for(SerializationTypeEnum serializationTypeEnum : SerializationTypeEnum.values()){
            if(serializationTypeEnum.getType() == type){
                return serializationTypeEnum;
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
