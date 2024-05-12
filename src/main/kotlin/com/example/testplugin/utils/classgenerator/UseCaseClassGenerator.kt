package com.example.testplugin.utils.classgenerator

import com.example.testplugin.model.classscodestruct.*
import com.example.testplugin.model.codeelements.KClassName

class UseCaseClassGenerator(private val rootClassName: String, private val repoInterface: RepoInterface) {

    fun generate(): UseCaseClass {
        val className = rootClassName
        return generateClass(className)
    }

    private fun generateClass(className: String): UseCaseClass {
        val properties = getProperties()
        return UseCaseClass(
            name = className,
            properties = properties,
            comments =  "",
        )
    }

    private fun getProperties(): List<Property> {
        val properties = mutableListOf<Property>()
        val p =Property(
            originName = repoInterface.name,
            type = KClassName.getName(repoInterface.name),
            typeObject = repoInterface
        )
        properties.add(p)
        return properties
    }

}
