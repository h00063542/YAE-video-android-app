package com.yilos.nailstar.aboutme.presenter;

import com.yilos.nailstar.aboutme.entity.Message;
import com.yilos.nailstar.aboutme.model.AboutMeServiceImpl;
import com.yilos.nailstar.aboutme.model.AboutMeService;
import com.yilos.nailstar.framework.exception.JSONParseException;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import java.io.IOException;

/**
 * Created by sisilai on 15/10/24.
 */
public class MessagePresenter {
    public AboutMeService aboutMeService = new AboutMeServiceImpl();
    Message message = new Message();
    public int getCount() throws NetworkDisconnectException, JSONParseException {
        try {
            message.setCount(aboutMeService.getMessageCount());
        } catch (NetworkDisconnectException e) {
            throw new NetworkDisconnectException("网络连接失败",e);
        } catch (JSONParseException e) {
            throw new JSONParseException("网络解析失败",e);
        }
        return message.getCount();
    }
}
