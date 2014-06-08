package com.placinta.diacritisizer;

import com.placinta.diacritisizer.builder.WordFactory;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiacriticsRestorer {
  private final TextProcessor processor;
  private final WordFactory wordFactory;
  private final WordService wordService;

  @Autowired
  public DiacriticsRestorer(TextProcessor processor, WordFactory wordFactory, WordService wordService) {
    this.processor = processor;
    this.wordFactory = wordFactory;
    this.wordService = wordService;
  }

  public String restore(String input) {
    return filterFirstPhase(input);
  }

  private String filterFirstPhase(String input) {
    String[] phrases = processor.toPhrases(input);

    for (String phrase : phrases) {
      List<String> words = processor.toWords(phrase);
      for (String text : words) {
        CleanForm cleanForm = wordFactory.createCleanForm(text);
        List<Word> wordsFromCleanForm = wordService.getWords(cleanForm);

        if (wordsFromCleanForm.size() == 1) {
          String replacement = getReplacementWord(text, wordsFromCleanForm.get(0));
          input = StringUtils.replace(input, text, replacement);
        }
      }
    }

    return input;
  }

  private String getReplacementWord(String text, Word replacementWord) {
    boolean isCapitalized = isCapitalWord(text);
    String lowercaseReplacement = replacementWord.getText();
    return isCapitalized ? StringUtils.capitalize(lowercaseReplacement) : lowercaseReplacement;
  }

  private boolean isCapitalWord(String text) {
    return StringUtils.capitalize(text).equals(text);
  }

}
