package com.placinta.diacritisizer;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import java.text.Normalizer;
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

  public String stripDiacritics(String word) {
    word = Normalizer.normalize(word, Normalizer.Form.NFD);
    return word.replaceAll("[^\\p{ASCII}]", "");
  }

  public Multiset<String> buildUnigramsSet(String textCorpora) {
    Multiset<String> unigrams = HashMultiset.create();
    String[] phrases = toPhrases(textCorpora);

    for (String phrase : phrases) {
      String[] words = toWords(phrase);

      for (String word : words) {
        if (containsDiacritics(word)) {
          unigrams.add(word.toLowerCase());
        }
      }
    }
    return unigrams;
  }

  public Multiset<Bigram> buildBigramsSet(String textCorpora) {
    Multiset<Bigram> bigrams = HashMultiset.create();
    String[] phrases = toPhrases(textCorpora);

    for (String phrase : phrases) {
      String[] words = toWords(phrase);

      for (int i = 0; i < words.length; i++) {
        if (containsDiacritics(words[i])) {
          if (i > 0) {
            bigrams.add(new Bigram(words[i - 1].toLowerCase(), words[i].toLowerCase()));
          }
          if (words.length - i > 1) {
            bigrams.add(new Bigram(words[i].toLowerCase(), words[i + 1].toLowerCase()));
          }
        }
      }
    }
    return bigrams;
  }

  public boolean containsDiacritics(String word) {
    if (StringUtils.isBlank(word)) {
      return false;
    }

    return !stripDiacritics(word).equals(word);
  }

}
