package com.example.testplugin.interceptor

import com.example.testplugin.interceptor.IKotlinClassInterceptor
import com.example.testplugin.model.classscodestruct.DataClass
import com.example.testplugin.model.classscodestruct.KotlinClass

class ChangePropertyKeywordToVarInterceptor : IKotlinClassInterceptor<KotlinClass> {

    override fun intercept(kotlinClass: KotlinClass): KotlinClass {

        if (kotlinClass is DataClass) {

            val varProperties = kotlinClass.properties.map {

                it.copy(keyword = "var")
            }

            return kotlinClass.copy(properties = varProperties)
        } else {
            return  kotlinClass
        }
    }

}
