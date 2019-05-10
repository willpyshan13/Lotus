package com.lotus.annotation;

import java.util.Map;

/**
 * Created by ljq on 2019/5/8
 */
public interface ILotusImplProvider {

    /**
     * key：LotusImpl注解的类
     * value：LotusImpl注解的value
     */
    Map<Class, String> getMap();
}
