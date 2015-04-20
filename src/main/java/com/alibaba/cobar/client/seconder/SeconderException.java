package com.alibaba.cobar.client.seconder;

import org.apache.commons.lang.exception.NestableRuntimeException;

/**
 * <br>==========================
 * <br> 公司：优视科技
 * <br> 开发：fangyuan
 * <br> 版本：1.0
 * <br> 创建时间： 2015/4/17
 * <br>==========================
 */
public class SeconderException extends NestableRuntimeException {

    private static final long serialVersionUID = -1L;

    public SeconderException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public SeconderException(String msg)
    {
        super(msg);
    }
}
