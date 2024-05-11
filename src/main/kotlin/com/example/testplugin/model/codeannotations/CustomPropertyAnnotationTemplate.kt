package com.example.testplugin.model.codeannotations

import com.example.testplugin.model.ConfigManager
import com.example.testplugin.model.classscodestruct.Annotation


class CustomPropertyAnnotationTemplate(val rawName: String) : AnnotationTemplate {

    private val annotation = com.example.testplugin.model.classscodestruct.Annotation(ConfigManager.customPropertyAnnotationFormatString, rawName)

    override fun getCode(): String {
        return annotation.getAnnotationString()
    }

    override fun getAnnotations(): List<Annotation> {
        return listOf(annotation)
    }

}