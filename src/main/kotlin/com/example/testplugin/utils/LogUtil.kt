package com.example.testplugin.utils

import com.intellij.openapi.diagnostic.LoggerRt
import com.example.testplugin.feedback.PLUGIN_NAME
import com.example.testplugin.TestConfig
import java.util.logging.Logger

/**
 * Created by Seal.Wu on 2018/3/12.
 */
object LogUtil {

    fun i(info: String) {
        if (TestConfig.isTestModel) {
            Logger.getLogger(PLUGIN_NAME).info(info)
        } else {
            LoggerRt.getInstance(PLUGIN_NAME).info(info)
        }
    }

    fun w(warning: String) {
        if (TestConfig.isTestModel) {
            Logger.getLogger(PLUGIN_NAME).warning(warning)
        } else {
            LoggerRt.getInstance(PLUGIN_NAME).warn(warning)
        }
    }

    fun e(message: String, e: Throwable) {
        if (TestConfig.isTestModel) {
            e.printStackTrace()
        } else {
            LoggerRt.getInstance(PLUGIN_NAME).error(message,e)
        }
    }
}