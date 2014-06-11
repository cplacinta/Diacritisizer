package com.placinta.diacritisizer;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Diacritisizer {

  private static final String PHRASE_ONE = "Mama paine alba coace.";
  private static final String PHRASE_TWO =
    "In fiecare clipa, transformarile ne cuprind, sunt varste ale cuvantului care, se stie, la inceput a fost. Tablele de valori, construite pe scarile timpului, suporta in permanenta prefaceri, iar jocul profund al relativitatii desface urmele, inoculand teama pistelor false.";

  public static void main(String[] args) {
    ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");

    DiacriticsRestorer diacriticsRestorer = getBean(context, "diacriticsRestorer");

    String input = PHRASE_ONE + " " + PHRASE_TWO;
    System.out.println("Initial text:");
    System.out.println(input);
    String restoredText = diacriticsRestorer.restore(input);
    System.out.println("Restored text:");
    System.out.println(restoredText);
  }

  @SuppressWarnings("unchecked")
  private static <T> T getBean(ApplicationContext context, String beanName) {
    return (T) context.getBean(beanName);
  }

}
