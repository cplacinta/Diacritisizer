package com.placinta.diacritisizer;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.placinta.diacritisizer.builder.WordFactory;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TextProcessor {

  private static final String PHRASE_SEPARATORS = ".?!";
  private static final String WORD_SEPARATORS = " ,;:\"\'";

  private final DiacriticsUtils diacriticsUtils;
  private final WordFactory wordFactory;

  @Autowired
  public TextProcessor(DiacriticsUtils diacriticsUtils, WordFactory wordFactory) {
    this.diacriticsUtils = diacriticsUtils;
    this.wordFactory = wordFactory;
  }

  public TextProcessingResult processText(String textCorpora) {

    String[] phrases = toPhrases(textCorpora);

    Map<String, Word> wordsMap = new HashMap<>();
    Multiset<Unigram> unigramMultiset = HashMultiset.create();
    Set<Bigram> bigrams = new HashSet<>();
    Set<Trigram> trigrams = new HashSet<>();

    for (String phrase : phrases) {
      String[] words = toWords(phrase);

      wordsMap.putAll(getUniqueWordsMap(words));
      unigramMultiset.addAll(buildUnigramsSet(words, wordsMap));
      bigrams.addAll(buildBigramsSet(words));
      trigrams.addAll(buildTrigramsSet(words));
    }

    Set<Unigram> unigrams = transformToUnigramsSet(unigramMultiset);

    return new TextProcessingResult(wordsMap.values(), unigrams, bigrams, trigrams);
  }

  private Map<String, Word> getUniqueWordsMap(String[] input) {
    Map<String, Word> words = new HashMap<>();
    for (String string : input) {
      String text = string.toLowerCase();
      if (!words.containsKey(text)) {
        Word word = wordFactory.createWord(text);
        words.put(word.getText(), word);
      }
    }
    return words;
  }

  private Multiset<Unigram> buildUnigramsSet(String[] words, Map<String, Word> wordsMap) {
    Multiset<Unigram> unigramMultiset = HashMultiset.create();

    for (String word : words) {
      if (diacriticsUtils.containsDiacritics(word)) {
        unigramMultiset.add(new Unigram(wordsMap.get(word.toLowerCase())));
      }
    }
    return unigramMultiset;
  }

  private Collection<? extends Bigram> buildBigramsSet(String[] words) {
    Set<Bigram> bigrams = new HashSet<>();

    for (int i = 0; i < words.length; i++) {
      if (diacriticsUtils.containsDiacritics(words[i])) {
        Word commonWord = wordFactory.createWord(words[i]);
        if (i > 0) {
          Word firstWord = wordFactory.createWord(words[i - 1]);
          bigrams.add(new Bigram(firstWord, commonWord));
        }
        if (words.length - i > 1) {
          Word secondWord = wordFactory.createWord(words[i + 1]);
          bigrams.add(new Bigram(commonWord, secondWord));
        }
      }
    }
    return bigrams;
  }

  private Collection<? extends Trigram> buildTrigramsSet(String[] words) {
    Set<Trigram> trigrams = new HashSet<>();

    for (int i = 0; i < words.length; i++) {
      if (diacriticsUtils.containsDiacritics(words[i])) {
        if (i > 1 && i < words.length - 1) {
          Word firstWord = wordFactory.createWord(words[i - 1]);
          Word secondWord = wordFactory.createWord(words[i]);
          Word thirdWord = wordFactory.createWord(words[i + 1]);

          trigrams.add(new Trigram(firstWord, secondWord, thirdWord));
        }
      }
    }
    return trigrams;

  }

  private Set<Unigram> transformToUnigramsSet(Multiset<Unigram> unigramMultiset) {
    Set<Unigram> unigrams = new HashSet<>();
    for (Multiset.Entry<Unigram> entity : unigramMultiset.entrySet()) {
      Unigram unigram = entity.getElement();
      unigram.setFrequency(entity.getCount());
      unigrams.add(unigram);
    }
    return unigrams;
  }

  public String[] toPhrases(String input) {
    String[] phrases = StringUtils.split(input, PHRASE_SEPARATORS);
    for (int i = 0; i < phrases.length; i++) {
      phrases[i] = phrases[i].trim();
    }

    return phrases;
  }

  public String[] toWords(String input) {
    return StringUtils.split(input, WORD_SEPARATORS);
  }

}
