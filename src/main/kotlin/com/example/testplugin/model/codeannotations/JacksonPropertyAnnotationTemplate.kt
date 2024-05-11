package com.example.testplugin.model.codeannotations

import com.example.testplugin.model.classscodestruct.Annotation

class JacksonPropertyAnnotationTemplate(val rawName: String) : AnnotationTemplate {

    companion object{

        const val annotationFormat = "@JsonProperty(\"%s\")"
    }

    private val annotation = com.example.testplugin.model.classscodestruct.Annotation(annotationFormat, rawName)

    override fun getCode(): String {
        return annotation.getAnnotationString()
    }

    override fun getAnnotations(): List<Annotation> {
        return listOf(annotation)
    }
}