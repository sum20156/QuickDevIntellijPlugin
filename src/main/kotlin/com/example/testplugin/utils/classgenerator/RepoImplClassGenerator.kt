package com.example.testplugin.utils.classgenerator

import com.example.testplugin.model.classscodestruct.*
import com.example.testplugin.model.codeelements.KClassName

class RepoImplClassGenerator(private val rootClassName: String, private val apiInterface: ApiInterface,
                             private val parentClass:RepoInterface) {

    fun generate(): RepoImplClass {
        val className = rootClassName
        return generateClass(className)
    }

    private fun generateClass(className: String): RepoImplClass {
        val properties = getProperties()
        return RepoImplClass(
            name = className,
            properties = properties,
            comments =  "",
            parentClass = parentClass
        )
    }

    private fun getProperties(): List<Property> {
        val properties = mutableListOf<Property>()
        val p =Property(
            originName = apiInterface.name,
            type = KClassName.getName(apiInterface.name),
            typeObject = apiInterface
        )
        properties.add(p)
        return properties
    }

}
