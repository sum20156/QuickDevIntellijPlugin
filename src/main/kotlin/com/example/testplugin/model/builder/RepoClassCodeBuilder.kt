package com.example.testplugin.model.builder

import com.example.testplugin.model.classscodestruct.RepoClass
import com.example.testplugin.utils.toAnnotationComments
import wu.seal.jsontokotlin.utils.getIndent
import java.lang.StringBuilder

class RepoClassCodeBuilder:ICodeBuilder<RepoClass> {
    override fun getCode(clazz: RepoClass): String {
       return getOnlyCurrentCode(clazz)
    }

    override fun getOnlyCurrentCode(clazz: RepoClass): String {
        clazz.run {
            val indent = getIndent()
            return StringBuilder().append(comments.toAnnotationComments())
                .append("class $name(val value: ${properties.first().name}) {\n")
               // .append(generateValues().joinToString("\n\n") { "$indent$it" })
                .append("\n}").toString()
        }
    }
}