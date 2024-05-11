package com.example.testplugin.interceptor

import com.example.testplugin.interceptor.IKotlinClassInterceptor
import com.example.testplugin.model.ConfigManager
import com.example.testplugin.model.DefaultValueStrategy
import com.example.testplugin.model.classscodestruct.DataClass
import com.example.testplugin.model.classscodestruct.KotlinClass
import wu.seal.jsontokotlin.model.codeelements.getDefaultValue
import wu.seal.jsontokotlin.model.codeelements.getDefaultValueAllowNull

class InitWithDefaultValueInterceptor : IKotlinClassInterceptor<KotlinClass> {
    override fun intercept(kotlinClass: KotlinClass): KotlinClass {
        return if (kotlinClass is DataClass) {
            val dataClass: DataClass = kotlinClass

            val initWithDefaultValueProperties = dataClass.properties.map {

                val initDefaultValue = if (ConfigManager.defaultValueStrategy == DefaultValueStrategy.AvoidNull) getDefaultValue(it.type)
                else getDefaultValueAllowNull(it.type)
                it.copy(value = initDefaultValue)
            }

            dataClass.copy(properties = initWithDefaultValueProperties)
        } else {
            kotlinClass
        }
    }
}
