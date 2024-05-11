package com.example.testplugin.utils.classgenerator

import com.example.testplugin.JSON_SCHEMA_FORMAT_MAPPINGS
import com.example.testplugin.model.classscodestruct.DataClass
import com.example.testplugin.model.classscodestruct.KotlinClass
import com.example.testplugin.model.classscodestruct.Property
import com.example.testplugin.model.classscodestruct.RepoClass
import wu.seal.jsontokotlin.model.classscodestruct.*
import wu.seal.jsontokotlin.model.codeelements.getDefaultValue
import wu.seal.jsontokotlin.model.jsonschema.JsonObjectDef
import wu.seal.jsontokotlin.model.jsonschema.JsonSchema
import com.example.testplugin.model.jsonschema.PropertyDef
import com.example.testplugin.utils.constToLiteral
import wu.seal.jsontokotlin.model.codeelements.KClassName

class RepoClassGenerator(private val rootClassName: String,private val apiClass:KotlinClass) {

    fun generate(): RepoClass {
        val className = rootClassName
        return generateClass(className)
    }

    private fun generateClass(className: String): RepoClass {
        val properties = getProperties()
        return RepoClass(
            name = className,
            properties = properties,
            comments =  "",
        )
    }

    private fun getProperties(): List<Property> {
        val properties = mutableListOf<Property>()
        val p =Property(
            originName = apiClass.name,
            type = KClassName.getName(apiClass.name),
            typeObject = apiClass
        )
        properties.add(p)
        return properties
    }

}
