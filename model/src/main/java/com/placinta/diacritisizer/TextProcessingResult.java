package com.placinta.diacritisizer;

import java.util.Collection;
import java.util.Set;

public class TextProcessingResult {
  private final Collection<Word> words;
  private final Set<Unigram> unigrams;
  private final Set<Bigram> bigrams;
  private final Set<Trigram> trigrams;

  public TextProcessingResult(Collection<Word> words, Set<Unigram> unigrams, Set<Bigram> bigrams,
    Set<Trigram> trigrams) {
    this.words = words;
    this.unigrams = unigrams;
    this.bigrams = bigrams;
    this.trigrams = trigrams;
  }

  public Collection<Word> getWords() {
    return words;
  }

  public Set<Unigram> getUnigrams() {
    return unigrams;
  }

  public Set<Bigram> getBigrams() {
    return bigrams;
  }

  public Set<Trigram> getTrigrams() {
    return trigrams;
  }

}
