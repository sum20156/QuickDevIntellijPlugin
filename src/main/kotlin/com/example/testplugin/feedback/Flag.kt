package com.example.testplugin.feedback

import com.example.testplugin.model.ConfigManager
import com.intellij.ide.plugins.PluginManager
import com.intellij.openapi.extensions.PluginId
import com.example.testplugin.TestConfig

/**
 * Flag relative
 * Created by Seal.Wu on 2017/9/25.
 */

val PLUGIN_VERSION = if (TestConfig.isTestModel.not()){
    PluginManager.getPlugin(PluginId.getId("wu.seal.tool.jsontokotlin"))?.version.toString()
} else "1.X"

val UUID = if (ConfigManager.userUUID.isEmpty()) {
    val uuid = java.util.UUID.randomUUID().toString()
    ConfigManager.userUUID = uuid
    uuid
} else ConfigManager.userUUID


const val PLUGIN_NAME = "JSON To Kotlin Class"
