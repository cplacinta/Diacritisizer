package com.placinta.diacritisizer.builder;

import com.placinta.diacritisizer.CleanForm;
import com.placinta.diacritisizer.DiacriticsUtils;
import com.placinta.diacritisizer.Word;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WordBuilder {

  private final DiacriticsUtils diacriticsUtils;

  @Autowired
  public WordBuilder(DiacriticsUtils diacriticsUtils) {
    this.diacriticsUtils = diacriticsUtils;
  }

  public Word buildWord(String text) {
    if (StringUtils.isBlank(text)) {
      throw new IllegalArgumentException("Word cannot be empty");
    }

    text = StringUtils.lowerCase(text);
    Word word = new Word(text);
    word.setCleanForm(buildCleanForm(text));

    return word;
  }

  public CleanForm buildCleanForm(String text) {
    if (StringUtils.isBlank(text)) {
      throw new IllegalArgumentException("Clean form cannot be empty");
    }

    String textInLowercase = StringUtils.lowerCase(text);
    return new CleanForm(diacriticsUtils.stripDiacritics(textInLowercase));
  }

}
