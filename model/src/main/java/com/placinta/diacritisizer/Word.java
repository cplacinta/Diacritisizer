package com.placinta.diacritisizer;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.apache.commons.lang3.builder.EqualsBuilder;

@Entity
@Table(name = "Dictionary", uniqueConstraints = @UniqueConstraint(columnNames = "word"))
@SuppressWarnings("unused")
public class Word implements Serializable {

  private static final long serialVersionUID = 20140607L;

  private long id;
  private String text;
  private CleanForm cleanForm;

  // required for hibernate
  public Word() {
  }

  public Word(String text) {
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

  @Column(name = "word", unique = true, nullable = false)
  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  @ManyToOne
  @JoinColumn(name = "clean_id", nullable = false)
  public CleanForm getCleanForm() {
    return cleanForm;
  }

  public void setCleanForm(CleanForm cleanForm) {
    this.cleanForm = cleanForm;
  }

  @Override
  @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
  public boolean equals(Object other) {
    return EqualsBuilder.reflectionEquals(this, other, "id", "cleanForm");
  }

  @Override
  public int hashCode() {
    return getText() != null ? getText().toLowerCase().hashCode() : 0;
  }

  @Override
  public String toString() {
    return getText();
  }

}
