package com.example.testplugin.interceptor.annotations.jackson

import com.example.testplugin.model.codeannotations.JacksonPropertyAnnotationTemplate
import com.example.testplugin.model.codeelements.KPropertyName
import com.example.testplugin.interceptor.IKotlinClassInterceptor
import com.example.testplugin.model.classscodestruct.DataClass
import com.example.testplugin.model.classscodestruct.KotlinClass


class AddJacksonAnnotationInterceptor : IKotlinClassInterceptor<KotlinClass> {

    override fun intercept(kotlinClass: KotlinClass): KotlinClass {

        if (kotlinClass is DataClass) {
            val addMoshiCodeGenAnnotationProperties = kotlinClass.properties.map {

                val camelCaseName = KPropertyName.makeLowerCamelCaseLegalName(it.originName)

                it.copy(annotations =  JacksonPropertyAnnotationTemplate(it.originName).getAnnotations(),name = camelCaseName)
            }

            return kotlinClass.copy(properties = addMoshiCodeGenAnnotationProperties)
        } else {
            return kotlinClass
        }
    }
}
