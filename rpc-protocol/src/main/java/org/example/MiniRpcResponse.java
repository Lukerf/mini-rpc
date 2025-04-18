package org.example;

import lombok.Data;

import java.io.Serializable;

@Data
public class MiniRpcResponse implements Serializable {
    private Object data;
    private String message;
    private String code;
}
