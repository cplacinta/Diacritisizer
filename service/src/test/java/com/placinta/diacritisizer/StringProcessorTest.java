package com.placinta.diacritisizer;

import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class StringProcessorTest {

  private static final String PHRASE_ONE = "Mama paine alba coace.";
  private static final String PHRASE_TWO = "Noi cantam voios.";
  private static final String[] PHRASES = new String[]{PHRASE_ONE, PHRASE_TWO};
  private static final String[] WORDS = new String[]{"Mama", "paine", "alba", "coace"};

  @Test
  public void testSplitTextToPhrases() {
    StringProcessor processor = new StringProcessor();

    String[] phrases = processor.toPhrases(StringUtils.join(PHRASES, StringUtils.SPACE));

    assertEquals(phrases.length, 2);
    assertTrue(PHRASE_ONE.startsWith(phrases[0]));
    assertTrue(PHRASE_TWO.startsWith(phrases[1]));
  }


  @Test
  public void testSplitPhraseInWords() {
    StringProcessor processor = new StringProcessor();

    String[] words = processor.toWords(PHRASE_ONE.replace(".", ""));

    assertEquals(words.length, 4);
    for (int i = 0; i < words.length; i++) {
      assertEquals(words[i], WORDS[i]);
    }
  }

}
