package com.example.testplugin.model.classscodestruct

import com.example.testplugin.model.classscodestruct.KotlinClass

interface ModifiableKotlinClass : KotlinClass {

    override val modifiable: Boolean
        get() = true


}