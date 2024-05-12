package com.example.testplugin.model.classscodestruct

import com.example.testplugin.interceptor.IKotlinClassInterceptor
import com.example.testplugin.model.builder.IKotlinDataClassCodeBuilder
import com.example.testplugin.model.builder.KotlinDataClassCodeBuilder
import com.example.testplugin.utils.LogUtil
import com.example.testplugin.model.codeelements.getDefaultValue

data class DataClass(
    val annotations: List<Annotation> = listOf(),
    override val name: String,
    val properties: List<Property> = listOf(),
    val parentClassTemplate: String = "",
    override val modifiable: Boolean = true,
    val comments: String = "",
    val fromJsonSchema: Boolean = false,
    val excludedProperties: List<String> = listOf(),
    val parentClass: KotlinClass? = null,
    val isTop:Boolean=false,
    override val codeBuilder: IKotlinDataClassCodeBuilder = KotlinDataClassCodeBuilder
) : ModifiableKotlinClass, NoGenericKotlinClass {

    override val hasGeneric: Boolean = false

    override val referencedClasses: List<KotlinClass>
        get() {
            return properties.flatMap { property ->
                mutableListOf(property.typeObject).apply {
                    addAll(property.typeObject.getAllGenericsRecursively())
                }
            }.distinct()
        }

    fun withExtends(properties: List<String>, parentClass: KotlinClass): KotlinClass {
        return copy(excludedProperties = properties, parentClass = parentClass)
    }

    override fun rename(newName: String): KotlinClass = copy(name = newName)

    override fun replaceReferencedClasses(replaceRule: Map<KotlinClass, KotlinClass>): KotlinClass {
        if (replaceRule.all { it.key == it.value }) return this
        val propertiesReferencedModifiableKotlinClass = properties.flatMap {
            if (it.typeObject is GenericKotlinClass) {
                it.typeObject.getAllGenericsRecursively().toMutableList().also { list -> list.add(it.typeObject) }
            } else {
                listOf(it.typeObject)
            }
        }.filter { it.modifiable }.distinct()
        if (propertiesReferencedModifiableKotlinClass.size != replaceRule.size) {
            throw IllegalStateException("properties used kotlin classes size should be equal referenced classes size!")
        }
        if (!replaceRule.all { it.key.modifiable }) {
            throw IllegalStateException("to be replaced referenced class should be modifiable!")
        }
        val newProperties = properties.map { property ->
            property.typeObject.let {
                val newTypObj = when (it) {
                    is GenericKotlinClass -> property.typeObject.replaceReferencedClasses(replaceRule)
                    is ModifiableKotlinClass -> replaceRule[property.typeObject]
                        ?: error("Modifiable Kotlin Class Must have a replacement")
                    else -> it
                }
                LogUtil.i("replace type: ${property.type} to ${newTypObj.name}")
                val typeSuffix = if (property.type.endsWith("?")) "?" else ""
                return@let property.copy(
                    type = "${newTypObj.name}$typeSuffix",
                    typeObject = newTypObj,
                    value = if (property.value.isNotEmpty()) getDefaultValue("${newTypObj.name}$typeSuffix") else property.value
                )
            }
        }

        return copy(properties = newProperties)
    }

    override fun getCode(): String {
        return codeBuilder.getCode(this)
    }

    override fun <T : KotlinClass> applyInterceptors(enabledKotlinClassInterceptors: List<IKotlinClassInterceptor<T>>): KotlinClass {
        val newProperties = mutableListOf<Property>()
        properties.forEach {
            newProperties.add(it.copy(typeObject = it.typeObject.applyInterceptors(enabledKotlinClassInterceptors)))
        }
        var newKotlinDataClass: KotlinClass = copy(properties = newProperties)
        enabledKotlinClassInterceptors.forEach {
            newKotlinDataClass = it.intercept(newKotlinDataClass)
        }
        return newKotlinDataClass
    }

    override fun getOnlyCurrentCode(): String {
        return codeBuilder.getOnlyCurrentCode(this)
    }
}
