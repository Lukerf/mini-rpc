package org.example.seralization.impl;

import com.sun.xml.internal.ws.encoding.soap.SerializationException;
import org.example.seralization.RpcSerialization;

import java.io.IOException;
import com.alibaba.fastjson.JSON;

public class JsonSerialization implements RpcSerialization {
    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        if(obj==null){
            throw new NullPointerException();
        }

        try{
            return JSON.toJSONBytes(obj);
        }catch (Exception e){
            throw new SerializationException(e);
        }
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clz) throws IOException {
        Object object = JSON.parseObject(data, clz);
        return (T) object;
    }
}
