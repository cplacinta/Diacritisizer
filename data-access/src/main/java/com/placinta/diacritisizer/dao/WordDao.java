package com.placinta.diacritisizer.dao;

import com.placinta.diacritisizer.Bigram;
import com.placinta.diacritisizer.CleanForm;
import com.placinta.diacritisizer.Trigram;
import com.placinta.diacritisizer.Unigram;
import com.placinta.diacritisizer.Word;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class WordDao {

  @Autowired
  private SessionFactory sessionFactory;

  private Session getCurrentSession() {
    return sessionFactory.getCurrentSession();
  }

  public void saveWordIfAbsent(Word word) {
    Word existingWord = getWordByValue(word.getText());

    if (existingWord != null) {
      return;
    }

    word = checkCleanFormSaved(word);
    getCurrentSession().save(word);
  }

  public Set<Word> getWords(CleanForm cleanForm) {
    CleanForm actualCleanForm = getCleanFormByValue(cleanForm.getText());
    if (actualCleanForm == null) {
      return new HashSet<>();
    }
    return actualCleanForm.getWords();
  }

  public void saveWords(Collection<Word> words) {
    for (Word word : words) {
      saveWordIfAbsent(word);
    }
  }

  public void saveBigrams(Set<Bigram> bigrams) {
    for (Bigram bigram : bigrams) {
      Word firstWord = getWordByValue(bigram.getFirstWord().getText());
      Word secondWord = getWordByValue(bigram.getSecondWord().getText());

      bigram.setFirstWord(firstWord);
      bigram.setSecondWord(secondWord);
      getCurrentSession().saveOrUpdate(bigram);
    }
  }
  public void saveTrigrams(Set<Trigram> trigrams) {
    for (Trigram trigram : trigrams) {
      Word firstWord = getWordByValue(trigram.getFirstWord().getText());
      Word secondWord = getWordByValue(trigram.getSecondWord().getText());
      Word thirdWord = getWordByValue(trigram.getThirdWord().getText());

      trigram.setFirstWord(firstWord);
      trigram.setSecondWord(secondWord);
      trigram.setThirdWord(thirdWord);
      getCurrentSession().saveOrUpdate(trigram);
    }

  }

  public void saveUnigrams(Set<Unigram> unigrams) {
    for (Unigram unigram : unigrams) {
      Word existingWord = getWordByValue(unigram.getWord().getText());

      if (existingWord != null) {
        Unigram existingUnigram = getUnigramById(existingWord.getId());
        if (existingUnigram != null) {
          existingUnigram.addFrequency(unigram.getFrequency());
          unigram = existingUnigram;
        } else {
          unigram.setWord(existingWord);
        }
      }

      saveWordIfAbsent(unigram.getWord());
      getCurrentSession().saveOrUpdate(unigram);
    }
  }

  private Word getWordByValue(String value) {
    Query query = getCurrentSession().createQuery("from Word where text = '" + value + "'");
    return (Word) query.uniqueResult();
  }

  private Word checkCleanFormSaved(Word word) {
    CleanForm cleanForm = getCleanFormByValue(word.getCleanForm().getText());
    if (cleanForm == null) {
      getCurrentSession().save(word.getCleanForm());
    } else {
      word.setCleanForm(cleanForm);
    }

    return word;
  }

  private CleanForm getCleanFormByValue(String value) {
    Query query = getCurrentSession().createQuery("from CleanForm where text = '" + value + "'");
    return (CleanForm) query.uniqueResult();
  }

  private Unigram getUnigramById(long wordId) {
    return (Unigram) getCurrentSession().get(Unigram.class, wordId);
  }

}
