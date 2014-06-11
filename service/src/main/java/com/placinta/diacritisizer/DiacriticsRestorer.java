package com.placinta.diacritisizer;

import com.placinta.diacritisizer.builder.WordFactory;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
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
    String[] phrases = processor.toPhrases(input);

    int phraseStartIndex;
    int startIndex;
    int endIndex;

    for (String phrase : phrases) {
      phraseStartIndex = input.indexOf(phrase);
      LinkedList<String> words = new LinkedList<>(processor.toWords(phrase));
      String previousWord;
      String nextWord;
      ListIterator<String> iterator = words.listIterator();
      while (iterator.hasNext()) {
        boolean replacementFound = false;
        if (iterator.hasPrevious()) {
          previousWord = iterator.previous();
          iterator.next();
        } else {
          previousWord = null;
        }
        String currentWord = iterator.next();
        startIndex = phraseStartIndex + phrase.indexOf(currentWord);
        endIndex = startIndex + currentWord.length();
        if (iterator.hasNext()) {
          nextWord = iterator.next();
          iterator.previous();
        } else {
          nextWord = null;
        }

        CleanForm cleanForm = wordFactory.createCleanForm(currentWord);
        List<Word> currentWordPossibleForms = wordService.getWords(cleanForm);

        if (currentWordPossibleForms.size() == 1) {
          input = replaceWord(input, currentWord, currentWordPossibleForms.get(0), startIndex, endIndex);
          replacementFound = true;
        } else if (previousWord != null || nextWord != null) {
          List<Word> previousWordPossibleForms = new ArrayList<>();
          if (previousWord != null) {
            CleanForm previousWordCleanForm = wordFactory.createCleanForm(previousWord);
            previousWordPossibleForms = wordService.getWords(previousWordCleanForm);
          }
          List<Word> nextWordCleanForms = new ArrayList<>();
          if (nextWord != null) {
            CleanForm nextWordCleanForm = wordFactory.createCleanForm(nextWord);
            nextWordCleanForms = wordService.getWords(nextWordCleanForm);
          }

          List<Bigram> bigrams = new ArrayList<>();

          for (Word firstWord : previousWordPossibleForms) {
            for (Word secondWord : currentWordPossibleForms) {
              Bigram bigram = new Bigram(firstWord, secondWord);
              bigrams.add(bigram);
            }
          }

          for (Word firstWord : currentWordPossibleForms) {
            for (Word secondWord : nextWordCleanForms) {
              Bigram bigram = new Bigram(firstWord, secondWord);
              bigrams.add(bigram);
            }
          }

          List<Bigram> filteredBigrams = wordService.filterExistingBigrams(bigrams);
          if (filteredBigrams.size() > 0) {
            bigrams = filteredBigrams;
          }
          if (filteredBigrams.size() == 1) {
            Word replacementWord =
              wordService.getCorrectWordFromBigram(bigrams.get(0), wordFactory.createCleanForm(currentWord));
            input = replaceWord(input, currentWord, replacementWord, startIndex, endIndex);
            replacementFound = true;
          } else {
            List<Trigram> trigrams = wordService.getTrigramsContainingBigrams(bigrams, currentWord.toLowerCase());
            if (trigrams.size() == 1) {
              Word replacementWord = trigrams.get(0).getSecondWord();
              input = replaceWord(input, currentWord, replacementWord, startIndex, endIndex);
              replacementFound = true;
            } else if (trigrams.size() > 1) {
              Unigram highestFrequencyUnigram = wordService.getHighestFrequencyUnigram(trigrams);
              if (highestFrequencyUnigram != null) {
                input = replaceWord(input, currentWord, highestFrequencyUnigram.getWord(), startIndex, endIndex);
                replacementFound = true;
              }
            }
          }
        }
        if (!replacementFound) {
          Unigram highestFrequencyUnigram = wordService.getHighestFrequencyUnigramByCleanForm(cleanForm.getText());
          if (highestFrequencyUnigram != null) {
            input = replaceWord(input, currentWord, highestFrequencyUnigram.getWord(), startIndex, endIndex);
          }
        }
      }
    }

    return input;
  }

  private String replaceWord(String input, String currentWord, Word correctWord, int startIndex, int endIndex) {
    String replacement = getReplacementWord(currentWord, correctWord);
    return input.substring(0, startIndex) + replacement + input.substring(endIndex);
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
