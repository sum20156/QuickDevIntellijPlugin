package com.example.testplugin.kotlin_gen.usecase

import com.example.testplugin.model.classscodestruct.ApiInterface
import com.example.testplugin.model.classscodestruct.KotlinClass
import com.example.testplugin.model.classscodestruct.RepoInterface
import com.example.testplugin.utils.classgenerator.RepoImplClassGenerator
import com.example.testplugin.utils.classgenerator.UseCaseClassGenerator

class UseCaseClassMaker(private val rootClassName: String, private val repoInterface: RepoInterface) {

    fun makeKotlinClass(): KotlinClass {
        return UseCaseClassGenerator(rootClassName,repoInterface).generate()
    }

}


