package com.example.testplugin.model.codeannotations

import com.example.testplugin.model.classscodestruct.Annotation


interface AnnotationTemplate {
    fun getCode():String
    fun getAnnotations():List<Annotation>
}