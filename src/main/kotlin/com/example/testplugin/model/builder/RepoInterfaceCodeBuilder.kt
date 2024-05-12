package com.example.testplugin.model.builder

import com.example.testplugin.model.classscodestruct.RepoInterface
import com.example.testplugin.utils.toAnnotationComments
import java.lang.StringBuilder

class RepoInterfaceCodeBuilder:ICodeBuilder<RepoInterface> {
    override fun getCode(clazz: RepoInterface): String {
       return getOnlyCurrentCode(clazz)
    }

    override fun getOnlyCurrentCode(clazz: RepoInterface): String {
        clazz.run {
            val nameWithOutRepoImpl = clazz.name.replace("Repo","", ignoreCase = true)
           return StringBuilder().append(comments.toAnnotationComments())
                .append("interface ${clazz.name} {\n" +
                        "\n" +
                        "\n" +
                        "    fun do${nameWithOutRepoImpl}()//: return ResponseClass?/EntityClass/YourDomainLayerModel/FlowOfData\n" +
                        "}").toString()
        }
    }
}