package org.example.provider;

import org.example.annotation.RpcService;
import org.example.api.Hello;

@RpcService
public class HelloImpl implements Hello {
    @Override
    public String sayHello(String name) {
        return "provider:"+name;
    }
}
