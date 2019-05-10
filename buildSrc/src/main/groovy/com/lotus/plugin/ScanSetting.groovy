package com.lotus.plugin

/**
 * Created by ljq on 2019/5/9
 */
class ScanSetting {

    static final String PLUGIN_NAME = 'Lotus'
    /**
     * annotation的包名
     */
    private static final String INTERFACE_PACKAGE_NAME = 'com/lotus/annotation/'
    /**
     * 要扫描的包
     */
    static final String LOTUS_ROOT_PACKAGE = "com/lotus/provider"
    /**
     * 要注入代码的类
     */
    static final String GENERATE_TO_CLASS_NAME = 'com/lotus/manager/Lotus'
    /**
     * 要注入代码的类的class
     */
    static final String GENERATE_TO_CLASS_FILE_NAME = GENERATE_TO_CLASS_NAME + '.class'
    /**
     * 要注入代码的方法
     */
    static final String GENERATE_TO_METHOD_NAME = "loadLotusMap"
    /**
     * 要注入的代码
     */
    static final String REGISTER_METHOD_NAME = "register"

    /**
     * 扫描结果的类名数组
     */
    ArrayList<String> classList = new ArrayList<>()

    /**
     * 扫描实现这个接口的类
     */
    String interfaceName

    ScanSetting(String interfaceName) {
        this.interfaceName = INTERFACE_PACKAGE_NAME + interfaceName
    }
}
