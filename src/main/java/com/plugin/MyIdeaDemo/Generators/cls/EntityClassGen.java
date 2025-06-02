package com.plugin.MyIdeaDemo.Generators.cls;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.plugin.MyIdeaDemo.Generators.ifc.EntityClassGenIfc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class EntityClassGen implements EntityClassGenIfc {
    private final Project project;
    private final String tableName;
    private final HashMap<String, String> columns;
    private final String entityClassName;
    private final String projectFolderStructure;

    public EntityClassGen(Project project, String tableName, HashMap<String, String> columns, String projectFolderStructure) {
        this.project = project;
        this.tableName = tableName;
        this.columns = columns;
        this.projectFolderStructure = projectFolderStructure;
        this.entityClassName = this.tableName.substring(0,1).toUpperCase()+this.tableName.substring(1).toLowerCase();
    }

    @Override
    public String generateEntityDetails() throws IOException {
        String basePath = project.getBasePath();
        String path = projectFolderStructure + "/entity";

        File directory = new File(path);
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("Failed to create directory: " + path);
        }

        File entityFile = new File(directory, entityClassName + ".java");
        if (entityFile.createNewFile()) {
            System.out.println("Created File: " + entityFile.getPath());
        }

        generateEntityCodeInFile(entityFile);

        // Refresh IntelliJ VFS
        VirtualFile vDir = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(directory);
        if (vDir != null) {
            vDir.refresh(true, true);
        }

        return this.entityClassName;
    }


    @Override
    public void generateEntityCodeInFile(File entityFile) throws IOException {
        try (FileWriter writer = new FileWriter(entityFile)) {
            generatePackageAndImports(writer);
            generateClassFile(writer);
        }
    }

    @Override
    public void generateClassFile(FileWriter writer) throws IOException {
        String classTemplate = """
                
                public class %s {
                
                public %s(){
                  /*
                   Default Constructor
                  */
                }
                
                """.formatted(entityClassName,entityClassName);
        writer.write(classTemplate);
        generateInstanceVariablesAndSetterGetters(writer);
        writer.write("}\n");
    }

    @Override
    public void generateInstanceVariablesAndSetterGetters(FileWriter writer) throws IOException {
        for (String column : columns.keySet()) {
            String dataType = columns.get(column);
            String capitalized = column.substring(0, 1).toUpperCase() + column.substring(1);
            if(column.equalsIgnoreCase("id")){
                writer.write(String.format("""
                        @Id
                        @GeneratedValue(strategy = GenerationType.IDENTITY)
                        private %s %s;

                        public %s get%s() {
                            return this.%s;
                        }
                        public void set%s(%s %s) {
                            this.%s = %s;
                        }
                    """, dataType, column, dataType, capitalized, column, capitalized, dataType, column, column, column));
            }
            else{
                writer.write(String.format("""
                        @Column(name = "%s")
                        private %s %s;
                    
                        public %s get%s() {
                            return this.%s;
                        }
                        public void set%s(%s %s) {
                            this.%s = %s;
                        }
                    """, column, dataType, column, dataType, capitalized, column, capitalized, dataType, column, column, column));
            }
        }
    }

    @Override
    public void generatePackageAndImports(FileWriter writer) throws IOException {
        StringBuilder packageName = new StringBuilder("");
        String[] folderArray = projectFolderStructure.split("/");
        boolean isJavaPassed = false;
        for(int i=0;i<folderArray.length;i++){
            if(isJavaPassed){
                packageName.append(folderArray[i]).append(".");
            }
            if(folderArray[i].equalsIgnoreCase("java")){
                isJavaPassed = true;
            }
        }


        String template = """
                package %sentity;
                
                import jakarta.persistence.*;

                @Entity
                @Table(name = "%s")
                """.formatted(packageName.toString(),tableName);
        writer.write(template);
    }

}
