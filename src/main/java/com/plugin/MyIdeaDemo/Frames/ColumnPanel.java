package com.plugin.MyIdeaDemo.Frames;

import com.intellij.openapi.ui.ComboBox;

import javax.swing.*;
import java.awt.*;

public class ColumnPanel {
    private final JPanel columnPanel;
    private final JTextField columnName;
    private final ComboBox<String> dataType;

    public ColumnPanel() {
        columnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel columnLabel = new JLabel("Column Name:");
        columnName = new JTextField(15);

        JLabel typeLabel = new JLabel("Data Type:");
        dataType = new ComboBox<>(new String[]{"String", "Integer", "Boolean", "Date"});

        columnPanel.add(columnLabel);
        columnPanel.add(columnName);
        columnPanel.add(typeLabel);
        columnPanel.add(dataType);
    }

    public JPanel getPanel() {
        return columnPanel;
    }

    public String getColumnName() {
        return columnName.getText().trim();
    }

    public String getDataType() {
        return (String) dataType.getSelectedItem();
    }
}
