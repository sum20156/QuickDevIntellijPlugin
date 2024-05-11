package com.example.testplugin.interceptor

import com.example.testplugin.model.classscodestruct.DataClass
import com.example.testplugin.model.classscodestruct.KotlinClass
import com.example.testplugin.utils.getOutType

class PropertyTypeNullableStrategyInterceptor : IKotlinClassInterceptor<KotlinClass> {

    override fun intercept(kotlinClass: KotlinClass): KotlinClass {

        return if (kotlinClass is DataClass) {

            val propertyTypeAppliedWithNullableStrategyProperties = kotlinClass.properties.map {

                val propertyTypeAppliedWithNullableStrategy = getOutType(it.type, it.originJsonValue)

                it.copy(type = propertyTypeAppliedWithNullableStrategy)
            }

            kotlinClass.copy(properties = propertyTypeAppliedWithNullableStrategyProperties)
        } else {
            kotlinClass
        }

    }

}
