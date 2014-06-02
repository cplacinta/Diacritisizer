package com.placinta.diacritisizer;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Hello world!
 */
public class Bigram {

  private final String first;
  private final String second;


  public Bigram(String first, String second) {
    this.first = first;
    this.second = second;
  }

  public boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj);
  }

  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }

}
