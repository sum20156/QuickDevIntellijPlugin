package com.example.testplugin

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import com.example.testplugin.feedback.PLUGIN_VERSION
import com.example.testplugin.feedback.sendConfigInfo
import com.example.testplugin.feedback.sendHistoryActionInfo
import com.example.testplugin.feedback.sendHistoryExceptionInfo
import com.example.testplugin.utils.LogUtil

/**
 *
 * Created by Seal.wu on 2017/8/21.
 */

class JsonToKotlinApplication : StartupActivity, DumbAware {

    override fun runActivity(project: Project) {

        LogUtil.i("init JSON To Kotlin Class version ==$PLUGIN_VERSION")

        Thread {
            try {
                sendConfigInfo()
                sendHistoryExceptionInfo()
                sendHistoryActionInfo()
            } catch (e: Exception) {
                LogUtil.e(e.message.toString(),e)
            }
        }.start()
    }
}
