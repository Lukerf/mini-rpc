package org.example.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.example.protocol.MiniRpcProtocol;
import org.example.protocol.MsgHeader;
import org.example.seralization.RpcSerialization;
import org.example.seralization.SerializationFactory;

public class MiniRpcEncoder extends MessageToByteEncoder<MiniRpcProtocol<Object>> {

    @Override

    protected void encode(ChannelHandlerContext ctx, MiniRpcProtocol<Object> msg, ByteBuf byteBuf) throws Exception {

        MsgHeader header = msg.getHeader();

        byteBuf.writeShort(header.getMagic());

        byteBuf.writeByte(header.getVersion());

        byteBuf.writeByte(header.getSerialization());

        byteBuf.writeByte(header.getMsgType());

        byteBuf.writeByte(header.getStatus());

        byteBuf.writeLong(header.getRequestId());

        RpcSerialization rpcSerialization = SerializationFactory.getRpcSerialization(header.getSerialization());

        byte[] data = rpcSerialization.serialize(msg.getBody());

        byteBuf.writeInt(data.length);

        byteBuf.writeBytes(data);

    }

}
