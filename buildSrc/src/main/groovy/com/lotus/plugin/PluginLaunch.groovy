package com.lotus.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import org.gradle.api.Project
import org.gradle.api.Plugin

/**
 * lotus注入代码
 * 参考Arouter-register实现
 */
class PluginLaunch implements Plugin<Project> {

    @Override
    void apply(Project project) {
        def isApp = project.plugins.hasPlugin(AppPlugin)
        println('=======================>>> isApp：' + isApp)
        if (isApp) {
            def android = project.extensions.getByType(AppExtension)
            println('=======================>>> android：' + android)
            def transform = new RegisterTransform(project)
            transform.sRegisterList.add(new ScanSetting('ILotusProxyProvider'))
            transform.sRegisterList.add(new ScanSetting('ILotusImplProvider'))
            android.registerTransform(transform)
        }
    }
}