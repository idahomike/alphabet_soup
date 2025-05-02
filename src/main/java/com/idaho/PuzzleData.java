package com.idaho;

/**
 * Record for storing the puzzle data. consists of number of rows and columns,
 * the matric of characters and an array of words that are searching for.
 *
 * @param row    - number of rows
 * @param column - number of columns
 * @param matrix - puzzle matrix
 * @param words  - words to find in the puzzle
 */
public record PuzzleData(int row, int column, char[][] matrix, String[] words) {
}
