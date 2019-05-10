package com.lotus.manager;

import android.util.Log;

import com.lotus.annotation.ILotusImplProvider;
import com.lotus.annotation.ILotusProxyProvider;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ljq on 2019/5/8
 */
public class Lotus {

    private static Map<Class, String> mLotusImplMap = new HashMap<>();
    private static Map<String, Class> mLotusProxyMap = new HashMap<>();

    private Lotus() {
        loadLotusMap();
    }

    //todo 这里as多次编译后会注入多次相同代码
    private static void loadLotusMap() {
        //这边使用Plugin注入代码
        //register(xxx.xxx.xxx)
        //register(xxx.xxx.xxx)
        //register(xxx.xxx.xxx)
    }

    private static void register(String className) {
        Log.d("Lotus", "register：" + className);
        if (className != null && !"".equals(className)) {
            try {
                Class<?> clazz = Class.forName(className);
                Object obj = clazz.getConstructor().newInstance();
                if (obj instanceof ILotusImplProvider) {
                    mLotusImplMap.putAll(((ILotusImplProvider) obj).getMap());
                } else if (obj instanceof ILotusProxyProvider) {
                    mLotusProxyMap.putAll(((ILotusProxyProvider) obj).getMap());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static class Instance {
        private static Lotus instance = new Lotus();
    }

    public static Lotus getInstance() {
        return Instance.instance;
    }

    /**
     * 动态代理+反射
     */
    @SuppressWarnings("unchecked")
    public <T> T invoke(Class<T> lotusImpl) {
        Object proxy = null;
        Class proxyClass = null;
        if (mLotusProxyMap != null && mLotusImplMap != null) {
            String annotationValue = mLotusImplMap.get(lotusImpl); //获取lotusImpl注解的路径
            proxyClass = mLotusProxyMap.get(annotationValue); //lotusImpl注解的路径获取lotusProxy
            if (proxyClass != null) {
                try {
                    proxy = proxyClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        Object finalProxy = proxy;
        Class finalProxyClass = proxyClass;
        return (T) Proxy.newProxyInstance(lotusImpl.getClassLoader(), new Class<?>[]{lotusImpl}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (finalProxyClass != null && finalProxy != null) {
                    //  这边没有catch，如果LotusProxyProvider找不到对应的方法，则会抛出异常
                    return finalProxyClass.getMethod(method.getName(), method.getParameterTypes()).invoke(finalProxy, args);
                }
                return null;
            }
        });
    }
}
