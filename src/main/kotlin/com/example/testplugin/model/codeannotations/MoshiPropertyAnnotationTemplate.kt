package com.example.testplugin.model.codeannotations

import com.example.testplugin.model.classscodestruct.Annotation


class MoshiPropertyAnnotationTemplate(val rawName: String) : AnnotationTemplate {

    companion object {

        const val propertyAnnotationFormat = "@Json(name = \"%s\")"

    }

    private val annotation = Annotation(propertyAnnotationFormat, rawName)

    override fun getCode(): String {
        return annotation.getAnnotationString()
    }

    override fun getAnnotations(): List<com.example.testplugin.model.classscodestruct.Annotation> {
        return listOf(annotation)
    }
}