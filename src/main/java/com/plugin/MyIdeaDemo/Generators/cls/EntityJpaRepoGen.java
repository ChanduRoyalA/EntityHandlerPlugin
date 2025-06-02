package com.plugin.MyIdeaDemo.Generators.cls;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.plugin.MyIdeaDemo.Generators.ifc.EntityJpaRepoGenIfc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class EntityJpaRepoGen implements EntityJpaRepoGenIfc {

    private final Project project;
    private final String entityClassName ;
    private final String primaryKeyDataType;
    private final String projectFolderStructure;


    public EntityJpaRepoGen(Project project, String entityClassName, String primaryKeyDataType, String projectFolderStructure) {
        this.entityClassName = entityClassName;
        this.project = project;
        this.primaryKeyDataType = primaryKeyDataType;
        this.projectFolderStructure = projectFolderStructure;
    }

    @Override
    public String generateJpaRepoInterface() throws IOException {
        String basePath = project.getBasePath();
        String path = projectFolderStructure + "/repository";
        File directory = new File(path);
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("Failed to create directory: " + path);
        }

        File jpaFile = new File(directory, entityClassName + "JpaRepository.java");
        if (jpaFile.createNewFile()) {
            System.out.println("Created File: " + jpaFile.getPath());
        }

        FileWriter writer;
        try {
            writer = new FileWriter(jpaFile);
        } catch (IOException e) {
            throw new IOException(e);
        }

        generateJpaRepo(writer);
        writer.close();

        VirtualFile vDir = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(directory);
        if (vDir != null) {
            vDir.refresh(true, true);
        }
        return entityClassName+"JpaRepository";
    }

    @Override
    public void generateJpaRepo(FileWriter writer) throws IOException {
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

        String interfaceName = entityClassName+"JpaRepository";

        String classTemplate = """
                package %srepository;
                
                import %sentity.%s;
                import org.springframework.data.jpa.repository.JpaRepository;
                
                public interface %s extends JpaRepository<%s,%s> {
                }
                """.formatted(packageName.toString(),packageName.toString(),entityClassName,interfaceName,entityClassName,primaryKeyDataType);
        writer.write(classTemplate);
        writer.close();
    }


}
