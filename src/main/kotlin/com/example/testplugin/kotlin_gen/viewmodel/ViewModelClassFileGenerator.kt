package com.example.testplugin.kotlin_gen.viewmodel

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFileFactory
import com.example.testplugin.model.classscodestruct.KotlinClass
import com.example.testplugin.filetype.KotlinFileType
import com.example.testplugin.model.classscodestruct.UseCaseClass
import com.example.testplugin.utils.ClassImportDeclaration
import com.example.testplugin.utils.executeCouldRollBackAction

class ViewModelClassFileGenerator {

    fun generateViewModelClassFile(
        packageDeclare: String,
        fileName: String,
        useCaseClass: UseCaseClass,
        project: Project?,
        psiFileFactory: PsiFileFactory,
        directory: PsiDirectory,
    ):KotlinClass {
        val fileNamesWithoutSuffix = currentDirExistsFileNamesWithoutKTSuffix(directory)
        var kotlinClassForGenerateFile = ViewModelClassMaker(fileName,useCaseClass).makeKotlinClass()
        while (fileNamesWithoutSuffix.contains(fileName)) {
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
        return kotlinClassForGenerateFile
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
