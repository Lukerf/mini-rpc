package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.annotation.RpcReference;
import org.example.api.Hello;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/hello")
@RestController
@Slf4j
public class HelloController {

    @RpcReference
    private Hello hello;

    @GetMapping("/sayHello")
    public String sayHello(String name) {
        String result = hello.sayHello(name);
        log.info("HelloController sayHello");
        return "Hello result:" + result;
    }
}
