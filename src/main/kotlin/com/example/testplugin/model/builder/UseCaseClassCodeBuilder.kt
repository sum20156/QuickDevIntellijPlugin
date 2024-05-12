package com.example.testplugin.model.builder

import com.example.testplugin.model.classscodestruct.UseCaseClass
import com.example.testplugin.utils.toAnnotationComments
import com.example.testplugin.model.codeelements.KPropertyName
import java.lang.StringBuilder

class UseCaseClassCodeBuilder:ICodeBuilder<UseCaseClass> {
    override fun getCode(clazz: UseCaseClass): String {
       return getOnlyCurrentCode(clazz)
    }

    override fun getOnlyCurrentCode(clazz: UseCaseClass): String {
        clazz.run {
            val repoInterfaceName =clazz.properties.first().name
            val nameWithOutUseCase= clazz.name.replace("UseCase","", ignoreCase = true)
            val repoInterfaceCamelCase = KPropertyName.classNameToCamelCase(repoInterfaceName)
            return StringBuilder().append(comments.toAnnotationComments())
                .append("class ${clazz.name}(\n" +
                        "    private val $repoInterfaceCamelCase: $repoInterfaceName\n" +
                        ") {\n" +
                        "\n" +
                        "    operator fun invoke() //: return ResponseClass?/EntityClass/YourDomainLayerModel/FlowOfData\n" +
                        "    {\n" +
                        "        val data = $repoInterfaceCamelCase.do$nameWithOutUseCase()\n" +
                        "        // perform operation on data, implement your business logics\n" +
                        "        return data\n" +
                        "    }\n" +
                        "}").toString()
        }
    }
}