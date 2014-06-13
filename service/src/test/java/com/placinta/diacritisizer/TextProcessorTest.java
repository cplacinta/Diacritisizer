package com.placinta.diacritisizer;

import com.placinta.diacritisizer.builder.WordBuilder;
import java.util.Collection;
import java.util.List;
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
  private WordBuilder wordBuilder;

  @BeforeMethod
  public void setUp() {
    DiacriticsUtils diacriticsUtils = new DiacriticsUtils();
    wordBuilder = new WordBuilder(diacriticsUtils);
    processor = new TextProcessor(diacriticsUtils, wordBuilder);
  }

  public void testSplitTextToPhrases() {
    String[] phrases = processor.toPhrases(StringUtils.join(PHRASES, StringUtils.SPACE));

    assertEquals(phrases.length, 2);
    assertTrue(PHRASE_ONE.startsWith(phrases[0]));
    assertTrue(PHRASE_TWO.startsWith(phrases[1]));
  }

  public void testSplitPhraseInWords() {
    List<String> words = processor.toWords(PHRASE_ONE.replace(".", ""));

    assertEquals(words.size(), 4);
    int i = 0;
    for (String word : words) {
      assertEquals(word, WORDS[i]);
      i++;
    }
  }

  public void testAllElementsIdentification() {
    TextProcessingResult result = processor.processText(TEXT_CORPORA);
    Collection<Word> uniqueWords = result.getWords();
    Set<Unigram> unigrams = result.getUnigrams();
    Set<Bigram> bigrams = result.getBigrams();
    Set<Trigram> trigrams = result.getTrigrams();


    assertEquals(uniqueWords.size(), 61);
    assertEquals(unigrams.size(), 28);
    assertEquals(bigrams.size(), 53);
    assertEquals(trigrams.size(), 31);

    Word word2 = wordBuilder.buildWord("indispensabilă");
    Word word3 = wordBuilder.buildWord("pentru");
    Word word4 = wordBuilder.buildWord("calitate");

    assertTrue(uniqueWords.contains(wordBuilder.buildWord("este")));
    assertFalse(uniqueWords.contains(wordBuilder.buildWord("2014")));

    assertTrue(unigrams.contains(new Unigram(word2)));
    assertFalse(unigrams.contains(new Unigram(wordBuilder.buildWord("le"))));

    verifyFrequency(unigrams);

    assertTrue(bigrams.contains(new Bigram(wordBuilder.buildWord("este"), word2)));
    assertFalse(bigrams.contains(new Bigram(word4, word2)));

    assertTrue(trigrams.contains(new Trigram(wordBuilder.buildWord("este"), word2, word3)));
    assertFalse(trigrams.contains(new Trigram(word4, wordBuilder.buildWord("este"), word2)));
  }

  private void verifyFrequency(Set<Unigram> unigrams) {
    Unigram frequentUnigram = new Unigram(wordBuilder.buildWord("și"));
    frequentUnigram.setFrequency(5);
    for (Unigram unigram : unigrams) {
      if (unigram.equals(frequentUnigram)) {
        assertEquals(unigram.getFrequency(), frequentUnigram.getFrequency());
      }
    }
  }

}
