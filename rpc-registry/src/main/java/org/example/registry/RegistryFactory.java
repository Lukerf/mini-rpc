package org.example.registry;

import org.example.enums.RegistryType;
import org.example.registry.impl.ZookeeperRegistryService;

public class RegistryFactory {

    public static RegistryService getInstance(String registryAddress, RegistryType registryType) throws Exception {
        switch (registryType) {
            case ZOOKEEPER:
                return new ZookeeperRegistryService(registryAddress);
            default:
                return null;
        }
    }
}
