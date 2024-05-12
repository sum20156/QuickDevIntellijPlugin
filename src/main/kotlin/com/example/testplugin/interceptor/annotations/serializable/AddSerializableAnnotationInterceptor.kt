package com.example.testplugin.interceptor.annotations.serializable

import com.example.testplugin.model.codeannotations.SerializablePropertyAnnotationTemplate
import com.example.testplugin.model.codeelements.KPropertyName
import com.example.testplugin.interceptor.IKotlinClassInterceptor
import com.example.testplugin.model.classscodestruct.Annotation
import com.example.testplugin.model.classscodestruct.DataClass
import com.example.testplugin.model.classscodestruct.KotlinClass

class AddSerializableAnnotationInterceptor: IKotlinClassInterceptor<KotlinClass> {

    override fun intercept(kotlinClass: KotlinClass): KotlinClass {

        if (kotlinClass is DataClass) {
            val addCustomAnnotationProperties = kotlinClass.properties.map {

                val camelCaseName = KPropertyName.makeLowerCamelCaseLegalName(it.originName)

                val annotations = SerializablePropertyAnnotationTemplate(it.originName).getAnnotations()

                it.copy(annotations = annotations,name = camelCaseName)
            }

            val classAnnotationString = "@Serializable"

            val classAnnotation = Annotation.fromAnnotationString(classAnnotationString)

            return kotlinClass.copy(properties = addCustomAnnotationProperties,annotations = listOf(classAnnotation))
        } else {
            return kotlinClass
        }
    }

}