package com.crio.qcharm.runner;
import com.crio.qcharm.ds.RunCodeOutput;
import java.util.List;

/*
 *  DONT'T EDIT
 */

public class CppRunner extends BaseRunner {


  public RunCodeOutput runProgram(List<String> filesToDelete, String fileName,
      String[] inputs) throws Exception {
    String className = fileName.substring(0, fileName.length() - 4);
    String outFile = className + ".out";
    String compileProcess = "timeout 5 g++ --std=c++14 -o " + outFile + " " + fileName;
    filesToDelete.add(outFile);
    String runProcess = "timeout 5 ./" + outFile;

    StringBuilder outputBuilder = new StringBuilder();
    outputBuilder.append(callProcess(compileProcess, new String[]{}));
    outputBuilder.append(callProcess(runProcess, inputs));

    RunCodeOutput output = new RunCodeOutput();
    output.setOutput(outputBuilder.toString());
    return output;
  }
}
