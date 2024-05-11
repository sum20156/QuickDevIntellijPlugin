package com.example.testplugin.interceptor.annotations.logansquare

import com.example.testplugin.model.codeannotations.LoganSquarePropertyAnnotationTemplate
import wu.seal.jsontokotlin.model.codeelements.KPropertyName
import com.example.testplugin.interceptor.IKotlinClassInterceptor
import com.example.testplugin.model.classscodestruct.Annotation
import com.example.testplugin.model.classscodestruct.DataClass
import com.example.testplugin.model.classscodestruct.KotlinClass

class AddLoganSquareAnnotationInterceptor : IKotlinClassInterceptor<KotlinClass> {

    override fun intercept(kotlinClass: KotlinClass): KotlinClass {

        if (kotlinClass is DataClass) {
            val addLoganSquareAnnotationProperties = kotlinClass.properties.map {

                val camelCaseName = KPropertyName.makeLowerCamelCaseLegalName(it.originName)

                it.copy(annotations =  LoganSquarePropertyAnnotationTemplate(it.originName).getAnnotations(),name = camelCaseName)
            }

            val classAnnotationString = "@JsonObject"

            val classAnnotation = Annotation.fromAnnotationString(classAnnotationString)


            return kotlinClass.copy(properties = addLoganSquareAnnotationProperties,annotations = listOf(classAnnotation))
        } else {
            return kotlinClass
        }
    }
}
