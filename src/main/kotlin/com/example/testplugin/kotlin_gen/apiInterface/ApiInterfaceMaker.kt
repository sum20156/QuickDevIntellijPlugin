package com.example.testplugin.kotlin_gen.apiInterface

import com.example.testplugin.model.classscodestruct.DataClass
import com.example.testplugin.model.classscodestruct.KotlinClass
import com.example.testplugin.utils.classgenerator.ApiInterfaceGenerator

class ApiInterfaceMaker(private val rootClassName: String, private val responseClass:DataClass) {

    fun makeKotlinClass(): KotlinClass {
        return ApiInterfaceGenerator(rootClassName,responseClass).generate()
    }


}


