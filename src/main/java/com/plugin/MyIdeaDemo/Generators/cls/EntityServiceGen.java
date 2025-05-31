package com.plugin.MyIdeaDemo.Generators.cls;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.plugin.MyIdeaDemo.Generators.ifc.EntityServiceGenIfc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class EntityServiceGen implements EntityServiceGenIfc {

    private final String entityJpaRepoName ;
    private final String projectFolderStructure;
    private final Project project;
    private final String entityClassName;
    private final String JpaRepoInstanceName;
    private final String primaryKeyDataType;

    public EntityServiceGen(Project project, String entityJpaRepoName, String entityClassName, String projectFolderStructure, String primaryKeyDataType) {
        this.entityJpaRepoName = entityJpaRepoName;
        this.projectFolderStructure = projectFolderStructure;
        this.project = project;
        this.entityClassName = entityClassName;
        this.JpaRepoInstanceName = entityJpaRepoName.substring(0,1).toLowerCase()+entityJpaRepoName.substring(1);
        this.primaryKeyDataType = primaryKeyDataType;
    }

    public String generateJpaRepoInterface() throws IOException {
        String basePath = project.getBasePath();
        String path = basePath + "/src/main/java/"+projectFolderStructure+"/service";
        File directory = new File(path);
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("Failed to create directory: " + path);
        }

        File jpaFile = new File(directory, entityClassName + "JpaService.java");
        if (jpaFile.createNewFile()) {
            System.out.println("Created File: " + jpaFile.getPath());
        }

        FileWriter writer;
        try {
            writer = new FileWriter(jpaFile);
        } catch (IOException e) {
            throw new IOException(e);
        }

        generateJpaService(writer);
        writer.close();

        VirtualFile vDir = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(directory);
        if (vDir != null) {
            vDir.refresh(true, true);
        }
        return entityClassName+"JpaService";
    }

    public void generateJpaService(FileWriter writer) throws IOException {
        String className = entityClassName+"JpaService";
        
        String classTemplate = """
                package %s.service;
                
                import org.springframework.beans.factory.annotation.Autowired;
                import org.springframework.stereotype.Service;
                import %s.repository.%s;
                import %s.entity.%s;
                
                public class %s{
                
                    @Autowired
                    private %s %s;
                
                
                """.formatted(projectFolderStructure,projectFolderStructure,entityJpaRepoName,projectFolderStructure,entityClassName,className,entityJpaRepoName,JpaRepoInstanceName);
        writer.write(classTemplate);
        generateBasicServiceOperations(writer);
        writer.write("}");
        writer.close();
    }

    private void generateBasicServiceOperations(FileWriter writer) throws IOException {
        String getEntityServiceTemplate = """
                    /*
                        Gets the %s for given primary key
                        returns -> the Instance of %s with the given primary key (fetchObj)
                    */
                    public %s get%s(%s primaryKey) {
                        %s fetchObj = %s.findById(primaryKey).get();
                        return fetchObj;
                    }
                
                
                """.formatted(entityClassName,entityClassName,entityClassName,entityClassName,primaryKeyDataType,entityClassName,JpaRepoInstanceName);

        String addEntityServiceTemplate = """
                    /*
                        add the %s's instance
                        returns -> the added Instance of %s
                    */
                    public %s add%s(%s newObj) {
                         %s addedObj = %s.save(newObj);
                         return addedObj;
                    }
                
                
                """.formatted(entityClassName,entityClassName,entityClassName,entityClassName,entityClassName,entityClassName,JpaRepoInstanceName);

        String updateEntityServiceTemplate = """
                    /*
                        update the %s's instance
                        returns -> the updated Instance of %s
               
                    public %s update%s(%s newObj) {
                         %s addedObj = %s.save(newObj);
                         return addedObj;
                    }
                     the above method is not built yet
                    */
               
              
               """.formatted(entityClassName,entityClassName,entityClassName,entityClassName,entityClassName,entityClassName,JpaRepoInstanceName);

        String deleteEntityServiceTemplate = """
                    /*
                        delete the %s's instance
                    */
                    public void delete%s(%s primaryKey) {
                          %s.deleteById(primaryKey);
                    }
               
               
               """.formatted(entityClassName,entityClassName,primaryKeyDataType,JpaRepoInstanceName);
        writer.write(getEntityServiceTemplate);
        writer.write(addEntityServiceTemplate);
        writer.write(updateEntityServiceTemplate);
        writer.write(deleteEntityServiceTemplate);
    }
}
