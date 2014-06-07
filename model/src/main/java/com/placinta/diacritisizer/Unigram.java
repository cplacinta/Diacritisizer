package com.placinta.diacritisizer;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "Unigrams" )
@SuppressWarnings("unused")
public class Unigram {

  private long wordId;
  private Word word;
  private int frequency;

  public Unigram() {
  }

  public Unigram(Word word) {
    this.word = word;
  }

  @Id
  @Column(name = "word_id", unique = true, nullable = false)
  @GeneratedValue(generator = "keyGenerator")
  @GenericGenerator(name = "keyGenerator", strategy = "foreign",
    parameters = @Parameter(name = "property", value = "word"))
  public long getWordId() {
    return wordId;
  }

  public void setWordId(long wordId) {
    this.wordId = wordId;
  }

  @OneToOne(cascade = CascadeType.ALL)
  @PrimaryKeyJoinColumn
  public Word getWord() {
    return word;
  }

  public void setWord(Word word) {
    this.word = word;
  }

  @Column(name = "frequency", unique = true, nullable = false)
  public int getFrequency() {
    return frequency;
  }

  public void setFrequency(int frequency) {
    this.frequency = frequency;
  }

  public void addFrequency(int frequency) {
    this.frequency += frequency;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }

    if (other == null || getClass() != other.getClass()) {
      return false;
    }

    return word.equals(((Unigram) other).word);
  }

  @Override
  public int hashCode() {
    return word.hashCode();
  }

}
