package com.dak.synechron.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class TranslatorImpl implements Translator {

    private final Map<String,String> translationCache;

    public TranslatorImpl() {
        translationCache = new HashMap<>();
        loadTranslations();
    }

    private void loadTranslations() {
        final String mappingFile = "/translations.txt";
        URL url = this.getClass().getResource(mappingFile);
        File file = new File(url.getFile());
        try (BufferedReader br = new BufferedReader(new FileReader(url.getFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] sa = line.split(",");
                    translationCache.put(sa[0], sa[1]);
                }
            }
        } catch (Exception e) {
            // Log error
        }
    }

    public String translate(String word) {
        return translationCache.getOrDefault(word, word);
    }
}
