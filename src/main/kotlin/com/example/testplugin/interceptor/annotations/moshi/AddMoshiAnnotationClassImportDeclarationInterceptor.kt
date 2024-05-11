package com.example.testplugin.interceptor.annotations.moshi

import com.example.testplugin.interceptor.IImportClassDeclarationInterceptor

class AddMoshiAnnotationClassImportDeclarationInterceptor : IImportClassDeclarationInterceptor {


    override fun intercept(originClassImportDeclaration: String): String {


        val propertyAnnotationImportClassString =  "import com.squareup.moshi.Json"


        return originClassImportDeclaration.append(propertyAnnotationImportClassString)
    }
}
