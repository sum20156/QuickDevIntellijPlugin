package com.example.testplugin.ui

import com.example.testplugin.feedback.ClickProjectURLAction
import com.example.testplugin.feedback.FormatJSONAction
import com.example.testplugin.feedback.sendActionInfo
import com.example.testplugin.model.ConfigManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.intellij.json.JsonFileType
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.progress.util.DispatchThreadProgressWindow
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.util.ui.JBDimension

import com.example.testplugin.utils.executeCouldRollBackAction
import java.awt.Dimension
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.net.URL
import java.util.*
import java.util.Timer
import javax.swing.*
import javax.swing.text.JTextComponent

/**
 * Dialog widget relative
 * Created by Seal.wu on 2017/9/21.
 */

/**
 * Json input Dialog
 */
private val jsonInputDialogValidator: JsonInputDialogValidator = JsonInputDialogValidator()

class JsonInputDialog(classsName: String, private val project: Project) : Messages.InputDialog(
    project,
    "Please input the JSON String and class name to generate Kotlin data class",
    "Generate Kotlin Data Class Code",
    null,
    "",
    jsonInputDialogValidator
) {
    private lateinit var jsonContentEditor: Editor

    private val prettyGson: Gson = GsonBuilder().setPrettyPrinting().serializeNulls().disableHtmlEscaping().create()

    init {
        setOKButtonText("Generate")
        myField.text = classsName
    }

    override fun createNorthPanel(): JComponent? {

        return jHorizontalLinearLayout {
          //  jIcon("/icons/icon_json_input_dialog.png")
            fixedSpace(5)
            jVerticalLinearLayout {
                alignLeftComponent {
                    jLabel(myMessage!!, 12f)
                }
                jHorizontalLinearLayout {
                    jLabel("JSON Text: ", 14f)
                    jLabel("Tips: you can use JSON string, http urls or local file just right click on text area", 12f)
                    fillSpace()
                    jButton("Format", { handleFormatJSONString() })
                }
            }
        }
    }

    override fun createCenterPanel(): JComponent? {
        jsonContentEditor = createJsonContentEditor()
        jsonInputDialogValidator.jsonInputEditor = jsonContentEditor

        //remove ˚
        Timer().schedule(object : TimerTask() {
            override fun run() {
                SwingUtilities.invokeLater {
                    executeCouldRollBackAction(project) {
                        jsonContentEditor.document.setText("")
                    }
                }
            }
        }, 100)

        myField = createTextFieldComponent()

        return jBorderLayout {

            putCenterFill(jsonContentEditor.component)

            bottomContainer {
                jVerticalLinearLayout {
                    fixedSpace(7)
                    jHorizontalLinearLayout {
                        jLabel("Generate: ")
                      //  ClassSelector()
                        jCheckBox(
                            "ViewModel, Repository, UseCase, Api Interface",
                            ConfigManager.isApiClasses,
                            { isSelected -> ConfigManager.isApiClasses = isSelected })

                    }

                    fixedSpace(7)

                    jHorizontalLinearLayout {
                        jLabel("Class Name: ", 14f)
                        add(myField)
                    }
                    fixedSpace(3)
                    jHorizontalLinearLayout {
                        jButton("Advanced", { AdvancedDialog(false).show() })
                        fillSpace()
                        jLabel("Like this version? Please star here: ")
                        jLink(
                            "https://github.com/sum20156/QuickDevIntellijPlugin",
                            "https://github.com/sum20156/QuickDevIntellijPlugin",
                            maxSize = JBDimension(210, 30)
                        ) {
                            sendActionInfo(prettyGson.toJson(ClickProjectURLAction()))
                        }
                    }
                }
            }
        }
    }


    private fun createAdvancedPanel(): JPanel {

        return jHorizontalLinearLayout {
            jButton("Advanced", { AdvancedDialog(false).show() })
            fillSpace()
            jLabel("Like this version? Please star here: ")
            jLink(
                "https://github.com/sum20156/QuickDevIntellijPlugin",
                "https://github.com/sum20156/QuickDevIntellijPlugin",
                maxSize = JBDimension(210, 30)
            ) {
                sendActionInfo(prettyGson.toJson(ClickProjectURLAction()))
            }
        }
    }

    private fun createJsonContentEditor(): Editor {
        val editorFactory = EditorFactory.getInstance()
        val document = editorFactory.createDocument("").apply {
            setReadOnly(false)
            addDocumentListener(object : com.intellij.openapi.editor.event.DocumentListener {
                override fun documentChanged(event: DocumentEvent) = revalidate()

                override fun beforeDocumentChange(event: DocumentEvent) = Unit
            })
        }

        val editor = editorFactory.createEditor(document, null, JsonFileType.INSTANCE, false)

        editor.component.apply {
            isEnabled = true
            preferredSize = Dimension(640, 480)
            autoscrolls = true
        }

        val contentComponent = editor.contentComponent
        contentComponent.isFocusable = true
        contentComponent.componentPopupMenu = JPopupMenu().apply {
            add(createPasteFromClipboardMenuItem())
            add(createRetrieveContentFromHttpURLMenuItem())
            add(createLoadFromLocalFileMenu())
        }

        return editor
    }

    override fun createTextFieldComponent(): JTextComponent {

        return jTextInput(maxSize = JBDimension(10000, 35)) {
            document = NamingConventionDocument()
        }
    }

    private fun createPasteFromClipboardMenuItem() = JMenuItem("Paste from clipboard").apply {
        addActionListener {
            val transferable = Toolkit.getDefaultToolkit().systemClipboard.getContents(null)
            if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                runWriteAction {
                    jsonContentEditor.document.setText(transferable.getTransferData(DataFlavor.stringFlavor).toString())
                }
            }
        }
    }

    private fun createRetrieveContentFromHttpURLMenuItem() = JMenuItem("Retrieve content from Http URL").apply {
        addActionListener {
            val url = Messages.showInputDialog("URL", "Retrieve content from Http URL", null, null, UrlInputValidator)
            val p = DispatchThreadProgressWindow(false, project)
            p.isIndeterminate = true
            p.setRunnable {
                try {
                    val urlContent = URL(url).readText()
                    runWriteAction {
                        jsonContentEditor.document.setText(urlContent.replace("\r\n", "\n"))
                    }
                } finally {
                    p.stop()
                }
            }
            p.start()
        }
    }

    private fun createLoadFromLocalFileMenu() = JMenuItem("Load from local file").apply {
        addActionListener {
            FileChooser.chooseFile(FileChooserDescriptor(true, false, false, false, false, false), null, null) { file ->
                val content = String(file.contentsToByteArray())
                ApplicationManager.getApplication().runWriteAction {
                    jsonContentEditor.document.setText(content.replace("\r\n", "\n"))
                }
            }
        }
    }

    /**
     * get the user input class name
     */
    fun getClassName(): String {
        return if (exitCode == 0) {
            val name = myField.text.trim()
            name.let { if (it.first().isDigit() || it.contains('$')) "`$it`" else it }
        } else ""
    }

    override fun getInputString(): String = if (exitCode == 0) jsonContentEditor.document.text.trim() else ""

    override fun getPreferredFocusedComponent(): JComponent? {
        return jsonContentEditor.contentComponent
    }

    fun handleFormatJSONString() {
        val currentText = jsonContentEditor.document.text
        if (currentText.isNotEmpty()) {
            try {
                val jsonElement = prettyGson.fromJson(currentText, JsonElement::class.java)
                val formatJSON = prettyGson.toJson(jsonElement)
                runWriteAction {
                    jsonContentEditor.document.setText(formatJSON)
                }
            } catch (e: Exception) {
            }
        }

        feedBackFormatJSONActionInfo()
    }

    private fun feedBackFormatJSONActionInfo() {
        Thread { sendActionInfo(prettyGson.toJson(FormatJSONAction())) }.start()
    }

    private fun revalidate() {
        okAction.isEnabled = jsonInputDialogValidator.checkInput(myField.text)
    }
}
