package com.example.testplugin.interceptor

import com.example.testplugin.interceptor.IKotlinClassInterceptor
import com.example.testplugin.model.ConfigManager
import com.example.testplugin.model.classscodestruct.DataClass
import com.example.testplugin.model.classscodestruct.KotlinClass

class ParentClassTemplateKotlinClassInterceptor : IKotlinClassInterceptor<KotlinClass> {

    override fun intercept(kotlinClass: KotlinClass): KotlinClass {

        if (kotlinClass is DataClass) {

            val parentClassTemplateSimple = ConfigManager.parenClassTemplate.substringAfterLast(".")
            return kotlinClass.copy(parentClassTemplate = parentClassTemplateSimple)
        } else {
            return kotlinClass
        }
    }


}