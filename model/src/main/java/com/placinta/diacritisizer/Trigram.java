package com.placinta.diacritisizer;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Hello world!
 */
public class Trigram {

  private final String first;
  private final String second;
  private final String third;


  public Trigram(String first, String second, String third) {
    this.first = first;
    this.second = second;
    this.third = third;
  }

  public boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj);
  }

  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }

}
