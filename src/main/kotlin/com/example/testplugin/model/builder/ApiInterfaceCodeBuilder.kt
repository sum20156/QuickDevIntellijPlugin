package com.example.testplugin.model.builder

import com.example.testplugin.model.classscodestruct.ApiInterface
import com.example.testplugin.utils.toAnnotationComments
import java.lang.StringBuilder

class ApiInterfaceCodeBuilder:ICodeBuilder<ApiInterface> {
    override fun getCode(clazz: ApiInterface): String {
       return getOnlyCurrentCode(clazz)
    }

    override fun getOnlyCurrentCode(clazz: ApiInterface): String {
        clazz.run {
            return StringBuilder().append(comments.toAnnotationComments())
                .append("import retrofit2.http.GET \n\n" +
                        "interface ${clazz.name} {\n" +
                        "    \n" +
                        "    @GET(\"your endpoint here\")\n" +
                        "    fun do${clazz.name.replace("api", "",ignoreCase = true)}():${clazz.responseClass.name}\n" +
                        "}").toString()
        }
    }
}