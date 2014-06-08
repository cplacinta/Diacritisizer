package com.placinta.diacritisizer;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
  private String text;
  private List<Word> words;

  // used by hibernate
  public CleanForm() {
  }

  public CleanForm(String text) {
    this.text = text;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @Column(name = "clean_form", unique = true, nullable = false)
  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  @OneToMany(mappedBy = "cleanForm", fetch = FetchType.EAGER)
  public List<Word> getWords() {
    return words;
  }

  public void setWords(List<Word> words) {
    this.words = words;
  }

}
