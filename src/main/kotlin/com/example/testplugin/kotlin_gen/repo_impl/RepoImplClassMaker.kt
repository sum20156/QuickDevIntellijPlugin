package com.example.testplugin.kotlin_gen.repo_impl

import com.example.testplugin.model.classscodestruct.ApiInterface
import com.example.testplugin.model.classscodestruct.KotlinClass
import com.example.testplugin.model.classscodestruct.RepoInterface
import com.example.testplugin.utils.classgenerator.RepoImplClassGenerator

class RepoImplClassMaker(private val rootClassName: String, private val apiInterface: ApiInterface,
    private val repoInterface: RepoInterface) {

    fun makeKotlinClass(): KotlinClass {
        return RepoImplClassGenerator(rootClassName,apiInterface,repoInterface).generate()
    }

}


