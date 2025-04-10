package org.example;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.example.decoder.MiniRpcDecoder;
import org.example.encoder.MiniRpcEncoder;
import org.example.handler.RpcResponseHandler;
import org.example.registry.RegistryService;

@Slf4j
public class RpcConsumer {

    private final Bootstrap bootstrap;
    private final EventLoopGroup eventLoopGroup;

    public RpcConsumer() {
        this.bootstrap = new Bootstrap();
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new MiniRpcEncoder())
                                .addLast(new MiniRpcDecoder())
                                .addLast(new RpcResponseHandler());
                    }
                });
    }

    public void sendRequest(MiniRpcProtocol<MiniRpcRequest> protocol, RegistryService registryService) throws Exception {

        MiniRpcRequest request = protocol.getBody();

        Object[] params = request.getParams();

        String serviceKey = RpcServiceHelper.buildServiceKey(request.getClassName(), request.getServiceVersion());

        int invokerHashCode = params.length > 0 ? params[0].hashCode() : serviceKey.hashCode();

        ServiceMeta serviceMetadata = registryService.discovery(serviceKey, invokerHashCode);

        if (serviceMetadata != null) {

            ChannelFuture future = bootstrap.connect(serviceMetadata.getServiceAddr(), serviceMetadata.getServicePort()).sync();

            future.addListener((ChannelFutureListener) arg0 -> {

                if (future.isSuccess()) {

                    log.info("connect rpc server {} on port {} success.", serviceMetadata.getServiceAddr(), serviceMetadata.getServicePort());

                } else {

                    log.error("connect rpc server {} on port {} failed.", serviceMetadata.getServiceAddr(), serviceMetadata.getServicePort());

                    future.cause().printStackTrace();

                    eventLoopGroup.shutdownGracefully();

                }

            });

            future.channel().writeAndFlush(protocol);

        }

    }
}
