package com.placinta.diacritisizer;

import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Test
public class BigramTest {

  public void testEquals() {
    Bigram bigram1 = new Bigram("dacă", "avem");
    Bigram bigram2 = new Bigram("dacă", "avem");

    assertTrue(bigram1.equals(bigram2));
  }

  public void testHashCode() {
    Bigram bigram1 = new Bigram("dacă", "avem");
    Bigram bigram2 = new Bigram("dacă", "avem");

    assertEquals(bigram1.hashCode(), bigram2.hashCode());
  }

}
