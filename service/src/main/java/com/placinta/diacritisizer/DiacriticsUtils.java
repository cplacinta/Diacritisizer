package com.placinta.diacritisizer;

import java.text.Normalizer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class DiacriticsUtils {
  public String stripDiacritics(String word) {
    word = Normalizer.normalize(word, Normalizer.Form.NFD).toLowerCase();
    return word.replaceAll("[^\\p{ASCII}]", "");
  }

  public boolean containsDiacritics(String word) {
    return StringUtils.isNotBlank(word) && !stripDiacritics(word).equals(word.toLowerCase());
  }

}
