package com.plugin.MyIdeaDemo

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import com.plugin.MyIdeaDemo.Frames.CreateTableForm
import javax.swing.JFrame

class HelloAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project
        if (project == null) {
            Messages.showErrorDialog("No project found!", "Error")
            return
        }

        // Open the Swing form dialog
        val form = CreateTableForm(project)
        form.defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
        form.setLocationRelativeTo(null)
        form.isVisible = true
    }
}
