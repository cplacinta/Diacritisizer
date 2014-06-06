package com.placinta.diacritisizer;

import com.placinta.diacritisizer.dao.WordDao;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WordService {

  @Autowired
  private WordDao wordDAO;

  public void addWord(Word word) {
    wordDAO.addWordIfAbsent(word);
  }

  public List<Word> getWords(CleanForm cleanForm) {
    return wordDAO.getWords(cleanForm);
  }

}
