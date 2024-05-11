package wu.seal.jsontokotlin.model.classscodestruct

import com.example.testplugin.model.classscodestruct.DataClass
import com.example.testplugin.model.classscodestruct.KotlinClass
import com.example.testplugin.model.classscodestruct.Property
import com.example.testplugin.model.builder.ICodeBuilder
import com.example.testplugin.model.classscodestruct.ModifiableKotlinClass
import wu.seal.jsontokotlin.utils.getIndent
import com.example.testplugin.utils.toAnnotationComments
import com.example.testplugin.utils.addIndent

data class SealedClass(
    override val name: String,
    override val generic: KotlinClass,
    override val referencedClasses: List<KotlinClass> = listOf(generic),
    val discriminatoryProperties: List<Property>,
    val comments: String = "",
    override val modifiable: Boolean = true,
    override val codeBuilder: ICodeBuilder<*> = ICodeBuilder.EMPTY
) : ModifiableKotlinClass, NoGenericKotlinClass {

    override fun rename(newName: String): KotlinClass = copy(name = newName)
    override fun getCode(): String {
        return getOnlyCurrentCode()
    }

    private fun getDiscriminatoryPropertiesCode(): String {
        return discriminatoryPropertiesCode(this.discriminatoryProperties)
    }

    override fun getOnlyCurrentCode(): String {
        val indent = getIndent()
        val innerReferencedClasses =
            referencedClasses.flatMap { it.referencedClasses }.distinctBy { it.name }
                .filter { it.modifiable }

        return buildString {
            append(comments.toAnnotationComments())
            append("sealed class $name(${getDiscriminatoryPropertiesCode()}) {\n")
            referencedClasses.forEach { it ->
                // TODO: Ensure that `referencedClasses` are actually of type `DataClass`
                append((it as DataClass).withExtends(
                    discriminatoryProperties.map { it.name },
                    this@SealedClass).getOnlyCurrentCode().addIndent(indent))
                append("\n")
            }
            append("}\n\n")
            innerReferencedClasses.forEach {
                append(it.getCode())
                append("\n")
            }
        }
    }

    override fun replaceReferencedClasses(replaceRule: Map<KotlinClass, KotlinClass>): KotlinClass {
        TODO("Not yet implemented")
    }

    companion object {
        fun discriminatoryPropertiesCode(properties: List<Property>): String {
            return properties.joinToString(", ") {
                it.getCode()
            }
        }
    }
}
