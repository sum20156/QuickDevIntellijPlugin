package extensions.wu.seal

import com.example.testplugin.model.classscodestruct.Annotation
import com.example.testplugin.model.classscodestruct.DataClass
import com.example.testplugin.model.classscodestruct.KotlinClass
import com.google.gson.annotations.Expose
import extensions.Extension

import com.example.testplugin.ui.addSelectListener
import com.example.testplugin.ui.jCheckBox
import com.example.testplugin.ui.jHorizontalLinearLayout
import javax.swing.JPanel

object AddGsonExposeAnnotationSupport : Extension() {

    @Expose
    val config_key = "wu.seal.add_gson_expose_annotation"

    override fun createUI(): JPanel {
        return jHorizontalLinearLayout {
            jCheckBox("Add Gson @Expose Annotation", config_key.booleanConfigValue) {
                addSelectListener { setConfig(config_key, it.toString()) }
            }
            fillSpace()
        }
    }

    override fun intercept(kotlinClass: KotlinClass): KotlinClass {
        return if (config_key.booleanConfigValue) {
            if (kotlinClass is DataClass) {
                val newProperties = kotlinClass.properties.map {
                    val newAnnotations = it.annotations + Annotation.fromAnnotationString("@Expose")
                    it.copy(annotations = newAnnotations)
                }
                kotlinClass.copy(properties = newProperties)
            } else kotlinClass
        } else kotlinClass
    }

    override fun intercept(originClassImportDeclaration: String): String {
        return if (config_key.booleanConfigValue) {
            originClassImportDeclaration.append("import com.google.gson.annotations.Expose")
        } else originClassImportDeclaration
    }
}