package com.placinta.diacritisizer;

import java.io.File;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.reporters.Files;

public class Diacritisizer {

  public static void main(String[] args) {
    checkParameters(args);
    String initialTextFile = getInputFilePath(args);
    String text = null;
    if (initialTextFile != null) {
      File inputFile = new File(initialTextFile);
      if (inputFile.exists()) {
        text = readFile(inputFile);
      }
    } else {
      text = args[0];
    }

    DiacriticsRestorer diacriticsRestorer = getDiacriticsRestorer();

    System.out.println("Initial text:");
    System.out.println(text);
    String restoredText = diacriticsRestorer.restore(text);
    System.out.println("Restored text:");
    System.out.println(restoredText);
  }

  private static String readFile(File inputFile) {
    try {
      return Files.readFile(inputFile);
    } catch (IOException e) {
      System.out.println("There was an error reading the file, see the details below: " + e.getMessage());
      System.exit(-1);
    }
    return null;
  }

  private static void checkParameters(String[] args) {
    if (args.length < 1 || args.length > 2) {
      System.out.println("Invalid number of arguments provided");
      printHelpAndExit();
    }
  }

  private static String getInputFilePath(String[] args) {
    if (argumentIsFlag(args[0])) {
      if (args[0].equals("-f")) {
        return args[1];
      }
      printHelpAndExit();
    }
    return null;
  }

  private static void printHelpAndExit() {
    System.out.println("Please provide the text to restore as a string, or use the -f flag to pass the file name");
    System.exit(-1);
  }

  private static boolean argumentIsFlag(String parameter) {
    return StringUtils.startsWith(parameter, "-");
  }

  private static DiacriticsRestorer getDiacriticsRestorer() {
    ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
    return (DiacriticsRestorer) context.getBean("diacriticsRestorer");
  }

}
