package com.dak.synechron.wordcount;

import com.dak.synechron.util.Translator;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WordCounter {

    private final Translator translator;

    private final Map<String,Integer> wordCache;

    public WordCounter(Translator translator) {
        this.translator = translator;
        this.wordCache = new ConcurrentHashMap<>();
    }

    public boolean addWord(String word) {
        if (!isValidWord(word)) {
            return false;
        }
        String keyWord = translator.translate(word);
        wordCache.compute(keyWord.toLowerCase(), (k,v) -> v == null ? 1 : v+1);
        return true;
    }

    public int getWordCount(String word) {
        if (word == null || word.isEmpty()) {
            return 0;
        }
        String keyWord = translator.translate(word);
        return wordCache.getOrDefault(keyWord.toLowerCase(), 0);
    }

    private boolean isValidWord(String word) {
        if (word == null || word.isEmpty()) {
            return false;
        }
        return word.chars().allMatch(Character::isAlphabetic);
    }
}
