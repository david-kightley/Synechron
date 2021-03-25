package com.dak.synechron.wordcount;

import com.dak.synechron.util.Translator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class WordCounterTest {

    @Mock
    private Translator translator;

    private ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);

    private WordCounter wordCounter;

    @Before
    public void setup() {
        translator = new Translator() {
            @Override
            public String translate(String word) {
                return word;
            }
        };
        wordCounter = new WordCounter(translator);
    }

    @Test
    public void testInvalidInput() {
        assertFalse(wordCounter.addWord(null));
        assertFalse(wordCounter.addWord(""));
        assertFalse(wordCounter.addWord("12345"));
        assertFalse(wordCounter.addWord("foob6r"));
        assertFalse(wordCounter.addWord("foo;"));
        assertFalse(wordCounter.addWord("foo bar"));
        assertEquals(0, getCounterSize());
    }

    public void testValidInput() {
        // Given
        final String goodWord = "foo";
        final String secondWord = "bar";

        // Then
        assertEquals(0, getCounterSize());
        assertTrue(wordCounter.addWord(goodWord));
        assertEquals(1, getCounterSize());
        assertEquals(1, wordCounter.getWordCount(goodWord));

        assertEquals(0, wordCounter.getWordCount(secondWord));
        assertTrue(wordCounter.addWord(secondWord));
        assertEquals(2, getCounterSize());
        assertEquals(1, wordCounter.getWordCount(goodWord));
        assertEquals(1, wordCounter.getWordCount(secondWord));

        assertTrue(wordCounter.addWord(goodWord));
        assertEquals(2, getCounterSize());
        assertEquals(2, wordCounter.getWordCount(goodWord));
        assertEquals(1, wordCounter.getWordCount(secondWord));

        assertTrue(wordCounter.addWord(goodWord));
        assertEquals(2, getCounterSize());
        assertEquals(3, wordCounter.getWordCount(goodWord));
        assertEquals(1, wordCounter.getWordCount(secondWord));
    }

    @Test
    public void testAddValidWordIgnoresCase() {
        // Given
        final String lowercase = "foobar";
        final String uppercase = "FOOBAR";
        final String mixedCase = "fOoBAr";
        final String prefixed = "fooBar2";

        // Then
        assertTrue(wordCounter.addWord(lowercase));
        assertEquals(1, wordCounter.getWordCount(lowercase));
        assertEquals(1, wordCounter.getWordCount(uppercase));
        assertEquals(1, wordCounter.getWordCount(mixedCase));
        assertEquals(0, wordCounter.getWordCount(prefixed));

        assertTrue(wordCounter.addWord(uppercase));
        assertEquals(2, wordCounter.getWordCount(lowercase));
        assertEquals(2, wordCounter.getWordCount(uppercase));
        assertEquals(2, wordCounter.getWordCount(mixedCase));

        assertTrue(wordCounter.addWord(mixedCase));
        assertEquals(3, wordCounter.getWordCount(lowercase));
        assertEquals(3, wordCounter.getWordCount(uppercase));
        assertEquals(3, wordCounter.getWordCount(mixedCase));

        assertEquals(1, getCounterSize());
    }

    @Test
    public void testAddValidWordTranslatesToCommonKey() {
        // Given
        translator = mock(Translator.class);
        wordCounter = new WordCounter(translator);

        final String english = "flower";
        final String spanish = "flor";
        final String german = "blume";
        when(translator.translate(english)).thenReturn(english);
        when(translator.translate(spanish)).thenReturn(english);
        when(translator.translate(german)).thenReturn(english);

        // Then
        assertTrue(wordCounter.addWord(spanish));
        assertEquals(1, wordCounter.getWordCount(english));
        assertEquals(1, wordCounter.getWordCount(german));
        assertEquals(1, wordCounter.getWordCount(spanish));

        assertTrue(wordCounter.addWord(german));
        assertEquals(2, wordCounter.getWordCount(english));
        assertEquals(2, wordCounter.getWordCount(german));
        assertEquals(2, wordCounter.getWordCount(spanish));

        assertTrue(wordCounter.addWord(english));
        assertEquals(3, wordCounter.getWordCount(english));
        assertEquals(3, wordCounter.getWordCount(german));
        assertEquals(3, wordCounter.getWordCount(spanish));

        assertEquals(1, getCounterSize());
    }

        private int getCounterSize() {
        try {
            Field f = WordCounter.class.getDeclaredField("wordCache");
            f.setAccessible(true);
            Map<String,Integer> wordCache = (Map<String, Integer>) f.get(wordCounter);
            return wordCache.size();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

}
