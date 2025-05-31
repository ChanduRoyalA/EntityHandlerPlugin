package com.plugin.MyIdeaDemo.Generators.cls;

import com.intellij.openapi.project.Project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class handleGenerator{
    private final Project project;
    private final String tableName;
    private final HashMap<String, String> columns;
    private final String projectFolderStructure;
    private String primaryKeyDataType ;


    public handleGenerator(Project project, String tableName, HashMap<String, String> columns, String projectFolderStructure) throws IOException {
        this.project = project;
        this.tableName = tableName;
        this.columns = columns;
        this.projectFolderStructure = projectFolderStructure;
        getPrimaryKeyDataType(this.columns);
        handleEntityGen();
    }

    private void getPrimaryKeyDataType(HashMap<String, String> columns) {
        ArrayList<String> dataTypes = new ArrayList<>(columns.keySet());
        for(String dataType:dataTypes){
            if(dataType.equalsIgnoreCase("id")){
                this.primaryKeyDataType = columns.get(dataType);
            }
        }
    }

    private void handleEntityGen() throws IOException{
        EntityClassGen entityClassGen = new EntityClassGen(this.project,this.tableName,this.columns,this.projectFolderStructure);
        String entityClassName = entityClassGen.generateEntityDetails();
        EntityJpaRepoGen entityJpaRepoGen = new EntityJpaRepoGen(this.project,entityClassName,this.primaryKeyDataType,this.projectFolderStructure);
        String entityJpaRepoName = entityJpaRepoGen.generateJpaRepoInterface();

    }
}
