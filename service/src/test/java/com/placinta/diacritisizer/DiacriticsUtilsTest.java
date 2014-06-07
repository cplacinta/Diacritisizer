package com.placinta.diacritisizer;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

@Test
public class DiacriticsUtilsTest {

  private DiacriticsUtils diacriticsUtils;

  @BeforeMethod
  public void setUp() {
    diacriticsUtils = new DiacriticsUtils();
  }

  public void testStripDiacritics() {
    String wordWithoutDiacritics = diacriticsUtils.stripDiacritics("Științifică");

    assertEquals(wordWithoutDiacritics, "stiintifica");
  }

  public void testWordContainsDiacritics() {
    boolean containsDiacritics = diacriticsUtils.containsDiacritics("Științifică");

    assertTrue(containsDiacritics);
  }

  public void testWordDoesNotContainDiacritics() {
    boolean containsDiacritics = diacriticsUtils.containsDiacritics("Magazin");

    assertFalse(containsDiacritics);
  }

}
