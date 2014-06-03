package com.placinta.diacritisizer;

import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Test
public class TrigramTest {

  public void testEquals() {
    Trigram trigram1 = new Trigram("dacă", "avem", "onoare");
    Trigram trigram2 = new Trigram("dacă", "avem", "onoare");

    assertTrue(trigram1.equals(trigram2));
  }

  public void testHashCode() {
    Trigram trigram1 = new Trigram("dacă", "avem", "onoare");
    Trigram trigram2 = new Trigram("dacă", "avem", "onoare");

    assertEquals(trigram1.hashCode(), trigram2.hashCode());
  }

}
