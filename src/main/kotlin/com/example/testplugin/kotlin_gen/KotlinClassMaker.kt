package com.example.testplugin.kotlin_gen

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.example.testplugin.model.ConfigManager
import com.example.testplugin.model.classscodestruct.KotlinClass
import com.example.testplugin.model.jsonschema.JsonSchema
import com.example.testplugin.utils.classgenerator.DataClassGeneratorByJSONObject
import com.example.testplugin.utils.classgenerator.DataClassGeneratorByJSONSchema
import com.example.testplugin.utils.isJSONSchema
import com.example.testplugin.utils.classgenerator.ListClassGeneratorByJSONArray

class KotlinClassMaker(private val rootClassName: String, private val json: String) {

    fun makeKotlinClass(): KotlinClass {
        return if (ConfigManager.autoDetectJsonScheme && json.isJSONSchema()) {
            val jsonSchema = Gson().fromJson(json, JsonSchema::class.java)
            DataClassGeneratorByJSONSchema(rootClassName, jsonSchema).generate()
        } else {
            when {
                json.isJSONObject() -> DataClassGeneratorByJSONObject(rootClassName, Gson().fromJson(json, JsonObject::class.java)).generate(isTop = true)
                json.isJSONArray() -> ListClassGeneratorByJSONArray(rootClassName, json).generate()
                else -> throw IllegalStateException("Can't generate Kotlin Data Class from a no JSON Object/JSON Object Array")
            }
        }
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


