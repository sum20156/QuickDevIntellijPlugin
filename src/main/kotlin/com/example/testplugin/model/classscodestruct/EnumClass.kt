package com.example.testplugin.model.classscodestruct

import com.example.testplugin.model.builder.KotlinEnumCodeBuilder
import wu.seal.jsontokotlin.model.classscodestruct.NoGenericKotlinClass

/**
 * Created by ted on 2020/3/14 18:14.
 */
data class EnumClass(
    override val name: String,
    val xEnumNames: List<String>?,
    override val generic: KotlinClass,
    override val referencedClasses: List<KotlinClass> = listOf(generic),
    val enum: List<Any>,
    val comments: String = "",
    override val modifiable: Boolean = true,
    override val codeBuilder: KotlinEnumCodeBuilder = KotlinEnumCodeBuilder.DEFAULT
) : ModifiableKotlinClass, NoGenericKotlinClass {

    override fun getOnlyCurrentCode(): String {
        return codeBuilder.getOnlyCurrentCode(this)
    }

    override fun rename(newName: String): KotlinClass = copy(name = newName)

    override fun getCode(): String {
        return codeBuilder.getCode(this)
    }

    override fun replaceReferencedClasses(replaceRule: Map<KotlinClass, KotlinClass>): EnumClass {
        return if (replaceRule.isEmpty()) this else copy(generic = replaceRule.values.toList()[0], referencedClasses = replaceRule.values.toList())
    }
}