package com.dak.synechron.rest;
import com.dak.synechron.wordcount.WordCounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WordCountController {

    @Autowired
    private WordCounter counter;

    @RequestMapping("/addword")
    public ResponseEntity addWord(@RequestBody String word) {
        if (counter.addWord(word)) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping("/count")
    public int getWordCount(@RequestBody String word) {
        return counter.getWordCount(word);
    }

}
