package org.example.handler.out;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class HttpServerOutHandler extends ChannelOutboundHandlerAdapter {

    private String name;

    public HttpServerOutHandler(String name ){
        this.name = name;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.out.println("OutHandler:"+name);
        super.write(ctx, msg, promise);
    }
}
