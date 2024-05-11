package com.example.testplugin.kotlin_gen

import com.example.testplugin.model.ConfigManager
import com.example.testplugin.model.classscodestruct.KotlinClass
import com.example.testplugin.interceptor.IKotlinClassInterceptor
import com.example.testplugin.interceptor.InterceptorManager
import com.example.testplugin.utils.distinctByPropertiesAndSimilarClassName

class KotlinClassCodeMaker(private val kotlinClass: KotlinClass, private val generatedFromJSONSchema: Boolean = false) {

    fun makeKotlinClassCode(): String {
        val interceptors = InterceptorManager.getEnabledKotlinDataClassInterceptors()
        return makeKotlinClassCode(interceptors)
    }

    private fun makeKotlinClassCode(interceptors: List<IKotlinClassInterceptor<KotlinClass>>): String {
        var kotlinClassForCodeGenerate = kotlinClass
        kotlinClassForCodeGenerate = kotlinClassForCodeGenerate.applyInterceptors(interceptors)
        return if (ConfigManager.isInnerClassModel) {
            kotlinClassForCodeGenerate.getCode()
        } else {
            val resolveNameConflicts = kotlinClassForCodeGenerate.resolveNameConflicts()
            val allModifiableClassesRecursivelyIncludeSelf = resolveNameConflicts
                .getAllModifiableClassesRecursivelyIncludeSelf()
            if (generatedFromJSONSchema) { //don't remove class when their properties are same
                allModifiableClassesRecursivelyIncludeSelf
                    .joinToString("\n\n") { it.getOnlyCurrentCode() }
            } else {
                allModifiableClassesRecursivelyIncludeSelf.distinctByPropertiesAndSimilarClassName()
                    .joinToString("\n\n") { it.getOnlyCurrentCode() }
            }
        }
    }

}
