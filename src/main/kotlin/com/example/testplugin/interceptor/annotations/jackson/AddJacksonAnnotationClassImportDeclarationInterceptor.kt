package com.example.testplugin.interceptor.annotations.jackson

import com.example.testplugin.interceptor.IImportClassDeclarationInterceptor

class AddJacksonAnnotationClassImportDeclarationInterceptor : IImportClassDeclarationInterceptor {


    override fun intercept(originClassImportDeclaration: String): String {


        val propertyAnnotationImportClassString = "import com.fasterxml.jackson.annotation.JsonProperty"


        return originClassImportDeclaration.append(propertyAnnotationImportClassString)
    }

}
