package com.placinta.diacritisizer.builder;

import com.placinta.diacritisizer.CleanForm;
import com.placinta.diacritisizer.DiacriticsUtils;
import com.placinta.diacritisizer.Word;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WordFactory {

  private final DiacriticsUtils diacriticsUtils;

  @Autowired
  public WordFactory(DiacriticsUtils diacriticsUtils) {
    this.diacriticsUtils = diacriticsUtils;
  }

  public Word createWord(String text) {
    if (StringUtils.isBlank(text)) {
      throw new IllegalArgumentException("Word cannot be empty");
    }

    text = StringUtils.lowerCase(text);
    Word word = new Word(text);
    word.setCleanForm(new CleanForm(diacriticsUtils.stripDiacritics(text)));

    return word;
  }

}
