package extensions.xu.rui

import com.example.testplugin.model.ConfigManager
import com.example.testplugin.model.DefaultValueStrategy
import com.example.testplugin.model.classscodestruct.DataClass
import com.example.testplugin.model.classscodestruct.KotlinClass
import extensions.Extension

import wu.seal.jsontokotlin.model.codeelements.getDefaultValue
import com.example.testplugin.ui.jCheckBox
import com.example.testplugin.ui.jHorizontalLinearLayout
import com.example.testplugin.utils.NULLABLE_PRIMITIVE_TYPES
import com.example.testplugin.utils.getNonNullPrimitiveType
import javax.swing.JPanel

object PrimitiveTypeNonNullableSupport : Extension() {

    /**
     * Config key can't be private, as it will be accessed from `library` module
     */
    @Suppress("MemberVisibilityCanBePrivate")
    const val configKey = "xu.rui.force_primitive_type_non-nullable"


    override fun intercept(kotlinClass: KotlinClass): KotlinClass {

        if (getConfig(configKey).toBoolean().not()) {
            return kotlinClass
        }

        if (kotlinClass is DataClass) {

            val primitiveTypeNonNullableProperties = kotlinClass.properties.map {
                if (it.type in NULLABLE_PRIMITIVE_TYPES) {
                    val newType = getNonNullPrimitiveType(it.type)
                    if (ConfigManager.defaultValueStrategy != DefaultValueStrategy.None) {
                        it.copy(type = newType, value = getDefaultValue(newType))
                    } else {
                        it.copy(type = newType)
                    }
                } else {
                    it
                }
            }

            return kotlinClass.copy(properties = primitiveTypeNonNullableProperties)
        } else {
            return kotlinClass
        }
    }

    override fun createUI(): JPanel {
        return jHorizontalLinearLayout {
            jCheckBox("Force Primitive Type Property Non-Nullable", getConfig(configKey).toBoolean(), { isSelected -> setConfig(configKey, isSelected.toString()) })
            fillSpace()
        }
    }

}