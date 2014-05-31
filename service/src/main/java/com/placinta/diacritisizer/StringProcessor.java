package com.placinta.diacritisizer;

import org.apache.commons.lang3.StringUtils;

public class StringProcessor {

  public String[] toPhrases(String input) {
    String[] phrases = StringUtils.split(input, ".?!");
    for (int i = 0; i < phrases.length; i++) {
      phrases[i] = phrases[i].trim();
    }

    return phrases;
  }

  public String[] toWords(String input) {
    return StringUtils.split(input, " ,;:\"\'");
  }
}
