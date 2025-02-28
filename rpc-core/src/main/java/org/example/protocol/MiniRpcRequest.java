package org.example.protocol;

import lombok.Data;

import java.io.Serializable;

@Data
public class MiniRpcRequest implements Serializable {

    private String serviceVersion; // 服务版本

    private String className; // 服务接口名

    private String methodName; // 服务方法名

    private Object[] params; // 方法参数列表

    private Class<?>[] parameterTypes; // 方法参数类型列表
}
