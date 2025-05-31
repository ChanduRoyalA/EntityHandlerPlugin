package com.plugin.MyIdeaDemo.Generators.ifc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public interface EntityClassGenIfc {
//    void setEntityDetails(String tableName, HashMap<String,Integer> columns) throws IOException;

    String generateEntityDetails() throws IOException;

    void generateEntityCodeInFile(File entityFile) throws IOException;

    void generateClassFile(FileWriter writer) throws IOException;

    void generateInstanceVariablesAndSetterGetters(FileWriter writer) throws IOException;

//    String getDataTypeById(Integer integer);

    void generatePackageAndImports(FileWriter writer) throws IOException;
}
