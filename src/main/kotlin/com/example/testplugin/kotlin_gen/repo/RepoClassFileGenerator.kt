package com.example.testplugin.kotlin_gen.repo

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFileFactory
import com.example.testplugin.model.classscodestruct.KotlinClass
import com.example.testplugin.model.ConfigManager
import com.example.testplugin.utils.distinctByPropertiesAndSimilarClassName
import com.example.testplugin.utils.toJavaDocMultilineComment
import com.example.testplugin.filetype.KotlinFileType
import com.example.testplugin.utils.ClassImportDeclaration
import extensions.yuan.varenyzc.NeedNonNullableClassesSupport.append
import wu.seal.jsontokotlin.utils.IgnoreCaseStringSet
import wu.seal.jsontokotlin.utils.executeCouldRollBackAction
import wu.seal.jsontokotlin.utils.showNotify

class RepoClassFileGenerator {

    fun generateRepoClassFile(
        packageDeclare: String,
        baseResponseClass: KotlinClass,
        project: Project?,
        psiFileFactory: PsiFileFactory,
        directory: PsiDirectory,
    ) {
        val fileNamesWithoutSuffix = currentDirExistsFileNamesWithoutKTSuffix(directory)
        val repoName=baseResponseClass.name.replace("response", "",ignoreCase = true)
            .append("Repo")
        var kotlinClassForGenerateFile = RepoClassMaker(repoName,baseResponseClass).makeKotlinClass()
        while (fileNamesWithoutSuffix.contains(repoName)) {
            kotlinClassForGenerateFile =
                    kotlinClassForGenerateFile.rename(newName = kotlinClassForGenerateFile.name + "X")
        }
        generateKotlinClassFile(
                kotlinClassForGenerateFile.name,
                packageDeclare,
                kotlinClassForGenerateFile.getCode(),
                project,
                psiFileFactory,
                directory,
        )

    }


    private fun currentDirExistsFileNamesWithoutKTSuffix(directory: PsiDirectory): List<String> {
        val kotlinFileSuffix = ".kt"
        return directory.files.filter { it.name.endsWith(kotlinFileSuffix) }
                .map { it.name.dropLast(kotlinFileSuffix.length) }
    }


    private fun generateKotlinClassFile(
            fileName: String,
            packageDeclare: String,
            classCodeContent: String,
            project: Project?,
            psiFileFactory: PsiFileFactory,
            directory: PsiDirectory,
    ) {
        val kotlinFileContent = buildString {
            if (packageDeclare.isNotEmpty()) {
                append(packageDeclare)
                append("\n\n")
            }
            val importClassDeclaration = ClassImportDeclaration.getImportClassDeclaration()
            if (importClassDeclaration.isNotBlank()) {
                append(importClassDeclaration)
                append("\n\n")
            }

            append(classCodeContent)
        }
        executeCouldRollBackAction(project) {
            val file =
                    psiFileFactory.createFileFromText("${fileName.trim('`')}.kt", KotlinFileType(), kotlinFileContent)
            directory.add(file)
        }
    }

}
