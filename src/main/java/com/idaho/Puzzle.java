package com.idaho;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Puzzle read, parses and solves a 2d word puzzle.
 * 
 * @author Mike Lopke
 * @version 1.0
 */
public class Puzzle {

    /**
     * Constructor for Puzzle.
     * 
     */
    public Puzzle() {
    }

    /**
     * Get the puzzle from the given file name by
     * reading each line and storing in a list.
     * 
     * @param puzzleFilename
     * @return List<String>
     */
    List<String> getLines(String puzzleFilename) {
        Path filePath = (Paths.get(puzzleFilename));
        List<String> puzzleLines = new ArrayList<>();
        try {
            puzzleLines = Files.readAllLines(filePath);
        } catch (Exception e) {
            System.err.println("Error reading String from file");
        }
        return puzzleLines;
    }

    /**
     * Parse through the puzzle and return a record that has
     * the row and colum size, a matrix of the puzzle and
     * an array of words to find in the puzzle.
     * 
     * @param puzzleLines
     * @return PuzzleData
     */
    PuzzleData decode(List<String> puzzleLines) {
        int lineNumber = 0;

        // get first line and puzzle dimentions
        String[] size = puzzleLines.get(lineNumber++).split("x");
        int row = Integer.parseInt(size[0]);
        int column = Integer.parseInt(size[1]);

        // get the matrix
        char[][] matrix = new char[row][column];
        for (int i = 0; i < row; i++) {
            String line = puzzleLines.get(lineNumber++).replaceAll("\\s", "");
            char[] columns = line.toCharArray();
            matrix[i] = columns;
        }

        // calculate how many words there are to find
        int numbOfWords = puzzleLines.size() - row - 1;

        String[] words = new String[numbOfWords];
        for (int i = 0; i < words.length; i++) {
            words[i] = puzzleLines.get(lineNumber++).replaceAll("\\s", "");
        }

        // store the decoded puzzle data
        PuzzleData puzzleData = new PuzzleData(row, column, matrix, words);
        return puzzleData;
    }

    /**
     * Given a record with the puzzleData, this method finds the location of each
     * word in
     * the puzzle matrix. The method searches each at cell in the matrix until it
     * finds
     * the occurance of the word.
     * 
     * @param puzzleData
     * @return SolutionKeyData[]
     */
    SolutionKeyData[] solve(PuzzleData puzzleData) {

        SolutionKeyData[] answers = new SolutionKeyData[puzzleData.words().length];
        int count = 0;
        for (String word : puzzleData.words()) {

            SolutionKeyData data = null;
            wordSearch: {
                for (int row = 0; row < puzzleData.row(); row++) {
                    for (int col = 0; col < puzzleData.column(); col++) {
                        data = getWordFromCell(puzzleData, row, col, word);
                        if (data != null) {
                            answers[count++] = data;
                            break wordSearch; // found an answer so go to next word
                        }
                    }
                }
            }
            // some error handling if word is not found
            if (data == null) {
                data = new SolutionKeyData(word + " not found", -1, -1, -1, -1);
                answers[count++] = data;
                System.err.println(word + " :not found in puzzle");
            }
        }
        return answers;
    }

    /**
     * This method searches for the occurance of a word at a specific row and column
     * starting point in the
     * puzzle matrix. It checks in all 8 directions and only
     * 
     * @param puzzleData - record containing the puzzle
     * @param row        - row to be considered
     * @param col        - column to be considered
     * @param word       - word that is being searched for
     * @return SolutionKeyData - record containg the solution
     */
    public SolutionKeyData getWordFromCell(PuzzleData puzzleData, int row, int col, String word) {
        // (row:column) for 8 directions
        final int[][] wordDirections = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 }, { 1, 1 }, { -1, 1 }, { 1, -1 },
                { -1, -1 } };

        char[][] matrix = puzzleData.matrix();
        SolutionKeyData solutionKeyData = null;

        if (matrix[row][col] == word.charAt(0)) {

            for (int[] direction : wordDirections) {

                // calculate endpoints and check that they are in the array bounds
                int rowEnd = row + direction[0] * (word.length() - 1);
                int columnEnd = col + direction[1] * (word.length() - 1);
                if (columnEnd < 0 || columnEnd > puzzleData.column() - 1 || rowEnd < 0
                        || rowEnd > puzzleData.row() - 1) {
                    continue;
                }

                boolean solved = true;
                for (int i = 0; i < word.length(); i++) {
                    if (matrix[row + i * direction[0]][col + i * direction[1]] != word.charAt(i)) {
                        solved = false;
                        break;
                    }
                }

                if (solved) {
                    solutionKeyData = new SolutionKeyData(word, row, col, rowEnd, columnEnd);
                    break;
                }
            }
        }

        return solutionKeyData;
    }

    /**
     * This gets called from the command line and prints the solution
     * 
     * @param args - filename containing the puzzle to be solved
     */
    public static void main(String[] args) {
        if (args.length > 0) {
            String filename = args[0];
            Puzzle puzzle = new Puzzle();
            List<String> lines = puzzle.getLines(filename);
            PuzzleData puzzleData = puzzle.decode(lines);
            SolutionKeyData[] solutionKeyData = puzzle.solve(puzzleData);
            for (SolutionKeyData line : solutionKeyData) {
                System.out.println(line.toString());
            }
        }
    }

}
