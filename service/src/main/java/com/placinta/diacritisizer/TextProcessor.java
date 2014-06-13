package com.placinta.diacritisizer;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.placinta.diacritisizer.builder.WordBuilder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TextProcessor {

  private static final String PHRASE_SEPARATORS = "—.?!";
  private static final String WORD_SEPARATORS = " ,;:–\"\'„”“()[]{}";

  private final DiacriticsUtils diacriticsUtils;
  private final WordBuilder wordBuilder;

  @Autowired
  public TextProcessor(DiacriticsUtils diacriticsUtils, WordBuilder wordBuilder) {
    this.diacriticsUtils = diacriticsUtils;
    this.wordBuilder = wordBuilder;
  }

  public TextProcessingResult processText(String textCorpora) {

    String[] phrases = toPhrases(textCorpora);

    Map<String, Word> wordsMap = new HashMap<>();
    Multiset<Unigram> unigramMultiset = HashMultiset.create();
    Set<Bigram> bigrams = new HashSet<>();
    Set<Trigram> trigrams = new HashSet<>();

    for (String phrase : phrases) {
      List<String> words = toWords(phrase);

      wordsMap.putAll(getUniqueWordsMap(words));
      unigramMultiset.addAll(buildUnigramsSet(words, wordsMap));
      bigrams.addAll(buildBigramsSet(words));
      trigrams.addAll(buildTrigramsSet(words));
    }

    Set<Unigram> unigrams = transformToUnigramsSet(unigramMultiset);

    return new TextProcessingResult(wordsMap.values(), unigrams, bigrams, trigrams);
  }

  private Map<String, Word> getUniqueWordsMap(List<String> input) {
    Map<String, Word> words = new HashMap<>();
    for (String string : input) {
      String text = string.toLowerCase();
      if (!words.containsKey(text)) {
        Word word = wordBuilder.buildWord(text);
        words.put(word.getText(), word);
      }
    }
    return words;
  }

  private Multiset<Unigram> buildUnigramsSet(List<String> words, Map<String, Word> wordsMap) {
    Multiset<Unigram> unigramMultiset = HashMultiset.create();

    for (String word : words) {
      if (diacriticsUtils.containsDiacritics(word)) {
        unigramMultiset.add(new Unigram(wordsMap.get(word.toLowerCase())));
      }
    }
    return unigramMultiset;
  }

  private Collection<? extends Bigram> buildBigramsSet(List<String> words) {
    Set<Bigram> bigrams = new HashSet<>();

    int i = 0;
    for (String word : words) {
      if (diacriticsUtils.containsDiacritics(word)) {
        Word commonWord = wordBuilder.buildWord(word);
        if (i > 0) {
          Word firstWord = wordBuilder.buildWord(words.get(i - 1));
          bigrams.add(new Bigram(firstWord, commonWord));
        }
        if (words.size() - i > 1) {
          Word secondWord = wordBuilder.buildWord(words.get(i + 1));
          bigrams.add(new Bigram(commonWord, secondWord));
        }
      }
      i++;
    }
    return bigrams;
  }

  private Collection<? extends Trigram> buildTrigramsSet(List<String> words) {
    Set<Trigram> trigrams = new HashSet<>();

    int i = 0;
    for (String word : words) {
      if (diacriticsUtils.containsDiacritics(word)) {
        if (i > 1 && i < words.size() - 1) {
          Word firstWord = wordBuilder.buildWord(words.get(i - 1));
          Word secondWord = wordBuilder.buildWord(word);
          Word thirdWord = wordBuilder.buildWord(words.get(i + 1));

          trigrams.add(new Trigram(firstWord, secondWord, thirdWord));
        }
      }
      i++;
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

  public List<String> toWords(String input) {
    String[] words = StringUtils.split(input, WORD_SEPARATORS);
    List<String> filteredWords = new ArrayList<>(words.length);

    for (String word : words) {
      word = StringUtils.trim(word);
      // ignoring all the words that contain special characters, except for "-" and "'"
      if (StringUtils.isAlpha(word) || word.length() > 1 && StringUtils.containsAny(word, "-’")) {
        filteredWords.add(word);
      }
    }

    return filteredWords;
  }

}
