package com.example.testplugin.model.codeelements

import ai.grazie.utils.capitalize

/**
 * make name to be camel case
 * Created by Sealwu on 2017/9/18.
 */

interface IPropertyNameMaker {

    /**
     * make legal property name from a input raw string
     */
    fun makePropertyName(rawString: String): String


    /**
     * make legal property name from a input raw string
     */
    fun makePropertyName(rawString: String, needTransformToLegalName: Boolean): String


}

object KPropertyName : KName(), IPropertyNameMaker {
    override fun getName(rawName: String): String {
        return makePropertyName(rawName, true)
    }

    override fun makePropertyName(rawString: String): String {

        return rawString
    }

    override fun makePropertyName(rawString: String, needTransformToLegalName: Boolean): String {
        return if (needTransformToLegalName) {
            val camelCaseLegalName = makeLowerCamelCaseLegalNameOrEmptyName(rawString)
            if (camelCaseLegalName.isEmpty()) makeLowerCamelCaseLegalNameOrEmptyName("x-$rawString") else camelCaseLegalName
        } else {
            rawString
        }
    }

    /**
     * get the none empty legal came case name
     */
    fun makeLowerCamelCaseLegalName(rawNameString:String) :String{

        return makePropertyName(rawNameString, true)
    }

    /**
     * this function may return empty string when the raw string is only make of number and illegal character
     */
    fun makeLowerCamelCaseLegalNameOrEmptyName(rawString: String): String {
        /**
         * keep nameSeparator character
         */
        val pattern = illegalCharacter.toMutableList().apply { removeAll(nameSeparator) }.toRegex()

        val temp = rawString.replace(pattern, "").let {

            return@let removeStartNumberAndIllegalCharacter(it)

        }

        val lowerCamelCaseName = toLowerCamelCase(temp)

        return toBeLegalName(lowerCamelCaseName)
    }


    /**
     * this function can remove the rest white space
     */
    private fun toLowerCamelCase(temp: String): String {

        val stringBuilder = StringBuilder()

        temp.split(nameSeparator.toRegex()).forEach {
            if (it.isNotBlank()) {
                stringBuilder.append(it.substring(0, 1).toUpperCase().plus(it.substring(1)))
            }
        }

        val camelCaseName = stringBuilder.toString()

        return if (camelCaseName.isNotEmpty()) {
            camelCaseName.substring(0, 1).toLowerCase().plus(camelCaseName.substring(1))
        } else {
            camelCaseName
        }
    }

    fun classNameToCamelCase(className: String): String {
        if (className.isBlank()||className.isEmpty()) return className
        // Split the class name by uppercase letters
        val parts = className.split("(?=[A-Z])".toRegex())

        // Convert the parts to lowercase except the first one
        val camelCaseBuilder = StringBuilder(parts[0].lowercase())
        if (parts.size>=2){
            camelCaseBuilder.append(parts[1].lowercase())
        }
        if (parts.size>=3){
            for (i in 2 until parts.size) {
                camelCaseBuilder.append(parts[i].lowercase().capitalize())
            }
        }


        return camelCaseBuilder.toString()
    }
}
