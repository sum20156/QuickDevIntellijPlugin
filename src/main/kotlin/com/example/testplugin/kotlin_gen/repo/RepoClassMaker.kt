package com.example.testplugin.kotlin_gen.repo

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.example.testplugin.model.ConfigManager
import com.example.testplugin.model.classscodestruct.KotlinClass
import wu.seal.jsontokotlin.model.jsonschema.JsonSchema
import com.example.testplugin.utils.classgenerator.DataClassGeneratorByJSONObject
import com.example.testplugin.utils.classgenerator.DataClassGeneratorByJSONSchema
import com.example.testplugin.utils.isJSONSchema
import com.example.testplugin.utils.classgenerator.ListClassGeneratorByJSONArray
import com.example.testplugin.utils.classgenerator.RepoClassGenerator

class RepoClassMaker(private val rootClassName: String, private val apiClass: KotlinClass) {

    fun makeKotlinClass(): KotlinClass {
        return RepoClassGenerator(rootClassName,apiClass).generate()
    }

    private fun String.isJSONObject(): Boolean {
        val jsonElement = Gson().fromJson(this, JsonElement::class.java)
        return jsonElement.isJsonObject
    }

    private fun String.isJSONArray(): Boolean {
        val jsonElement = Gson().fromJson(this, JsonElement::class.java)
        return jsonElement.isJsonArray
    }
}


