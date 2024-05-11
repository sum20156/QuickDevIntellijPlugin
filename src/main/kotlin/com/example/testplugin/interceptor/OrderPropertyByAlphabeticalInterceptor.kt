package com.example.testplugin.interceptor

import com.example.testplugin.interceptor.IKotlinClassInterceptor
import com.example.testplugin.model.classscodestruct.DataClass
import com.example.testplugin.model.classscodestruct.KotlinClass


class OrderPropertyByAlphabeticalInterceptor : IKotlinClassInterceptor<KotlinClass> {

    override fun intercept(kotlinClass: KotlinClass): KotlinClass {
        if (kotlinClass is DataClass) {

            val orderByAlphabeticalProperties = kotlinClass.properties.sortedBy { it.name }

            return kotlinClass.copy(properties = orderByAlphabeticalProperties)
        } else {
            return kotlinClass
        }

    }
}

