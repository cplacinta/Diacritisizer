package com.placinta.diacritisizer;

import com.placinta.diacritisizer.builder.WordFactory;
import java.util.Collection;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

@Test
public class TextProcessorTest {

  private static final String PHRASE_ONE = "Mama paine alba coace.";
  private static final String PHRASE_TWO = "Noi cantam voios.";
  private static final String[] PHRASES = new String[]{PHRASE_ONE, PHRASE_TWO};
  private static final String[] WORDS = new String[]{"Mama", "paine", "alba", "coace"};
  private static final String TEXT_CORPORA = "Educația de calitate este indispensabilă pentru dezvoltarea și " +
    "valorificarea potențialului fiecărui copil și, în cele din urmă, pentru prosperarea țării în care trăim. " +
    "Sesiunea de bacalaureat 2014, care începe astăzi, constituie un element important în procesul de îmbunătățire " +
    "a calității educației. O evaluare corectă și transparentă, care asigură că fiecare elev obține rezultate " +
    "conform meritelor, le oferă tinerilor șanse egale de a-și alege și construi o carieră profesională și " +
    "generează schimbări semnificative de atitudine în întreg procesul educațional.";

  private TextProcessor processor;
  private WordFactory wordFactory;

  @BeforeMethod
  public void setUp() {
    DiacriticsUtils diacriticsUtils = new DiacriticsUtils();
    wordFactory = new WordFactory(diacriticsUtils);
    processor = new TextProcessor(diacriticsUtils, wordFactory);
  }

  public void testSplitTextToPhrases() {
    String[] phrases = processor.toPhrases(StringUtils.join(PHRASES, StringUtils.SPACE));

    assertEquals(phrases.length, 2);
    assertTrue(PHRASE_ONE.startsWith(phrases[0]));
    assertTrue(PHRASE_TWO.startsWith(phrases[1]));
  }

  public void testSplitPhraseInWords() {
    String[] words = processor.toWords(PHRASE_ONE.replace(".", ""));

    assertEquals(words.length, 4);
    for (int i = 0; i < words.length; i++) {
      assertEquals(words[i], WORDS[i]);
    }
  }

  public void testAllElementsIdentification() {
    TextProcessingResult result = processor.processText(TEXT_CORPORA);
    Collection<Word> uniqueWords = result.getWords();
    Set<Unigram> unigrams = result.getUnigrams();
    Set<Bigram> bigrams = result.getBigrams();
    Set<Trigram> trigrams = result.getTrigrams();


    assertEquals(uniqueWords.size(), 62);
    assertEquals(unigrams.size(), 28);
    assertEquals(bigrams.size(), 53);
    assertEquals(trigrams.size(), 31);

    Word word2 = wordFactory.createWord("indispensabilă");
    Word word3 = wordFactory.createWord("pentru");
    Word word4 = wordFactory.createWord("calitate");

    assertTrue(uniqueWords.contains(wordFactory.createWord("este")));

    assertTrue(unigrams.contains(new Unigram(word2)));
    assertFalse(unigrams.contains(new Unigram(wordFactory.createWord("este"))));

    verifyFrequency(unigrams);

    assertTrue(bigrams.contains(new Bigram(wordFactory.createWord("este"), word2)));
    assertFalse(bigrams.contains(new Bigram(word4, word2)));

    assertTrue(trigrams.contains(new Trigram(wordFactory.createWord("este"), word2, word3)));
    assertFalse(trigrams.contains(new Trigram(word4, wordFactory.createWord("este"), word2)));
  }

  private void verifyFrequency(Set<Unigram> unigrams) {
    Unigram frequentUnigram = new Unigram(wordFactory.createWord("și"));
    frequentUnigram.setFrequency(5);
    for (Unigram unigram : unigrams) {
      if (unigram.equals(frequentUnigram)) {
        assertEquals(unigram.getFrequency(), frequentUnigram.getFrequency());
      }
    }
  }

}
