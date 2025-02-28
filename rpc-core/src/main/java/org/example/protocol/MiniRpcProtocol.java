package org.example.protocol;

import lombok.Data;

import java.io.Serializable;

@Data
public class MiniRpcProtocol<T> implements Serializable {

    private MsgHeader header; // 协议头

    private T body; // 协议体

}
