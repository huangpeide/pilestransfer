package socket.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

public class SocketClient {

    public static String host = "127.0.0.1";
    public static int port = 8882;
    public static LinkedBlockingQueue queue = new LinkedBlockingQueue();
    public static void main(String[] args) throws InterruptedException, IOException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ClientChannelInitializer());

            // 连接服务端
            Channel ch = b.connect(host, port).sync().channel();
//            byte[] msg= new byte[]{0x68,0x01,0x00,0x00,0x00,0x1D,0x10,0x00,0x02,0x54,(byte)0x84,0x56,0x18,0x35,0x02,0x02,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
//                    0x01,0x00,0x01,0x02,0x00,0x00,0x00,0x04,0x00,0x00,0x00,0x01,0x2B,(byte)0xD9};
//            byte[] msg = new byte[]{0x68,(byte)0xda,0x44,0x00,0x32,0x00,(byte)0x82,0x00,0x3,0x00,0xc,0x00,0x3,0x1,0x00,0x00,0x20,0x00,0x00,0x00,0x00,0x00,0x00,0x31,0x32,0x33,0x34,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,(byte)0x90,0x65,0x29,0xf,0xd,0x1,0x12,(byte)0xc0,0x5d,0x2a,0xf,0xd,0x1,0x12,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x2,0x00,0x11,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x11,0x00,0x00,0x6,0x00,0x00,0x31,0x32,0x33,0x34,0x35,0x35,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x01};
            byte[] msg = new byte[]{0x33,0x11,0x12,0x22,0x33,0x11,0x12,0x68,0x56,(byte)0xba,(byte)0x9e,(byte)0xc4,(byte)0x9c,(byte)0x86,0x0,0x3,0x0,0x6b,0x0,0x1,(byte)0xfd,0x8,0x0,0x0,0x0,0x0,0x21,0xf,0x0,0x0,0x7,0x6,0x0,(byte)0x80,0x20,0x1,(byte)0x86,0x0,0x0,0x2,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0};
        for(int i = 0;i<100;i++) {
                ch.writeAndFlush(msg);
            }
            CountDownLatch countDownLatch = new CountDownLatch(1);
            Thread.sleep( 10*1000L );
            countDownLatch.countDown();
            countDownLatch.await();
        } finally {
            group.shutdownGracefully();
        }
    }
}