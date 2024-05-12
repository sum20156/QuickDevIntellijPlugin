package com.example.testplugin.model.classscodestruct

import com.example.testplugin.model.builder.ApiInterfaceCodeBuilder
import com.example.testplugin.model.codeelements.getDefaultValue
import com.example.testplugin.utils.LogUtil

/**
 * Created by ted on 2020/3/14 18:14.
 */
data class ApiInterface(
    override val name: String,
    val properties: List<Property> = listOf(),
    val responseClass: DataClass,
    override val modifiable: Boolean = true,
    val comments: String = "",
    override val codeBuilder: ApiInterfaceCodeBuilder = ApiInterfaceCodeBuilder()
) : ModifiableKotlinClass, NoGenericKotlinClass {

    override val hasGeneric: Boolean
        get() = false

    override val referencedClasses: List<KotlinClass>
        get() {
            return properties.flatMap { property ->
                mutableListOf(property.typeObject).apply {
                    addAll(property.typeObject.getAllGenericsRecursively())
                }
            }.distinct()
        }

    override fun getOnlyCurrentCode(): String {
        return codeBuilder.getOnlyCurrentCode(this)
    }

    override fun rename(newName: String): KotlinClass = copy(name = newName)

    override fun getCode(): String {
        return codeBuilder.getCode(this)
    }

    override fun replaceReferencedClasses(replaceRule: Map<KotlinClass, KotlinClass>): ApiInterface {
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
}