package com.example.testplugin

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.PsiManager
import com.intellij.psi.impl.file.PsiDirectoryFactory
import com.example.testplugin.feedback.dealWithException
import com.example.testplugin.interceptor.InterceptorManager
import com.example.testplugin.model.ConfigManager
import com.example.testplugin.ui.JsonInputDialog
import com.example.testplugin.kotlin_gen.KotlinClassFileGenerator
import com.example.testplugin.model.UnSupportJsonException
import com.example.testplugin.kotlin_gen.KotlinClassMaker
import com.example.testplugin.kotlin_gen.apiInterface.ApiInterfaceFileGenerator
import com.example.testplugin.kotlin_gen.repo_impl.RepoImplClassFileGenerator
import com.example.testplugin.kotlin_gen.usecase.UseCaseClassFileGenerator
import com.example.testplugin.kotlin_gen.repo_interface.RepoInterfaceFileGenerator
import com.example.testplugin.kotlin_gen.viewmodel.ViewModelClassFileGenerator
import com.example.testplugin.model.classscodestruct.ApiInterface
import com.example.testplugin.model.classscodestruct.DataClass
import com.example.testplugin.model.classscodestruct.RepoInterface
import com.example.testplugin.model.classscodestruct.UseCaseClass
import com.example.testplugin.utils.classgenerator.ViewModelClassGenerator
import com.example.testplugin.utils.isJSONSchema


class GenerateKotlinFileAction : AnAction("Json to Class") {

    override fun actionPerformed(event: AnActionEvent) {
        var jsonString = ""
        try {
            val project = event.getData(PlatformDataKeys.PROJECT) ?: return

            val dataContext = event.dataContext
            val module = LangDataKeys.MODULE.getData(dataContext) ?: return

            val directory = when (val navigatable = LangDataKeys.NAVIGATABLE.getData(dataContext)) {
                is PsiDirectory -> navigatable
                is PsiFile -> navigatable.containingDirectory
                else -> {
                    val root = ModuleRootManager.getInstance(module)
                    root.sourceRoots
                        .asSequence()
                        .mapNotNull {
                            PsiManager.getInstance(project).findDirectory(it)
                        }.firstOrNull()
                }
            } ?: return

            val directoryFactory = PsiDirectoryFactory.getInstance(directory.project)
            val packageName = directoryFactory.getQualifiedName(directory, false)
            val psiFileFactory = PsiFileFactory.getInstance(project)
            val packageDeclare = if (packageName.isNotEmpty()) "package $packageName" else ""
            val inputDialog = JsonInputDialog("", project)
            inputDialog.show()
            val className = inputDialog.getClassName()
            val inputString = inputDialog.inputString.takeIf { it.isNotEmpty() } ?: return

            jsonString = inputString
            doGenerateKotlinDataClassFileAction(
                className,
                inputString,
                packageDeclare,
                project,
                psiFileFactory,
                directory
            )
        } catch (e: UnSupportJsonException) {
            val advice = e.advice
            Messages.showInfoMessage(dealWithHtmlConvert(advice), "Tip")
        } catch (e: Throwable) {
            dealWithException(jsonString, e)
            throw e
        }
    }

    private fun dealWithHtmlConvert(advice: String) = advice.replace("<", "&lt;").replace(">", "&gt;")

    private fun doGenerateKotlinDataClassFileAction(
        className: String,
        json: String,
        packageDeclare: String,
        project: Project?,
        psiFileFactory: PsiFileFactory,
        directory: PsiDirectory
    ) {
        val kotlinClass = KotlinClassMaker(className, json).makeKotlinClass() as DataClass
        val dataClassAfterApplyInterceptor =
            kotlinClass.applyInterceptors(InterceptorManager.getEnabledKotlinDataClassInterceptors()) as DataClass
        if (ConfigManager.isInnerClassModel) {

            KotlinClassFileGenerator().generateSingleKotlinClassFile(
                packageDeclare,
                dataClassAfterApplyInterceptor,
                project,
                psiFileFactory,
                directory,
                json
            )
        } else {
            KotlinClassFileGenerator().generateMultipleKotlinClassFiles(
                dataClassAfterApplyInterceptor,
                packageDeclare,
                project,
                psiFileFactory,
                directory,
                json.isJSONSchema(),
                json
            )
        }

        if (ConfigManager.isApiClasses){
            val apiInterfaceName = dataClassAfterApplyInterceptor.name.replace("response", "",ignoreCase = true)+"Api"

            val apiInterface:ApiInterface = ApiInterfaceFileGenerator().generateApiInterfaceFile(
                packageDeclare,
                apiInterfaceName,
                dataClassAfterApplyInterceptor,
                project,
                psiFileFactory,
                directory,
            ) as ApiInterface

            val repoInterfaceName = dataClassAfterApplyInterceptor.name.replace("response", "",ignoreCase = true)+"Repo"
            val repoInterface=RepoInterfaceFileGenerator().generateRepoInterfaceClassFile(
                packageDeclare,
                fileName = repoInterfaceName,
                project,
                psiFileFactory,
                directory,
            ) as RepoInterface


            val repoImplClassName = dataClassAfterApplyInterceptor.name.replace("response", "",ignoreCase = true)+"RepoImpl"
            RepoImplClassFileGenerator().generateRepoImplClassFile(
                packageDeclare,
                fileName = repoImplClassName,
                apiInterface,
                repoInterface,
                project,
                psiFileFactory,
                directory,
            )

            val useCaseClassName = dataClassAfterApplyInterceptor.name.replace("response", "",ignoreCase = true)+"UseCase"
            val useCase = UseCaseClassFileGenerator().generateUseCaseClassFile(
                packageDeclare,
                fileName = useCaseClassName,
                repoInterface,
                project,
                psiFileFactory,
                directory,
            ) as UseCaseClass

            val viewModelClassName = dataClassAfterApplyInterceptor.name.replace("response", "",ignoreCase = true)+"ViewModel"
            ViewModelClassFileGenerator().generateViewModelClassFile(
                packageDeclare,
                fileName = viewModelClassName,
                useCase,
                project,
                psiFileFactory,
                directory,
            )
        }

    }
}
