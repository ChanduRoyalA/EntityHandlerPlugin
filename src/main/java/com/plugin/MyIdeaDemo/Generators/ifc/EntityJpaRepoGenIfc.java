package com.plugin.MyIdeaDemo.Generators.ifc;

import java.io.FileWriter;
import java.io.IOException;

public interface EntityJpaRepoGenIfc {

    String generateJpaRepoInterface() throws Exception;

    void generateJpaRepo(FileWriter writer) throws IOException;
}
