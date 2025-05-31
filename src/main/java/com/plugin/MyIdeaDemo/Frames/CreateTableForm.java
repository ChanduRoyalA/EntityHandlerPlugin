package com.plugin.MyIdeaDemo.Frames;


import com.intellij.openapi.project.Project;
import com.plugin.MyIdeaDemo.Generators.cls.handleGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.HashMap;

public class CreateTableForm extends JFrame {
    private final JTextField projectFolderField = new JTextField(20);
    private final JTextField tableNameField = new JTextField(20);
    private final JTextField columnNameField = new JTextField(20);
    private final JComboBox<String> columnTypeComboBox = new JComboBox<>(new String[]{"Select","String", "Integer", "Boolean", "Date"});
    private final DefaultListModel<String> columnListModel = new DefaultListModel<>();
    private final HashMap<String, String> columns = new HashMap<>();

    public CreateTableForm(Project project) {
        setTitle("Create Entity Table");
        setSize(600, 600);
        setLocationRelativeTo(null); // Center the window
        setLayout(new BorderLayout(10, 10));
        setResizable(Boolean.FALSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Project Folder Structure
        JPanel projectPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        projectPanel.add(new JLabel("Project Folder Structure:"));
        projectPanel.add(projectFolderField);
        mainPanel.add(projectPanel);

        // Table name
        JPanel tablePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tablePanel.add(new JLabel("Table Name:"));
        tablePanel.add(tableNameField);
        mainPanel.add(tablePanel);

        // Column name + type
        JPanel columnInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        columnInputPanel.add(new JLabel("Column Name:"));
        columnInputPanel.add(columnNameField);
        columnInputPanel.add(new JLabel("Column Data Type:"));
        columnInputPanel.add(columnTypeComboBox);
        mainPanel.add(columnInputPanel);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addColumnButton = new JButton("Add Column");
        JButton generateButton = new JButton("Generate Entity");
        buttonPanel.add(addColumnButton);
        buttonPanel.add(generateButton);
        mainPanel.add(buttonPanel);

        add(mainPanel, BorderLayout.NORTH);

        // Column list view
        JList<String> columnList = new JList<>(columnListModel);
        JScrollPane scrollPane = new JScrollPane(columnList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Columns"));
        add(scrollPane, BorderLayout.CENTER);

        // Listeners
        addColumnButton.addActionListener((ActionEvent e) -> {
            String columnName = columnNameField.getText();
            String columnDataType = (String) columnTypeComboBox.getSelectedItem();
            int selectedDataType = columnTypeComboBox.getSelectedIndex();
            if(selectedDataType==0){
                JOptionPane.showMessageDialog(this, "Please select a valid data type for the column: "+columnName);
                return;
            }
            if (!columnName.isBlank()) {
                columnListModel.addElement(columnName + " : " + columnTypeComboBox.getSelectedItem());
                columns.put(columnName, columnDataType);
                columnNameField.setText("");
                columnTypeComboBox.setSelectedIndex(0);
            }
        });

        generateButton.addActionListener((ActionEvent e) -> {
            String tableName = tableNameField.getText();
            String projectFolderStructure = (projectFolderField.getText()).replace('.','/');


            if (tableName.isBlank() || columns.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a table name and add at least one column.");
                return;
            }

            try {
                handleGenerator generator = new handleGenerator(project, tableName, columns,projectFolderStructure);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            JOptionPane.showMessageDialog(this, "Entity successfully generated!");
            dispose();
        });
    }
}
