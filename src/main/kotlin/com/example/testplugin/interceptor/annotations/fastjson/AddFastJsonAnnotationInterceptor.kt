package com.example.testplugin.interceptor.annotations.fastjson

import com.example.testplugin.model.codeannotations.FastjsonPropertyAnnotationTemplate
import com.example.testplugin.model.codeelements.KPropertyName
import com.example.testplugin.interceptor.IKotlinClassInterceptor
import com.example.testplugin.model.classscodestruct.DataClass
import com.example.testplugin.model.classscodestruct.KotlinClass

class AddFastJsonAnnotationInterceptor : IKotlinClassInterceptor<KotlinClass> {

    override fun intercept(kotlinClass: KotlinClass): KotlinClass {

        return if (kotlinClass is DataClass) {

            val addFastJsonAnnotationProperties = kotlinClass.properties.map {

                val camelCaseName = KPropertyName.makeLowerCamelCaseLegalName(it.originName)

                val annotations = FastjsonPropertyAnnotationTemplate(it.originName).getAnnotations()

                it.copy(annotations = annotations, name = camelCaseName)
            }

            kotlinClass.copy(properties = addFastJsonAnnotationProperties)
        } else {
            kotlinClass
        }

    }

}
