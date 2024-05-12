package com.example.testplugin.utils.classgenerator

import com.example.testplugin.model.classscodestruct.*

class ApiInterfaceGenerator(private val rootClassName: String, private val responseClass: DataClass) {

    fun generate(): ApiInterface {
        val className = rootClassName
        return generateClass(className)
    }

    private fun generateClass(className: String): ApiInterface {
        val properties = getProperties()
        return ApiInterface(
            name = className,
            properties = properties,
            comments =  "",
            responseClass = responseClass
        )
    }

    private fun getProperties(): List<Property> {
        val properties = mutableListOf<Property>()
        return properties
    }

}
