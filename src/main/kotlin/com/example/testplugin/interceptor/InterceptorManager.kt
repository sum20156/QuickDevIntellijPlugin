package com.example.testplugin.interceptor

import com.example.testplugin.interceptor.*
import com.example.testplugin.interceptor.annotations.custom.AddCustomAnnotationClassImportDeclarationInterceptor
import com.example.testplugin.interceptor.annotations.custom.AddCustomAnnotationInterceptor
import com.example.testplugin.interceptor.annotations.fastjson.AddFastJsonAnnotationInterceptor
import com.example.testplugin.interceptor.annotations.fastjson.AddFastjsonAnnotationClassImportDeclarationInterceptor
import com.example.testplugin.interceptor.annotations.gson.AddGsonAnnotationClassImportDeclarationInterceptor
import com.example.testplugin.interceptor.annotations.gson.AddGsonAnnotationInterceptor
import com.example.testplugin.interceptor.annotations.jackson.AddJacksonAnnotationClassImportDeclarationInterceptor
import com.example.testplugin.interceptor.annotations.jackson.AddJacksonAnnotationInterceptor
import com.example.testplugin.interceptor.annotations.logansquare.AddLoganSquareAnnotationClassImportDeclarationInterceptor
import com.example.testplugin.interceptor.annotations.logansquare.AddLoganSquareAnnotationInterceptor
import com.example.testplugin.interceptor.annotations.moshi.AddMoshiAnnotationClassImportDeclarationInterceptor
import com.example.testplugin.interceptor.annotations.moshi.AddMoshiAnnotationInterceptor
import com.example.testplugin.interceptor.annotations.moshi.AddMoshiCodeGenAnnotationInterceptor
import com.example.testplugin.interceptor.annotations.moshi.AddMoshiCodeGenClassImportDeclarationInterceptor
import com.example.testplugin.interceptor.annotations.serializable.AddSerializableAnnotationClassImportDeclarationInterceptor
import com.example.testplugin.interceptor.annotations.serializable.AddSerializableAnnotationInterceptor
import com.example.testplugin.model.ConfigManager
import com.example.testplugin.model.DefaultValueStrategy
import com.example.testplugin.model.TargetJsonConverter
import com.example.testplugin.model.classscodestruct.KotlinClass
import extensions.ExtensionsCollector

object InterceptorManager {

    fun getEnabledKotlinDataClassInterceptors(): List<IKotlinClassInterceptor<KotlinClass>> {

        return mutableListOf<IKotlinClassInterceptor<KotlinClass>>().apply {

            if (ConfigManager.isPropertiesVar) {
                add(ChangePropertyKeywordToVarInterceptor())
            }

            add(PropertyTypeNullableStrategyInterceptor())

            if (ConfigManager.defaultValueStrategy != DefaultValueStrategy.None) {
                add(InitWithDefaultValueInterceptor())
            }

            when (ConfigManager.targetJsonConverterLib) {
                TargetJsonConverter.None -> {
                }
                TargetJsonConverter.NoneWithCamelCase -> add(MakePropertiesNameToBeCamelCaseInterceptor())
                TargetJsonConverter.Gson -> add(AddGsonAnnotationInterceptor())
                TargetJsonConverter.FastJson -> add(AddFastJsonAnnotationInterceptor())
                TargetJsonConverter.Jackson -> add(AddJacksonAnnotationInterceptor())
                TargetJsonConverter.MoShi -> add(AddMoshiAnnotationInterceptor())
                TargetJsonConverter.MoshiCodeGen -> add(AddMoshiCodeGenAnnotationInterceptor())
                TargetJsonConverter.LoganSquare -> add(AddLoganSquareAnnotationInterceptor())
                TargetJsonConverter.Custom -> add(AddCustomAnnotationInterceptor())
                TargetJsonConverter.Serializable -> add(AddSerializableAnnotationInterceptor())
            }

            if (ConfigManager.parenClassTemplate.isNotBlank()) {
                add(ParentClassTemplateKotlinClassInterceptor())
            }

            if (ConfigManager.isCommentOff) {
                add(CommentOffInterceptor)
            }

            if (ConfigManager.isOrderByAlphabetical) {
                add(OrderPropertyByAlphabeticalInterceptor())
            }

        }.apply {
            //add extensions's interceptor
            addAll(ExtensionsCollector.extensions)
        }.apply {
            if (ConfigManager.enableMinimalAnnotation) {
                add(MinimalAnnotationKotlinClassInterceptor())
            }
            add(FinalKotlinClassWrapperInterceptor())
        }
    }


    fun getEnabledImportClassDeclarationInterceptors(): List<IImportClassDeclarationInterceptor> {

        return mutableListOf<IImportClassDeclarationInterceptor>().apply {


            when (ConfigManager.targetJsonConverterLib) {
                TargetJsonConverter.Gson->add(AddGsonAnnotationClassImportDeclarationInterceptor())
                TargetJsonConverter.FastJson-> add(AddFastjsonAnnotationClassImportDeclarationInterceptor())
                TargetJsonConverter.Jackson-> add(AddJacksonAnnotationClassImportDeclarationInterceptor())
                TargetJsonConverter.MoShi->add(AddMoshiAnnotationClassImportDeclarationInterceptor())
                TargetJsonConverter.MoshiCodeGen->add(AddMoshiCodeGenClassImportDeclarationInterceptor())
                TargetJsonConverter.LoganSquare->add(AddLoganSquareAnnotationClassImportDeclarationInterceptor())
                TargetJsonConverter.Custom->add(AddCustomAnnotationClassImportDeclarationInterceptor())
                TargetJsonConverter.Serializable->add(AddSerializableAnnotationClassImportDeclarationInterceptor())
                else->{}
            }

            if (ConfigManager.parenClassTemplate.isNotBlank()) {

                add(ParentClassClassImportDeclarationInterceptor())
            }
        }.apply {
            //add extensions's interceptor
            addAll(ExtensionsCollector.extensions)
        }
    }

}
