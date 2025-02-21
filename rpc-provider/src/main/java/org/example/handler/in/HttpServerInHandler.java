package org.example.handler.in;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

public class HttpServerInHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private String name;

    public HttpServerInHandler(String name) {

        this.name = name;
    }
    @Override

    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) {

        String content = String.format("Receive http request, uri: %s, method: %s, content: %s%n", msg.uri(), msg.method(), msg.content().toString(CharsetUtil.UTF_8));

        System.out.println(content);
        FullHttpResponse response = new DefaultFullHttpResponse(

                HttpVersion.HTTP_1_1,

                HttpResponseStatus.OK,

                Unpooled.wrappedBuffer(content.getBytes()));

        ctx.writeAndFlush(response);

    }

}