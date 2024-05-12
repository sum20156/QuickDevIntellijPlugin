package com.example.testplugin.kotlin_gen.repo_interface

import com.example.testplugin.model.classscodestruct.ApiInterface
import com.example.testplugin.model.classscodestruct.KotlinClass
import com.example.testplugin.utils.classgenerator.RepoImplClassGenerator
import com.example.testplugin.utils.classgenerator.RepoInterfaceGenerator

class RepoInterfaceClassMaker(private val rootClassName: String) {

    fun makeKotlinClass(): KotlinClass {
        return RepoInterfaceGenerator(rootClassName).generate()
    }

}


