package com.example.testplugin.model.codeannotations

import com.example.testplugin.model.classscodestruct.Annotation


class GsonPropertyAnnotationTemplate(val rawName: String) : AnnotationTemplate {

    companion object {

        const val propertyAnnotationFormat = "@SerializedName(\"%s\")"
    }

    private val annotation = com.example.testplugin.model.classscodestruct.Annotation(propertyAnnotationFormat, rawName)

    override fun getCode(): String {
        return annotation.getAnnotationString()
    }

    override fun getAnnotations(): List<Annotation> {
        return listOf(annotation)
    }
}