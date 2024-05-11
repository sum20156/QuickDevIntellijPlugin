package extensions.yuan.varenyzc

import com.example.testplugin.model.classscodestruct.DataClass
import com.example.testplugin.model.classscodestruct.KotlinClass
import extensions.Extension
import com.example.testplugin.ui.jCheckBox
import com.example.testplugin.ui.jHorizontalLinearLayout
import javax.swing.JPanel

object CamelCaseSupport : Extension() {

    /**
     * Config key can't be private, as it will be accessed from `library` module
     */
    @Suppress("MemberVisibilityCanBePrivate")
    const val configKey = "top.varenyzc.camel_case_enable"

    override fun createUI(): JPanel {
        return jHorizontalLinearLayout {
            jCheckBox(
                "Let properties' name to be camel case",
                getConfig(configKey).toBoolean(),
                { isSelected -> setConfig(configKey, isSelected.toString()) })
            fillSpace()
        }
    }

    override fun intercept(kotlinClass: KotlinClass): KotlinClass {
        if (kotlinClass is DataClass) {
            return if (getConfig(configKey).toBoolean()) {
                val originProperties = kotlinClass.properties
                val newProperties = originProperties.map {
                    val oldName = it.name
                    if (oldName.isNotEmpty() && oldName.contains("_")) {
                        val newName = StringBuilder().run {
                            val list = oldName.split("_")
                            for (s in list) {
                                if (this.isEmpty()) {
                                    append(s)
                                } else {
                                    append(s.substring(0, 1).toUpperCase())
                                    append(s.substring(1).toLowerCase())
                                }
                            }
                            toString()
                        }
                        it.copy(name = newName)
                    } else it
                }
                kotlinClass.copy(properties = newProperties)
            } else {
                kotlinClass
            }
        } else {
            return kotlinClass
        }
    }
}