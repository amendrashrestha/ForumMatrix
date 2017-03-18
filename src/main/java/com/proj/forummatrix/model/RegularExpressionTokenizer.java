/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proj.forummatrix.model;

/**
 *
 * @author amendrashrestha
 */
import com.proj.forummatrix.utilities.TFIDFCalculator.Tokenizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Tokenize text using a regular expression.
 * The default returns alphanumeric words.
 */
public class RegularExpressionTokenizer implements Tokenizer {
    final private Pattern regex;

    public RegularExpressionTokenizer(Pattern regex) {
        this.regex = regex;
    }

    public RegularExpressionTokenizer() {
        this(Pattern.compile("\\s+"));
    }

    @Override
    public List<String> tokenize(String text) {
        List<String> tokens = new ArrayList<>();
        Matcher matcher = regex.matcher(text);
        while (matcher.find()) {
            tokens.add(matcher.group());
        }
        return tokens;
    }
}
