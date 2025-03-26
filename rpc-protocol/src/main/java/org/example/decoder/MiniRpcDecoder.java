package org.example.decoder;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.example.*;
import org.example.enums.MsgType;
import org.example.seralization.RpcSerialization;
import org.example.seralization.SerializationFactory;


import java.util.List;

public class MiniRpcDecoder extends ByteToMessageDecoder {

    @Override

    public final void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        if (in.readableBytes() < ProtocolConstants.HEADER_TOTAL_LEN) {

            return;

        }

        in.markReaderIndex();

        short magic = in.readShort();

        if (magic != ProtocolConstants.MAGIC) {

            throw new IllegalArgumentException("magic number is illegal, " + magic);

        }

        byte version = in.readByte();

        byte serializeType = in.readByte();

        byte msgType = in.readByte();

        byte status = in.readByte();

        long requestId = in.readLong();

        int dataLength = in.readInt();

        if (in.readableBytes() < dataLength) {

            in.resetReaderIndex();

            return;

        }

        byte[] data = new byte[dataLength];

        in.readBytes(data);

        MsgType msgTypeEnum = MsgType.findByType(msgType);

        if (msgTypeEnum == null) {

            return;

        }

        MsgHeader header = new MsgHeader();

        header.setMagic(magic);

        header.setVersion(version);

        header.setSerialization(serializeType);

        header.setStatus(status);

        header.setRequestId(requestId);

        header.setMsgType(msgType);

        header.setMsgLen(dataLength);

        RpcSerialization rpcSerialization = SerializationFactory.getRpcSerialization(serializeType);

        switch (msgTypeEnum) {

            case REQUEST:

                MiniRpcRequest request = rpcSerialization.deserialize(data, MiniRpcRequest.class);

                if (request != null) {

                    MiniRpcProtocol<MiniRpcRequest> protocol = new MiniRpcProtocol<>();

                    protocol.setHeader(header);

                    protocol.setBody(request);

                    out.add(protocol);

                }
                break;

            case RESPONSE:

                MiniRpcResponse response = rpcSerialization.deserialize(data, MiniRpcResponse.class);

                if (response != null) {

                    MiniRpcProtocol<MiniRpcResponse> protocol = new MiniRpcProtocol<>();

                    protocol.setHeader(header);

                    protocol.setBody(response);

                    out.add(protocol);

                }

                break;

        }

    }

}
