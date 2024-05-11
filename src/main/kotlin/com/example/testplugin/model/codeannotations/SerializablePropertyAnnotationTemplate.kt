package com.example.testplugin.model.codeannotations

import com.example.testplugin.model.classscodestruct.Annotation


class SerializablePropertyAnnotationTemplate(val rawName: String) : AnnotationTemplate {

    companion object{

        const val annotationFormat = "@SerialName(\"%s\")"
    }

    private val annotation = Annotation(annotationFormat, rawName)

    override fun getCode(): String {
        return annotation.getAnnotationString()
    }

    override fun getAnnotations(): List<com.example.testplugin.model.classscodestruct.Annotation> {
        return listOf(annotation)
    }
}