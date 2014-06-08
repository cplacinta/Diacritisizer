package com.placinta.diacritisizer;

import com.placinta.diacritisizer.dao.WordDao;
import java.util.Set;
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

  public void addWord(Word word) {
    wordDAO.saveWordIfAbsent(word);
  }

  public Set<Word> getWords(CleanForm cleanForm) {
    return wordDAO.getWords(cleanForm);
  }

}
