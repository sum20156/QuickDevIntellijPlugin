package com.example.testplugin.model.builder

import com.example.testplugin.model.classscodestruct.RepoImplClass
import com.example.testplugin.utils.toAnnotationComments
import com.example.testplugin.model.codeelements.KPropertyName
import java.lang.StringBuilder

class RepoImplClassCodeBuilder:ICodeBuilder<RepoImplClass> {
    override fun getCode(clazz: RepoImplClass): String {
       return getOnlyCurrentCode(clazz)
    }

    override fun getOnlyCurrentCode(clazz: RepoImplClass): String {
        clazz.run {
            val apiInterfaceName =clazz.properties.first().name
            val nameWithOutRepoImpl = clazz.name.replace("RepoImpl","", ignoreCase = true)
            val apiInterfaceCamelCase = KPropertyName.classNameToCamelCase(apiInterfaceName)
            return StringBuilder().append(comments.toAnnotationComments())
                .append("class ${clazz.name}(\n" +
                        "    private val ${apiInterfaceCamelCase}: $apiInterfaceName\n" +
                        ") : ${parentClass.name} {\n" +
                        "\n" +
                        "\n" +
                        "    override fun do${nameWithOutRepoImpl}()//: return ResponseClass?/EntityClass/YourDomainLayerModel/FlowOfData\n" +
                        "    {\n" +
                        "        // Cache strategy:  Try to get data from server, if fails fetch from local db\n" +
                        "        // You can implement your own cache strategy if needed\n" +
                        "        try {\n" +
                        "            //do network call\n" +
                        "            $apiInterfaceCamelCase.do${nameWithOutRepoImpl}()//.toEnitiy()\n" +
                        "        } catch (e: Exception) {\n" +
                        "            // if network call fail get from local db\n" +
                        "            // dao.getData()\n" +
                        "        }\n" +
                        "    }\n" +
                        "\n" +
                        "}").toString()
        }
    }
}