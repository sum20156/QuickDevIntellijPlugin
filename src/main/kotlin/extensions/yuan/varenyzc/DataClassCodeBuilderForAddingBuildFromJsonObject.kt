package extensions.yuan.varenyzc

import com.example.testplugin.model.classscodestruct.DataClass
import extensions.wu.seal.BaseDataClassCodeBuilder
import com.example.testplugin.model.builder.IKotlinDataClassCodeBuilder
import com.example.testplugin.utils.newLine
import com.example.testplugin.utils.times

/**
 * kotlin class code generator for adding buildFromJsonObject function
 *
 * Created by Seal on 2020/7/7 21:45.
 */
class DataClassCodeBuilderForAddingBuildFromJsonObject(private val kotlinDataClassCodeBuilder: IKotlinDataClassCodeBuilder) :
        BaseDataClassCodeBuilder(kotlinDataClassCodeBuilder) {

    private val baseTypeList = listOf<String>("Int", "String", "Boolean", "Double", "Float", "Long")

    override fun DataClass.genBody(): String {
        val delegateBody = kotlinDataClassCodeBuilder.run { genBody() }
        val buildFromJsonObjectFunctionCode = genBuildFromJsonObjectCode()
        return buildString {
            if (delegateBody.isEmpty()) {
                append(buildFromJsonObjectFunctionCode)
            }else{
                appendLine(delegateBody)
                append(buildFromJsonObjectFunctionCode)
            }
        }
    }

    private fun DataClass.genBuildFromJsonObjectCode(): String {
        return buildString {
            append(indent * 1).append("companion object {").newLine()
            append(indent * 2).append("@JvmStatic").newLine()
            append(indent * 2).append("fun buildFromJson(jsonObject: JSONObject?): $name? {").newLine().newLine()
            append(indent * 3).append("jsonObject?.run {").newLine()
            append(indent * 4).append("return $name(").newLine()
            properties.filterNot { excludedProperties.contains(it.name) }.forEachIndexed { index, property ->
                when {
                    baseTypeList.contains(property.type.replace("?", "")) -> {
                        append(indent * 5).append("opt${property.type.replace("?", "")}(\"${property.originName}\")")
                    }
                    property.type.contains("List<") -> {
                        val type = property.type.substring(property.type.indexOf('<') + 1, property.type.indexOf('>'))
                        append(indent * 5).append("Array${property.type.replace("?", "")}().apply {").newLine()
                        append(indent * 6).append("optJSONArray(\"${property.originName}\")?.let {").newLine()
                        append(indent * 7).append("for(i in 0 until it.length()) {").newLine()
                        append(indent * 8).append("this.add($type.buildFromJson(it.getJSONObject(i)))").newLine()
                        append(indent * 7).append("}").newLine()
                        append(indent * 6).append("}").newLine()
                        append(indent * 5).append("}")
                    }
                    else -> {
                        append(indent * 5).append(
                                "${
                                    property.type.replace(
                                            "?",
                                            ""
                                    )
                                }.buildFromJson(optJSONObject(\"${property.originName}\"))"
                        )
                    }
                }

                if (index < properties.size - 1) {
                    append(",")
                }
                newLine()
            }
            append(indent * 4).append(")").newLine()
            append(indent * 3).append("}").newLine()
            append(indent * 3).append("return null").newLine()
            append(indent * 2).append("}").newLine()
            append(indent * 1).append("}")
        }
    }
}
