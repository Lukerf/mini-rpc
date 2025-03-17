package org.example;

import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;
import org.example.enums.MsgType;
import org.example.enums.SerializationTypeEnum;
import org.example.handler.MiniRpcFuture;
import org.example.handler.MiniRpcRequestHolder;
import org.example.registry.RegistryService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

public class RpcInvokerProxy implements InvocationHandler {

    private String serviceVersion;
    private long timeout;
    RegistryService registryService;


    public RpcInvokerProxy(String serviceVersion, long timeout, RegistryService registryService) {
        this.serviceVersion = serviceVersion;
        this.timeout = timeout;
        this.registryService = registryService;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 构造 RPC 协议对象

        MiniRpcProtocol<MiniRpcRequest> protocol = new MiniRpcProtocol<>();

        MsgHeader header = new MsgHeader();

        long requestId = MiniRpcRequestHolder.REQUEST_ID_GEN.incrementAndGet();

        header.setMagic(ProtocolConstants.MAGIC);

        header.setVersion(ProtocolConstants.VERSION);

        header.setRequestId(requestId);

        header.setSerialization((byte) SerializationTypeEnum.HESSIAN.getType());

        header.setMsgType((byte) MsgType.REQUEST.getType());

        header.setStatus((byte) 0x1);

        protocol.setHeader(header);

        MiniRpcRequest request = new MiniRpcRequest();

        request.setServiceVersion(this.serviceVersion);

        request.setClassName(method.getDeclaringClass().getName());

        request.setMethodName(method.getName());

        request.setParameterTypes(method.getParameterTypes());

        request.setParams(args);

        protocol.setBody(request);

        RpcConsumer rpcConsumer = new RpcConsumer();

        MiniRpcFuture<MiniRpcResponse> future = new MiniRpcFuture<>(new DefaultPromise<>(new DefaultEventLoop()), timeout);

        MiniRpcRequestHolder.REQUEST_MAP.put(requestId, future);

        // 发起 RPC 远程调用

        rpcConsumer.sendRequest(protocol, this.registryService);

        // 等待 RPC 调用执行结果

        return future.getPromise().get(future.getTimeout(), TimeUnit.MILLISECONDS).getData();

    }
}
