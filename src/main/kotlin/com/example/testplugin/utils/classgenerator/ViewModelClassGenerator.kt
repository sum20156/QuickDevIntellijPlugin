package com.example.testplugin.utils.classgenerator

import com.example.testplugin.model.classscodestruct.*
import com.example.testplugin.model.codeelements.KClassName

class ViewModelClassGenerator(private val rootClassName: String, private val useCaseClass: UseCaseClass) {

    fun generate(): ViewModelClass {
        val className = rootClassName
        return generateClass(className)
    }

    private fun generateClass(className: String): ViewModelClass {
        val properties = getProperties()
        return ViewModelClass(
            name = className,
            properties = properties,
            comments =  "",
        )
    }

    private fun getProperties(): List<Property> {
        val properties = mutableListOf<Property>()
        val p =Property(
            originName = useCaseClass.name,
            type = KClassName.getName(useCaseClass.name),
            typeObject = useCaseClass
        )
        properties.add(p)
        return properties
    }

}
