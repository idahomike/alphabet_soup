package com.idaho;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class PuzzleTest {

    /*
     * Test functionality around reading the puzzle from a file line by line.
     */
    @Test
    void testGetPuzzleLines() {
        String filename = "src/test/java/com/idaho/test1.txt";
        Puzzle puzzle = new Puzzle();
        List<String> puzzleLines = puzzle.getLines(filename);
        System.out.println(puzzleLines.toString());

        int fileLines = puzzleLines.size();
        Assert.assertEquals(fileLines, 6, "Should find 6 lines in puzzle");
        Assert.assertEquals(puzzleLines.get(0), "3x3", "First line should have dimentions");
        Assert.assertEquals(puzzleLines.get(fileLines - 1), "AEI", "Last line should be AEI");

    }

    /*
     * Testing decoding puzzle into it's seperate parts.
     */
    @Test
    void testDecode() {
        Puzzle puzzle = new Puzzle();
        ArrayList<String> puzzleLines = new ArrayList<>(
                List.of("4x3", "A B C", "D E F", "G H I", "X Z Z", "AB C", "AEI"));
        PuzzleData puzzleData = puzzle.decode(puzzleLines);

        Assert.assertEquals(puzzleData.row(), 4, "Should have 4 rows in puzzle");
        Assert.assertEquals(puzzleData.column(), 3, "Should have 3 columnsin puzzle");
        char[][] matrix = puzzleData.matrix();
        Assert.assertEquals(matrix[0][0], 'A', "Should find location of A in matrix");
        Assert.assertEquals(matrix[1][0], 'D', "Should find location of D in matrix");
        Assert.assertEquals(puzzleData.words()[0], "ABC", "Should clean whitespaces");

    }

    /*
     * Different word to find that cover all 8 directions.
     */
    @DataProvider(name = "wordFind")
    public static Object[][] wordFind() {
        return new Object[][] {
                { "ab1", 3, 2, 1, 2 }, // up
                { "ab2", 3, 2, 1, 4 }, // up right
                { "ab3", 3, 2, 3, 4 }, // right
                { "ab4", 3, 2, 5, 4 }, // right down
                { "ab5", 3, 2, 5, 2 }, // down
                { "ab6", 3, 2, 5, 0 }, // down left
                { "ab7", 3, 2, 3, 0 }, // left
                { "ab8", 3, 2, 1, 0 }, // up left
        };
    }

    /*
     * Test that you can find all 8 words with same starting point.
     * 
     */
    @Test(dataProvider = "wordFind")
    void testFindWord(String word, int row, int column, int rowEnd, int columnEnd) {

        char[][] matrix = {
                { '.', '.', '.', '.', '.' },
                { '8', '.', '1', '.', '2' },
                { '.', 'b', 'b', 'b', '.' },
                { '7', 'b', 'a', 'b', '3' },
                { '.', 'b', 'b', 'b', '.' },
                { '6', '.', '5', '.', '4' },
                { '.', '.', '.', '.', '.' },
        };
        PuzzleData testPuzzleData = new PuzzleData(7, 5, matrix, new String[] { word });
        Puzzle testPuzzle = new Puzzle();

        SolutionKeyData data = testPuzzle.getWordFromCell(testPuzzleData, row, column, word);
        Assert.assertEquals(data.rowEnd(), rowEnd, String.format("%s expected to end on row %d", word, rowEnd));
        Assert.assertEquals(data.columnEnd(), columnEnd);
    }

    /*
     * Different words at different starting locations with unique directions
     */
    @DataProvider(name = "wordKey")
    public static String[][] wordKeys() {
        return new String[][] {
                { "abcde", " 0:0 0:4" }, // right
                { "ihgf", " 1:3 1:0" }, // left
                { "hmr", " 1:2 3:2" }, // down
                { "qlgb", " 3:1 0:1" }, // up
                { "plhd", " 3:0 0:3" }, // up right
                { "hn", " 1:2 2:3" }, // down right
                { "qk", " 3:1 2:0" }, // up left
                { "eimq", " 0:4 3:1" }, // down left
        };
    }

    /*
     * Test each word is correctly found
     */
    @Test(dataProvider = "wordKey")
    void testSolve(String word, String expectedBounds) {

        char[][] matrix = {
                { 'a', 'b', 'c', 'd', 'e' },
                { 'f', 'g', 'h', 'i', 'j' },
                { 'k', 'l', 'm', 'n', 'o' },
                { 'p', 'q', 'r', 's', 't' }
        };
        int rows = matrix.length;
        int columns = matrix[rows - 1].length;

        String[] words = { word };
        PuzzleData testPuzzleData = new PuzzleData(rows, columns, matrix, words);

        Puzzle testPuzzle = new Puzzle();
        SolutionKeyData[] solution = testPuzzle.solve(testPuzzleData);
        Assert.assertEquals(solution[0].toString(), word + expectedBounds);
    }

}
