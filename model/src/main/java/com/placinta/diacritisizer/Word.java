package com.placinta.diacritisizer;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import static javax.persistence.GenerationType.IDENTITY;

@Table(name = "Dictionary")
public class Word {

  private long id;
  private String word;

  @Id @GeneratedValue(strategy = IDENTITY)
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @Column(name = "word")
  public String getWord() {
    return word;
  }

  public void setWord(String word) {
    this.word = word;
  }

}
