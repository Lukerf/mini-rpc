package org.example;

public class StartRpcServer {

    private void startRpcServer() throws Exception {

        this.serverAddress = InetAddress.getLocalHost().getHostAddress();

        EventLoopGroup boss = new NioEventLoopGroup();

        EventLoopGroup worker = new NioEventLoopGroup();

        try {

            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(boss, worker)

                    .channel(NioServerSocketChannel.class)

                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override

                        protected void initChannel(SocketChannel socketChannel) throws Exception {

                        }

                    })

                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture channelFuture = bootstrap.bind(this.serverAddress, this.serverPort).sync();

            log.info("server addr {} started on port {}", this.serverAddress, this.serverPort);

            channelFuture.channel().closeFuture().sync();

        } finally {

            boss.shutdownGracefully();

            worker.shutdownGracefully();

        }

    }
}
