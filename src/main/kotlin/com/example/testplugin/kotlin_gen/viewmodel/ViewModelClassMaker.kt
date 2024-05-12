package com.example.testplugin.kotlin_gen.viewmodel

import com.example.testplugin.model.classscodestruct.ApiInterface
import com.example.testplugin.model.classscodestruct.KotlinClass
import com.example.testplugin.model.classscodestruct.RepoInterface
import com.example.testplugin.model.classscodestruct.UseCaseClass
import com.example.testplugin.utils.classgenerator.RepoImplClassGenerator
import com.example.testplugin.utils.classgenerator.UseCaseClassGenerator
import com.example.testplugin.utils.classgenerator.ViewModelClassGenerator

class ViewModelClassMaker(private val rootClassName: String, private val useCaseClass: UseCaseClass) {

    fun makeKotlinClass(): KotlinClass {
        return ViewModelClassGenerator(rootClassName,useCaseClass).generate()
    }

}


