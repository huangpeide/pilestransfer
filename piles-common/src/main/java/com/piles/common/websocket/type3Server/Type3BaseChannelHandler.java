package com.piles.common.websocket.type3Server;

import com.piles.common.business.IBusinessHandler;
import com.piles.common.util.ChannelMapByEntity;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component("type3BaseChannelHandler")
@Sharable
public class Type3BaseChannelHandler extends SimpleChannelInboundHandler<byte[]> {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "xunDaoBusinessHandler")
    private IBusinessHandler xunDaoBusinessHandler;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {
        Channel incoming = ctx.channel();
        String temp = "";
        for (byte b : msg) {
            temp += " " + Integer.toHexString(Byte.toUnsignedInt(b));
        }
        logger.info("[" + incoming.remoteAddress() + "]【type3】发送请求信息:" + temp);
        byte[] response = xunDaoBusinessHandler.process(msg, incoming);
        if (response != null) {
            ctx.writeAndFlush(response);
            temp = "";
            for (byte b : response) {
                temp += " " + Integer.toHexString(Byte.toUnsignedInt(b));
            }
            logger.info("[" + incoming.remoteAddress() + "]【type3】返回结果信息:" + temp);
        }
        //包装

    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception { // (2)
        Channel incoming = ctx.channel();
        logger.info("【type3】add connect:" + incoming.remoteAddress());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception { // (3)
        Channel incoming = ctx.channel();
        logger.info("【type3】remove connect:" + incoming.remoteAddress());
        ChannelMapByEntity.removeChannel(incoming);
//		ChannelMap.removeChannel(incoming);
    }

    /**
     * 当从Channel中读数据时被调用
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
    }

    /**
     * 当Channel变成活跃状态时被调用；Channel是连接/绑定、就绪的
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        logger.info("【type3】channelActive:" + incoming.remoteAddress() + "在线");
        super.channelActive(ctx);
    }

    /**
     * Channel未连接到远端
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception { // (6)
        Channel incoming = ctx.channel();
        logger.info("【type3】channelInactive:" + incoming.remoteAddress() + "掉线");
        incoming.close();
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //出现异常只打印日志
//		Channel incoming = ctx.channel();
//		ctx.close();
        logger.error("【type3】exceptioncaught," + ctx.channel().remoteAddress(), cause);
//		ChannelMap.removeChannel(incoming);
    }
}
