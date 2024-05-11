package com.example.testplugin.interceptor

import com.example.testplugin.interceptor.IKotlinClassInterceptor
import com.example.testplugin.model.classscodestruct.DataClass
import com.example.testplugin.model.classscodestruct.KotlinClass
import wu.seal.jsontokotlin.model.codeelements.KPropertyName

class MakePropertiesNameToBeCamelCaseInterceptor : IKotlinClassInterceptor<KotlinClass> {

    override fun intercept(kotlinClass: KotlinClass): KotlinClass {


        if (kotlinClass is DataClass) {

            val camelCaseNameProperties = kotlinClass.properties.map {

                val camelCaseName = KPropertyName.makeLowerCamelCaseLegalNameOrEmptyName(it.originName)

                it.copy(name = camelCaseName)
            }

            return kotlinClass.copy(properties = camelCaseNameProperties)
        } else {
            return kotlinClass
        }

    }

}
