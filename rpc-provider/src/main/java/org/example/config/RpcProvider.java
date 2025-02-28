package org.example.config;

import org.example.annotation.RpcService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.HashMap;
import java.util.Map;

public class RpcProvider implements InitializingBean, BeanPostProcessor { // BeanPostProcessor接口提供了对Bean进行再加工的扩展点

    // 省略其他代码



    private final Map<String, Object> rpcServiceMap = new HashMap<>();

    @Override

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);

        if (rpcService != null) {

            String serviceName = rpcService.serviceInterface().getName();

            String serviceVersion = rpcService.serviceVersion();

            try {

                ServiceMeta serviceMeta = new ServiceMeta();

                serviceMeta.setServiceAddr(serverAddress);

                serviceMeta.setServicePort(serverPort);

                serviceMeta.setServiceName(serviceName);

                serviceMeta.setServiceVersion(serviceVersion);

                // TODO 发布服务元数据至注册中心

                rpcServiceMap.put(RpcServiceHelper.buildServiceKey(serviceMeta.getServiceName(), serviceMeta.getServiceVersion()), bean);

            } catch (Exception e) {

                log.error("failed to register service {}#{}", serviceName, serviceVersion, e);

            }

        }

        return bean;

    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}