package com.piles.control.service.impl;

import com.piles.common.business.IPushBusiness;
import com.piles.common.entity.BasePushCallBackResponse;
import com.piles.common.entity.type.ECommandCode;
import com.piles.common.entity.type.EPushResponseCode;
import com.piles.common.util.ChannelResponseCallBackMap;
import com.piles.control.entity.RemoteClosePushRequest;
import com.piles.control.entity.RemoteCloseRequest;
import com.piles.control.service.IRemoteClosePushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 远程结束充电给充电桩发送消息实现类
 */
@Slf4j
@Service("remoteStopPushService_3")
public class Type3RemoteStopPushServiceImpl implements IRemoteClosePushService {
    @Resource(name = "type3PushBusinessImpl")
    IPushBusiness pushBusiness;


    /**
     * 默认1分钟超时
     */
    @Value("${timeout:60000}")
    private long timeout;

    @Override
    public BasePushCallBackResponse<RemoteCloseRequest> doPush(RemoteClosePushRequest remoteClosePushRequest) {
        byte[] pushMsg = RemoteClosePushRequest.packBytesXundao(remoteClosePushRequest);
        BasePushCallBackResponse<RemoteCloseRequest> basePushCallBackResponse = new BasePushCallBackResponse();
        basePushCallBackResponse.setSerial(remoteClosePushRequest.getSerial());
        boolean flag = pushBusiness.push(pushMsg, remoteClosePushRequest.getTradeTypeCode(), remoteClosePushRequest.getPileNo(), basePushCallBackResponse, ECommandCode.REMOTE_CHARGE_CODE);
        if (!flag) {
            basePushCallBackResponse.setCode(EPushResponseCode.CONNECT_ERROR);
            return basePushCallBackResponse;
        }
        try {
            basePushCallBackResponse.getCountDownLatch().await(timeout, TimeUnit.MILLISECONDS);
            ChannelResponseCallBackMap.remove(remoteClosePushRequest.getTradeTypeCode(), remoteClosePushRequest.getPileNo(), remoteClosePushRequest.getSerial());
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        }
        return basePushCallBackResponse;
    }
}
