package org.example;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

import java.net.URI;
import java.nio.charset.StandardCharsets;

public class HttpClient {
    public void connect(String host, int port) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup(); // 默认是多线程模型，2倍CPU核数的线程
        try {
            Bootstrap b = new Bootstrap();
            b.group(group);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) {
                    ch.pipeline().addLast(new HttpResponseDecoder());
                    ch.pipeline().addLast(new HttpRequestEncoder());
//                    ch.pipeline().addLast(new HttpClientHandler());
                }
            });
            ChannelFuture f = b.connect(host, port).sync();
            URI uri = new URI("http://127.0.0.1:8099");
            String content = "hello world";
            DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET,
                    uri.toASCIIString(), Unpooled.wrappedBuffer(content.getBytes(StandardCharsets.UTF_8)));
            request.headers().set(HttpHeaderNames.HOST, host);
            request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            request.headers().set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
            f.channel().write(request);
            f.channel().flush();
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
    public static void main(String[] args) throws Exception {
        HttpClient client = new HttpClient();
        client.connect("127.0.0.1", 8099);
    }
}