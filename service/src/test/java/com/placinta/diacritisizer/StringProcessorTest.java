package com.placinta.diacritisizer;

import com.google.common.collect.Multiset;
import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

@Test
public class StringProcessorTest {

  private static final String PHRASE_ONE = "Mama paine alba coace.";
  private static final String PHRASE_TWO = "Noi cantam voios.";
  private static final String[] PHRASES = new String[]{PHRASE_ONE, PHRASE_TWO};
  private static final String[] WORDS = new String[]{"Mama", "paine", "alba", "coace"};
  private static final String UNIGRAMS_TEXT_CORPORA = "Educația de calitate este indispensabilă pentru dezvoltarea și " +
    "valorificarea potențialului fiecărui copil și, în cele din urmă, pentru prosperarea țării în care trăim. " +
    "Sesiunea de bacalaureat 2014, care începe astăzi, constituie un element important în procesul de îmbunătățire " +
    "a calității educației. O evaluare corectă și transparentă, care asigură că fiecare elev obține rezultate " +
    "conform meritelor, le oferă tinerilor șanse egale de a-și alege și construi o carieră profesională și " +
    "generează schimbări semnificative de atitudine în întreg procesul educațional.";
  private static final String NGRAMS_TEXT_CORPORA = "Treceți batalioane române Carpații. La arme cu frunze și flori!" +
    " Treceți batalioane române Carpații. La arme cu frunze și flori!";

  public void testSplitTextToPhrases() {
    StringProcessor processor = new StringProcessor();

    String[] phrases = processor.toPhrases(StringUtils.join(PHRASES, StringUtils.SPACE));

    assertEquals(phrases.length, 2);
    assertTrue(PHRASE_ONE.startsWith(phrases[0]));
    assertTrue(PHRASE_TWO.startsWith(phrases[1]));
  }

  public void testSplitPhraseInWords() {
    StringProcessor processor = new StringProcessor();

    String[] words = processor.toWords(PHRASE_ONE.replace(".", ""));

    assertEquals(words.length, 4);
    for (int i = 0; i < words.length; i++) {
      assertEquals(words[i], WORDS[i]);
    }
  }

  public void testStripDiacritics() {
    StringProcessor processor = new StringProcessor();

    String wordWithoutDiacritics = processor.stripDiacritics("Științifică");

    assertEquals(wordWithoutDiacritics, "Stiintifica");
  }

  public void testWordContainsDiacritics() {
    StringProcessor processor = new StringProcessor();

    boolean containsDiacritics = processor.containsDiacritics("Științifică");

    assertTrue(containsDiacritics);
  }


  public void testWordDoesNotContainDiacritics() {
    StringProcessor processor = new StringProcessor();

    boolean containsDiacritics = processor.containsDiacritics("Magazin");

    assertFalse(containsDiacritics);
  }

  public void testUnigramsIdentification() {
    StringProcessor processor = new StringProcessor();

    Multiset<String> unigrams = processor.buildUnigramsSet(UNIGRAMS_TEXT_CORPORA);

    assertEquals(unigrams.size(), 35);
    assertEquals(unigrams.count("și"), 5);
    assertEquals(unigrams.count("în"), 4);
    assertEquals(unigrams.count("îmbunătățire"), 1);
    assertEquals(unigrams.elementSet().size(), 28);
  }

  public void testBigramsIdentification() {
    StringProcessor processor = new StringProcessor();

    Multiset<Bigram> bigrams = processor.buildBigramsSet(NGRAMS_TEXT_CORPORA);

    assertEquals(bigrams.size(), 12);
    assertEquals(bigrams.count(new Bigram("și", "flori")), 2);
    assertEquals(bigrams.count(new Bigram("flori", "treceți")), 0);
    assertEquals(bigrams.elementSet().size(), 5);
  }

  public void testTrigramsIdentification() {
    StringProcessor processor = new StringProcessor();

    Multiset<Trigram> trigrams = processor.buildTrigramsSet(NGRAMS_TEXT_CORPORA);

    assertEquals(trigrams.size(), 4);
    assertEquals(trigrams.count(new Trigram("frunze", "și", "flori")), 2);
    assertEquals(trigrams.count(new Trigram("treceți", "batalioane", "române")), 0);
    assertEquals(trigrams.elementSet().size(), 2);
  }

}
