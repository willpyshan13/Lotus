package com.lotus.plugin

import org.gradle.api.Project

class Logger {
    static org.gradle.api.logging.Logger mLogger

    static init(Project project) {
        mLogger = project.getLogger()
    }

    static void i(String info) {
        if (null != info && null != mLogger) {
            mLogger.info("Lotus::Register >>> " + info)
        }
    }

    static void e(String error) {
        if (null != error && null != mLogger) {
            mLogger.error("Lotus::Register >>> " + error)
        }
    }

    static void w(String warning) {
        if (null != warning && null != mLogger) {
            mLogger.warn("Lotus::Register >>> " + warning)
        }
    }
}