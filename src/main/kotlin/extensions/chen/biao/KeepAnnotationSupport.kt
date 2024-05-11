package extensions.chen.biao

import com.example.testplugin.model.classscodestruct.Annotation
import com.example.testplugin.model.classscodestruct.DataClass
import com.example.testplugin.model.classscodestruct.KotlinClass
import extensions.Extension
import com.example.testplugin.ui.jCheckBox
import com.example.testplugin.ui.jHorizontalLinearLayout
import javax.swing.JPanel

/**
 * @author chenbiao
 * create at 2019/5/16
 * description:
 */
object KeepAnnotationSupport : Extension() {


    @Suppress("MemberVisibilityCanBePrivate")
    /**
     * Config key can't be private, as it will be accessed from `library` module
     */
    const val configKey = "chen.biao.add_keep_annotation_enable"

    override fun createUI(): JPanel {
        return jHorizontalLinearLayout {
            jCheckBox("Add @Keep Annotation On Class ", getConfig(configKey).toBoolean(), { isSelected -> setConfig(configKey, isSelected.toString()) })
            fillSpace()
        }
    }

    override fun intercept(kotlinClass: KotlinClass): KotlinClass {

        if (kotlinClass is DataClass) {
            return if (getConfig(configKey).toBoolean()) {

                val classAnnotationString = "@Keep"

                val classAnnotation = Annotation.fromAnnotationString(classAnnotationString)

                val newAnnotations = mutableListOf(classAnnotation).also { it.addAll(kotlinClass.annotations) }

                return kotlinClass.copy(annotations = newAnnotations)
            } else {
                kotlinClass
            }
        } else {
            return kotlinClass
        }

    }

    override fun intercept(originClassImportDeclaration: String): String {

        val classAnnotationImportClassString = "import android.support.annotation.Keep"

        return if (getConfig(configKey).toBoolean()) {
            originClassImportDeclaration.append(classAnnotationImportClassString)
        } else {
            originClassImportDeclaration
        }
    }
}