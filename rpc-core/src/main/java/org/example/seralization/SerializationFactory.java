package org.example.seralization;

import org.example.enums.SerializationTypeEnum;
import org.example.seralization.impl.HessianSerialization;
import org.example.seralization.impl.JsonSerialization;

public class SerializationFactory {

    public static RpcSerialization getRpcSerialization(byte serializationType) {

        SerializationTypeEnum typeEnum = SerializationTypeEnum.findByType(serializationType);

        switch (typeEnum) {

            case HESSIAN:

                return new HessianSerialization();

            case JSON:

                return new JsonSerialization();

            default:

                throw new IllegalArgumentException("serialization type is illegal, " + serializationType);

        }

    }

}
