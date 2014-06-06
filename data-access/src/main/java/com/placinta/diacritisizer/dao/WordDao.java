package com.placinta.diacritisizer.dao;

import com.placinta.diacritisizer.CleanForm;
import com.placinta.diacritisizer.Word;
import java.util.List;
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

  public void addWordIfAbsent(Word word) {
    Word existingWord = getWordByValue(word.getWord());

    if (existingWord != null) {
      return;
    }

    word = checkCleanFormSaved(word);
    getCurrentSession().save(word);
  }

  @SuppressWarnings("unchecked")
  public List<Word> getWords(CleanForm cleanForm) {
    CleanForm actualCleanForm = getCleanFormByValue(cleanForm.getWord());
    return getCurrentSession().createQuery("from Word where clean_id = " + actualCleanForm.getId()).list();
  }

  public Word getWordByValue(String value) {
    Query query = getCurrentSession().createQuery("from Word where word = '" + value + "'");
    return (Word) query.uniqueResult();
  }

  private Word checkCleanFormSaved(Word word) {
    CleanForm cleanForm = getCleanFormByValue(word.getCleanForm().getWord());
    if (cleanForm == null) {
      getCurrentSession().save(word.getCleanForm());
    } else {
      word.setCleanForm(cleanForm);
    }

    return word;
  }

  private CleanForm getCleanFormByValue(String value) {
    Query query = getCurrentSession().createQuery("from CleanForm where word = '" + value + "'");
    return (CleanForm) query.uniqueResult();
  }

}
