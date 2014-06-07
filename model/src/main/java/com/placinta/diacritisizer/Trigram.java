package com.placinta.diacritisizer;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Table(name = "Trigrams")
public class Trigram implements Serializable {

  private static final long serialVersionUID = 20140607L;

  private Word firstWord;
  private Word secondWord;
  private Word thirdWord;

  @SuppressWarnings("unused")
  public Trigram() {
  }

  public Trigram(Word firstWord, Word secondWord, Word thirdWord) {
    this.firstWord = firstWord;
    this.secondWord = secondWord;
    this.thirdWord = thirdWord;
  }

  @Id
  @ManyToOne
  @JoinColumn(name = "first_word_id")
  public Word getFirstWord() {
    return firstWord;
  }

  public void setFirstWord(Word firstWord) {
    this.firstWord = firstWord;
  }

  @Id
  @ManyToOne
  @JoinColumn(name = "second_word_id")
  public Word getSecondWord() {
    return secondWord;
  }

  public void setSecondWord(Word secondWord) {
    this.secondWord = secondWord;
  }

  @Id
  @ManyToOne
  @JoinColumn(name = "third_word_id")
  public Word getThirdWord() {
    return thirdWord;
  }

  public void setThirdWord(Word thirdWord) {
    this.thirdWord = thirdWord;
  }

  @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
  public boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj);
  }

  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }

}
