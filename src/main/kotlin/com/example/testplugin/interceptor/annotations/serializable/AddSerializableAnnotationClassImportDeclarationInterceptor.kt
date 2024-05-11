package com.example.testplugin.interceptor.annotations.serializable

import com.example.testplugin.interceptor.IImportClassDeclarationInterceptor

class AddSerializableAnnotationClassImportDeclarationInterceptor : IImportClassDeclarationInterceptor {

    override fun intercept(originClassImportDeclaration: String): String {


        val propertyAnnotationImportClassString = "import kotlinx.serialization.SerialName\n" +
                "import kotlinx.serialization.Serializable"

        return originClassImportDeclaration.append(propertyAnnotationImportClassString)
    }
}
