package com.placinta.diacritisizer;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

@Test
public class TrigramTest {

  private Trigram trigram1;
  private Trigram trigram2;
  private Trigram trigram3;

  @BeforeMethod
  public void setUp() {
    Word word1 = new Word("dacă");
    Word word2 = new Word("avem");
    Word word3 = new Word("onoare");
    Word word4 = new Word("bani");

    trigram1 = new Trigram(word1, word2, word3);
    trigram2 = new Trigram(word1, word2, word3);
    trigram3 = new Trigram(word1, word2, word4);
  }

  public void testEquals() {
    assertEquals(trigram1, trigram2);
    assertNotEquals(trigram1, trigram3);
    assertNotEquals(trigram2, trigram3);
  }

  public void testHashCode() {
    assertEquals(trigram1.hashCode(), trigram2.hashCode());
    assertNotEquals(trigram1.hashCode(), trigram3.hashCode());
    assertNotEquals(trigram2.hashCode(), trigram3.hashCode());
  }

}
