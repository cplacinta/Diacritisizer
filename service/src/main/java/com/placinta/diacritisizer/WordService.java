package com.placinta.diacritisizer;

import com.placinta.diacritisizer.dao.WordDao;
import java.util.Collection;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WordService {

  @Autowired
  private WordDao wordDAO;

  public void addWord(Word word) {
    wordDAO.saveWordIfAbsent(word);
  }

  public Set<Word> getWords(CleanForm cleanForm) {
    return wordDAO.getWords(cleanForm);
  }

  public void saveWords(Collection<Word> words) {
    wordDAO.saveWords(words);
  }

  public void saveUnigrams(Set<Unigram> unigrams) {
    wordDAO.saveUnigrams(unigrams);
  }

  public void saveBigrams(Set<Bigram> bigrams) {
    wordDAO.saveBigrams(bigrams);
  }

  public void saveTrigrams(Set<Trigram> trigrams) {
    wordDAO.saveTrigrams(trigrams);
  }

}
