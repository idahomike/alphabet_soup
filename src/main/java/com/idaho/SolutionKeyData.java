package com.idaho;

/**
 * Record for storing Solution key data.
 * 
 * @param word        - word in puzzle
 * @param rowStart    - row where the word starts
 * @param rowEnd      - row where the word ends
 * @param columnStart - column where the word starts
 * @param columnEnd   - column where the word ends
 */
public record SolutionKeyData(String word, int rowStart, int columnStart, int rowEnd, int columnEnd) {

    /**
     * Method for returning a nicelyformated solution key for one answer
     * 
     * @return String
     */
    @Override
    public final String toString() {
        return String.format("%s %d:%d %d:%d", word, rowStart, columnStart, rowEnd, columnEnd);
    }

}
