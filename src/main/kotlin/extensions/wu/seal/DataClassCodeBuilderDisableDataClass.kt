package extensions.wu.seal

import com.example.testplugin.model.classscodestruct.DataClass
import com.example.testplugin.model.builder.IKotlinDataClassCodeBuilder

/**
 * kotlin class code generator with internal modifier before class
 *
 * Created by Seal on 2020/7/7 21:40.
 */
class DataClassCodeBuilderDisableDataClass(private val kotlinDataClassCodeBuilder: IKotlinDataClassCodeBuilder) :
    BaseDataClassCodeBuilder(kotlinDataClassCodeBuilder) {

    override fun DataClass.genClassName(): String {
        val originClassName = kotlinDataClassCodeBuilder.run { genClassName() }
        return originClassName.replace("data ", "")
    }
}
