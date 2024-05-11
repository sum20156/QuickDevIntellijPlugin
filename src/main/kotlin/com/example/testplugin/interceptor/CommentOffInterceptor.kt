package com.example.testplugin.interceptor

import com.example.testplugin.interceptor.IKotlinClassInterceptor
import com.example.testplugin.model.classscodestruct.DataClass
import com.example.testplugin.model.classscodestruct.KotlinClass

/**
 * Interceptor that apply the `isCommentOff` config enable condition
 */
object CommentOffInterceptor : IKotlinClassInterceptor<KotlinClass> {

    override fun intercept(kotlinClass: KotlinClass): KotlinClass {

        return if (kotlinClass is DataClass) {
            val newProperty = kotlinClass.properties.map {
                it.copy(comment = "")
            }

            kotlinClass.copy(properties = newProperty)
        } else {
            kotlinClass
        }

    }
}