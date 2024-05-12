package com.example.testplugin.model.builder

import com.example.testplugin.model.classscodestruct.ViewModelClass
import com.example.testplugin.utils.toAnnotationComments
import com.example.testplugin.model.codeelements.KPropertyName
import java.lang.StringBuilder

class ViewModelClassCodeBuilder:ICodeBuilder<ViewModelClass> {
    override fun getCode(clazz: ViewModelClass): String {
       return getOnlyCurrentCode(clazz)
    }

    override fun getOnlyCurrentCode(clazz: ViewModelClass): String {
        clazz.run {
            val useCaseName =clazz.properties.first().name
            val nameWithOutViewModel= clazz.name.replace("ViewModel","", ignoreCase = true)
            val useCaseCamelCase = KPropertyName.classNameToCamelCase(useCaseName)
            return StringBuilder().append(comments.toAnnotationComments())
                .append("import androidx.lifecycle.ViewModel\n" +
                        "import androidx.lifecycle.ViewModelProvider\n" +
                        "\n" +
                        "class ${clazz.name}(\n" +
                        "    private val $useCaseCamelCase: $useCaseName\n" +
                        ") : ViewModel() {\n" +
                        "\n" +
                        "    fun do$nameWithOutViewModel() {\n" +
                        "        // update ui state to loading\n" +
                        "        try {\n" +
                        "            $useCaseCamelCase()\n" +
                        "            // prepare the data for ui layer\n" +
                        "            // update ui state with data\n" +
                        "        } catch (e: Exception) {\n" +
                        "            // update  ui for error\n" +
                        "        }\n" +
                        "    }\n" +
                        "}\n\n" +
                        "class ${clazz.name}Factory(private val $useCaseCamelCase: $useCaseName): ViewModelProvider.NewInstanceFactory() {\n" +
                        "    override fun <T : ViewModel> create(modelClass: Class<T>): T {\n" +
                        "       return ${clazz.name}($useCaseCamelCase) as T\n" +
                        "    }\n" +
                        "}").toString()
        }
    }
}