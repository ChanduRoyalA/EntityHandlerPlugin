package com.plugin.MyIdeaDemo

import com.github.weisj.jsvg.e
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import com.plugin.MyIdeaDemo.Frames.CreateTableForm
import javax.swing.JFrame


class HelloAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project
        if (project == null) {
            Messages.showErrorDialog("No project found!", "Error")
            return
        }

        val folder: VirtualFile? = event.getData(CommonDataKeys.VIRTUAL_FILE)

        if (folder != null && folder.isDirectory) {
            val path = folder.path
            println("Generating entity in: $path")
        }
        val folderPath = folder?.path
        // Open the Swing form dialog
        val form = CreateTableForm(project,folderPath)
        form.defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
        form.setLocationRelativeTo(null)
        form.isVisible = true
    }
}
