package com.example.testplugin.interceptor.annotations.moshi

import com.example.testplugin.model.codeannotations.MoshiPropertyAnnotationTemplate
import com.example.testplugin.model.codeelements.KPropertyName
import com.example.testplugin.interceptor.IKotlinClassInterceptor
import com.example.testplugin.model.classscodestruct.DataClass
import com.example.testplugin.model.classscodestruct.KotlinClass

class AddMoshiAnnotationInterceptor : IKotlinClassInterceptor<KotlinClass> {


    override fun intercept(kotlinClass: KotlinClass): KotlinClass {

        if (kotlinClass is DataClass) {
            val addMoshiCodeGenAnnotationProperties = kotlinClass.properties.map {

                val camelCaseName = KPropertyName.makeLowerCamelCaseLegalName(it.originName)

                it.copy(annotations = MoshiPropertyAnnotationTemplate(it.originName).getAnnotations(), name = camelCaseName)
            }

            return kotlinClass.copy(properties = addMoshiCodeGenAnnotationProperties)
        } else {
            return kotlinClass
        }

    }
}
