package com.placinta.diacritisizer;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DiacriticsRestorator {

  private static final String PHRASE_ONE = "Mama paine alba coace.";
  private static final String PHRASE_TWO = "Aceeasi fiinta blanda.";

  public static void main(String[] args) {
    ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");

    DiacriticsRestorer diacriticsRestorer = getBean(context, "diacriticsRestorer");

    String input = PHRASE_ONE + " " + PHRASE_TWO;
    System.out.println("Initial text:");
    System.out.println(input);
    String restoredText = diacriticsRestorer.restore(input);
    System.out.println(restoredText);
  }

  @SuppressWarnings("unchecked")
  private static <T> T getBean(ApplicationContext context, String beanName) {
    return (T) context.getBean(beanName);
  }

}
