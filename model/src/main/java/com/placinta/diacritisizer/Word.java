package com.placinta.diacritisizer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "Dictionary", uniqueConstraints = @UniqueConstraint(columnNames = "word"))
@SuppressWarnings("unused")
public class Word {

  private long id;
  private String word;
  private CleanForm cleanForm;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @Column(name = "word", unique = true, nullable = false)
  public String getWord() {
    return word;
  }

  public void setWord(String word) {
    this.word = word;
  }

  @ManyToOne
  @JoinColumn(name = "clean_id", nullable = false)
  public CleanForm getCleanForm() {
    return cleanForm;
  }

  public void setCleanForm(CleanForm cleanForm) {
    this.cleanForm = cleanForm;
  }

}
