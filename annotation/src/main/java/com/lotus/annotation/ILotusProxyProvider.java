package com.lotus.annotation;

import java.util.Map;

/**
 * Created by ljq on 2019/5/8
 */
public interface ILotusProxyProvider {

    /**
     * key：LotusProxy注解的value
     * value：LotusProxy注解的类
     */
    Map<String, Class> getMap();
}
