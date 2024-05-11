package com.example.testplugin.interceptor.annotations.custom

import com.example.testplugin.model.codeannotations.CustomPropertyAnnotationTemplate
import wu.seal.jsontokotlin.model.codeelements.KPropertyName
import com.example.testplugin.interceptor.IKotlinClassInterceptor
import com.example.testplugin.model.ConfigManager
import com.example.testplugin.model.classscodestruct.Annotation
import com.example.testplugin.model.classscodestruct.DataClass
import com.example.testplugin.model.classscodestruct.KotlinClass

class AddCustomAnnotationInterceptor : IKotlinClassInterceptor<KotlinClass> {

    override fun intercept(kotlinClass: KotlinClass): KotlinClass {

        if (kotlinClass is DataClass) {
            val addCustomAnnotationProperties = kotlinClass.properties.map {

                val camelCaseName = KPropertyName.makeLowerCamelCaseLegalName(it.originName)

                val annotations = CustomPropertyAnnotationTemplate(it.originName).getAnnotations()

                it.copy(annotations = annotations,name = camelCaseName)
            }

            val classAnnotationString = ConfigManager.customClassAnnotationFormatString

            val classAnnotation = Annotation.fromAnnotationString(classAnnotationString)

            return kotlinClass.copy(properties = addCustomAnnotationProperties,annotations = listOf(classAnnotation))
        } else {
            return kotlinClass
        }
    }
}
