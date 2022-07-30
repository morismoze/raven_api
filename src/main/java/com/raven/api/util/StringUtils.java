package com.raven.api.util;

import org.apache.commons.text.RandomStringGenerator;
import static org.apache.commons.text.CharacterPredicates.DIGITS;
import static org.apache.commons.text.CharacterPredicates.LETTERS;


public class StringUtils {

    public static String generateUniqueAlphaNumericString (int length) {
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
            .withinRange('0', 'z')
            .filteredBy(LETTERS, DIGITS)
            .build();

            return generator.generate(length);
    }
    
}
