package extensions.nstd

import com.example.testplugin.model.classscodestruct.DataClass
import extensions.wu.seal.BaseDataClassCodeBuilder
import com.example.testplugin.model.builder.IKotlinDataClassCodeBuilder
import com.example.testplugin.utils.addIndent
import wu.seal.jsontokotlin.utils.getCommentCode
import com.example.testplugin.utils.toAnnotationComments

/**
 * kotlin class code generator
 *
 * Created by Nstd on 2020/6/29 15:40.
 */
class DataClassCodeBuilderForNoConstructorMemberFields(private val kotlinDataClassCodeBuilder: IKotlinDataClassCodeBuilder) :
        BaseDataClassCodeBuilder(kotlinDataClassCodeBuilder) {

    override fun DataClass.genBody(): String {
        val delegateBody = kotlinDataClassCodeBuilder.run { genBody() }
        val noConstructorMemberFields = genNoConstructorProperties()
        return buildString {
            if (delegateBody.isEmpty()) {
                append(noConstructorMemberFields)
            } else{
                appendLine(noConstructorMemberFields)
                append(delegateBody)
            }
        }
    }


    override fun DataClass.genPrimaryConstructorProperties(): String {
        return ""
    }

    private fun DataClass.genNoConstructorProperties(): String {
        return buildString {
            properties.filterNot { excludedProperties.contains(it.name) }.forEachIndexed { index, property ->
                val addIndentCode = property.getCode().addIndent(indent)
                val commentCode = getCommentCode(property.comment)
                if (fromJsonSchema && commentCode.isNotBlank()) {
                    append(commentCode.toAnnotationComments(indent))
                }
                append(addIndentCode)
                if (!fromJsonSchema && commentCode.isNotBlank()) append(" // ").append(commentCode)
                if (index != properties.size - 1) append("\n")
            }
        }
    }
}
