package com.placinta.diacritisizer;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

@Test
public class BigramTest {

  private Bigram bigram1;
  private Bigram bigram2;
  private Bigram bigram3;

  @BeforeMethod
  public void setUp() {
    Word word1 = new Word("dacă");
    Word word2 = new Word("avem");
    Word word3 = new Word("mândrie");

    bigram1 = new Bigram(word2, word1);
    bigram2 = new Bigram(word2, word1);
    bigram3 = new Bigram(word3, word2);
  }

  public void testEquals() {
    assertEquals(bigram1, bigram2);
    assertNotEquals(bigram1, bigram3);
    assertNotEquals(bigram2, bigram3);
  }

  public void testHashCode() {
    assertEquals(bigram1.hashCode(), bigram2.hashCode());
    assertNotEquals(bigram1.hashCode(), bigram3.hashCode());
    assertNotEquals(bigram2.hashCode(), bigram3.hashCode());
  }

}
