package extensions.wu.seal

import com.example.testplugin.model.classscodestruct.DataClass
import com.example.testplugin.model.classscodestruct.KotlinClass
import extensions.Extension

import com.example.testplugin.ui.jCheckBox
import com.example.testplugin.ui.jHorizontalLinearLayout
import javax.swing.JPanel

object InternalModifierSupport : Extension() {

    const val CONFIG_KEY = "wu.seal.internal_modifier_support"

    override fun createUI(): JPanel {

        return jHorizontalLinearLayout {
            jCheckBox(
                "Let classes to be internal",
                getConfig(CONFIG_KEY).toBoolean(),
                { isSelected -> setConfig(CONFIG_KEY, isSelected.toString()) })
            fillSpace()
        }
    }

    override fun intercept(kotlinClass: KotlinClass): KotlinClass {
        if (getConfig(CONFIG_KEY).toBoolean())
            if (kotlinClass is DataClass) {
                return kotlinClass.copy(codeBuilder = DataClassCodeBuilderForInternalClass(kotlinClass.codeBuilder))
            }
        return kotlinClass
    }
}