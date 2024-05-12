package com.example.testplugin.utils.classgenerator

import com.example.testplugin.model.classscodestruct.*

class RepoInterfaceGenerator(private val rootClassName: String) {

    fun generate(): RepoInterface {
        val className = rootClassName
        return generateClass(className)
    }

    private fun generateClass(className: String): RepoInterface {
        val properties = getProperties()
        return RepoInterface(
            name = className,
            properties = properties,
            comments =  "",
        )
    }

    private fun getProperties(): List<Property> {
        val properties = mutableListOf<Property>()
        return properties
    }

}
