package com.placinta.diacritisizer;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "FlatDictionary", uniqueConstraints = @UniqueConstraint(columnNames = "clean_form"))
@SuppressWarnings("unused")
public class CleanForm {

  private long id;
  private String word;
  private Set<Word> words;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @Column(name = "clean_form", unique = true, nullable = false)
  public String getWord() {
    return word;
  }

  public void setWord(String word) {
    this.word = word;
  }

  @OneToMany(mappedBy = "word")
  public Set<Word> getWords() {
    return words;
  }

  public void setWords(Set<Word> words) {
    this.words = words;
  }

}
