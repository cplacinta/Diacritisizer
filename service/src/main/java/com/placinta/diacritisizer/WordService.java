package com.placinta.diacritisizer;

import com.placinta.diacritisizer.dao.WordDao;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WordService {

  @Autowired
  private WordDao wordDAO;

  public void saveTextProcessingResult(TextProcessingResult result) {
    wordDAO.saveWords(result.getWords());
    wordDAO.saveUnigrams(result.getUnigrams());
    wordDAO.saveBigrams(result.getBigrams());
    wordDAO.saveTrigrams(result.getTrigrams());
  }

  public List<Word> getWords(CleanForm cleanForm) {
    return wordDAO.getWords(cleanForm);
  }

  public List<Bigram> filterExistingBigrams(List<Bigram> bigrams) {
    List<Bigram> filteredBigrams = new ArrayList<>(bigrams.size());
    for (Bigram bigram : bigrams) {
      Bigram existingBigram = wordDAO.getBigram(bigram);
      if (existingBigram != null) {
        filteredBigrams.add(existingBigram);
      }
    }
    return filteredBigrams;
  }

  public Word getCorrectWordFromBigram(Bigram bigram, CleanForm cleanForm) {
    if (bigram.getFirstWord().getCleanForm().equals(cleanForm)) {
      return bigram.getFirstWord();
    } else if (bigram.getSecondWord().getCleanForm().equals(cleanForm)) {
      return bigram.getSecondWord();
    }

    throw new IllegalArgumentException(String.format("Invalid bigram (%s) or clean form (%s) provided",
      bigram.toString(), cleanForm.toString()));
  }

  public List<Trigram> getTrigramsContainingBigrams(List<Bigram> bigrams, String currentWord) {
    StringBuilder firstWords = new StringBuilder();
    StringBuilder secondWords = new StringBuilder();
    StringBuilder thirdWords = new StringBuilder();

    for (Bigram bigram : bigrams) {
      if (bigram.getSecondWord().getCleanForm().getText().equals(currentWord)) {
        appendWord(firstWords, bigram.getFirstWord().getText());
        appendWord(secondWords, bigram.getSecondWord().getText());
      } else {
        appendWord(secondWords, bigram.getFirstWord().getText());
        appendWord(thirdWords, bigram.getSecondWord().getText());
      }
    }
    removeLastSeparator(firstWords);
    removeLastSeparator(secondWords);
    removeLastSeparator(thirdWords);

    return wordDAO.getPossibleTrigrams(firstWords.toString(), secondWords.toString(), thirdWords.toString());
  }

  public Unigram getHighestFrequencyUnigram(List<Trigram> trigrams) {
    StringBuilder possibleWords = new StringBuilder();
    for (Trigram trigram : trigrams) {
      appendWord(possibleWords, trigram.getSecondWord().getText());
    }
    removeLastSeparator(possibleWords);
    return wordDAO.getHighestFrequencyUnigram(possibleWords.toString());
  }

  public Unigram getHighestFrequencyUnigramByCleanForm(String text) {
    return wordDAO.getHighestFrequencyUnigramByCleanForm("'" + text + "'");
  }

  private void appendWord(StringBuilder stringBuilder, String word) {
    stringBuilder
      .append("'")
      .append(word)
      .append("',");
  }

  private void removeLastSeparator(StringBuilder builder) {
    int lastCharIndex = builder.length() - 1;
    if (lastCharIndex > 0 && builder.lastIndexOf(",") == lastCharIndex) {
      builder.delete(lastCharIndex, builder.length());
    }
  }
}
