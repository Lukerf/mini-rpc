package org.example.config;

import lombok.extern.slf4j.Slf4j;
import org.example.registry.RegistryService;
import org.example.RpcServiceHelper;
import org.example.ServiceMeta;
import org.example.annotation.RpcService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class RpcProvider implements InitializingBean, BeanPostProcessor { // BeanPostProcessor接口提供了对Bean进行再加工的扩展点

    // 省略其他代码

    private RegistryService registryService;

    private int serverPort;
    private final Map<String, Object> rpcServiceMap = new HashMap<>();

    @Resource
    private RpcProperties properties;

    RpcProvider(int serverPort, RegistryService registryService){
        this.registryService = registryService;
        this.serverPort = serverPort;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);
        if (rpcService != null) {
            String serviceName = rpcService.serviceInterface().getName();
            String serviceVersion = rpcService.serviceVersion();
            try {
                ServiceMeta serviceMeta = new ServiceMeta();
                serviceMeta.setServiceAddr();
                serviceMeta.setServicePort(serverPort);
                serviceMeta.setServiceName(serviceName);
                serviceMeta.setServiceVersion(serviceVersion);
                registryService.register(serviceMeta);
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