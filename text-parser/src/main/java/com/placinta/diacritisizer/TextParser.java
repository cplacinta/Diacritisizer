package com.placinta.diacritisizer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TextParser {

  public static void main(String[] args) {
    ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");

    WordService service = getBean(context, "wordService");
    TextProcessor processor = getBean(context, "textProcessor");

    verifyParameterPresence(args);

    Path directoryPath = verifyDirectoryLocation(args[0]);

    ArrayList<String> textFiles = getFilesList(directoryPath);
    long currentTime = System.currentTimeMillis();
    int i = 0;

    try {
      for (String filename : textFiles) {
        List<String> lines = getLines(directoryPath, filename);
        for (String line : lines) {
          processLine(line, processor, service);
          i = printProgress(i, currentTime);
        }
      }

    } catch (IOException e) {
      System.out.println("There was an error processing the text files");
      e.printStackTrace();
    }

    System.out.println("Finished processing, elapsed time: " + formatMillis(System.currentTimeMillis() - currentTime));
  }

  @SuppressWarnings("unchecked")
  private static <T> T getBean(ApplicationContext context, String beanName) {
    return (T) context.getBean(beanName);
  }

  private static List<String> getLines(Path directoryPath, String filename) throws IOException {
    System.out.println("Processing: " + filename);
    return Files.readAllLines(Paths.get(directoryPath + "/" + filename), Charset.defaultCharset());
  }

  private static void processLine(String line, TextProcessor processor, WordService service) {
    TextProcessingResult result = processor.processText(line);
    service.saveTextProcessingResult(result);
  }

  private static int printProgress(int i, long currentTime) {
    if (++i % 100 == 0) {
      System.out.println("Lines processed thus far: " + i);
      System.out.println("Time elapsed: " + formatMillis(System.currentTimeMillis() - currentTime));
    }
    return i;
  }

  private static void verifyParameterPresence(String[] args) {
    if (args.length == 0) {
      System.out.println("You need to specify the directory containing the texts");
      System.exit(0);
    }
  }

  private static Path verifyDirectoryLocation(String directoryLocation) {
    Path directoryPath = Paths.get(directoryLocation);
    if (!Files.exists(directoryPath) || !Files.isDirectory(directoryPath)) {
      System.out.println("Directory not found at location: " + directoryLocation);
      System.exit(0);
    }
    return directoryPath;
  }

  private static ArrayList<String> getFilesList(Path directoryPath) {
    return new ArrayList<>(Arrays.asList(new File(directoryPath.toUri()).list()));
  }

  private static String formatMillis(long value) {
    StringBuilder builder = new StringBuilder(20);
    String sign = "";

    if (value < 0) {
      sign = "-";
      value = Math.abs(value);
    }

    append(builder, sign, 0, (value / 3600000));
    append(builder, ":", 2, ((value % 3600000) / 60000));
    append(builder, ":", 2, ((value % 60000) / 1000));
    append(builder, ".", 3, (value % 1000));
    return builder.toString();
  }

  private static void append(StringBuilder target, String prefix, int digit, long value) {
    target.append(prefix);
    if (digit > 1) {
      int pad = (digit - 1);
      for (long xa = value; xa > 9 && pad > 0; xa /= 10) {
        pad--;
      }
      for (int xa = 0; xa < pad; xa++) {
        target.append('0');
      }
    }
    target.append(value);
  }

}
