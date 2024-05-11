package com.example.testplugin.interceptor.annotations.custom

import com.example.testplugin.model.ConfigManager
import com.example.testplugin.interceptor.IImportClassDeclarationInterceptor

class AddCustomAnnotationClassImportDeclarationInterceptor : IImportClassDeclarationInterceptor {

    override fun intercept(originClassImportDeclaration: String): String {


        val propertyAnnotationImportClassString = ConfigManager.customAnnotationClassImportdeclarationString


        return originClassImportDeclaration.append(propertyAnnotationImportClassString)
    }
}
