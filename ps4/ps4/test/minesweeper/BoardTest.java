/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package minesweeper;

import static org.junit.Assert.assertEquals;

import java.io.IOException;


import org.junit.Test;

/**
 * TODO: Description
 */
public class BoardTest {
    
    // TODO: Testing strategy
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    // TODO: Tests

    /*
     * Initiate two seperate Boards
     * check if every square is untouched
     */
    @Test
    public void testStartingState() {
        Board a = new Board(3,3);
        Board b = new Board(4,4);
        String aa = "- - -\n- - -\n- - -\n";
        String bb = "- - - -\n- - - -\n- - - -\n- - - -\n";
       // System.out.println(aa);
        assertEquals(a.toString(), aa);
        assertEquals(b.toString(), bb);
    }
    /*
     * Initiate Board
     * flag some squares
     * deflag flagged squares
     * deflag unflagged squares
     */
    @Test
    public void testFlagAndDeflag() {
        Board a = new Board(3,3);
        a.flag(0, 1);
        String aa = "- F -\n- - -\n- - -\n";
        assertEquals(a.toString(), aa);
        a.flag(1, 0);
        aa = "- F -\nF - -\n- - -\n";
        assertEquals(a.toString(), aa);
        a.flag(2, 2);
        aa = "- F -\nF - -\n- - F\n";
        assertEquals(a.toString(), aa);
        a.deflag(1, 0);
        aa = "- F -\n- - -\n- - F\n";
        assertEquals(a.toString(), aa);
        a.deflag(2, 2);
        a.deflag(1,1);
        aa = "- F -\n- - -\n- - -\n";
        assertEquals(a.toString(), aa);
        a.deflag(0, 1);
        aa = "- - -\n- - -\n- - -\n";
        assertEquals(a.toString(), aa);        
    }
  
    
    @Test
    public void testDig() {
        Board a = new Board(3,3);
       // System.out.println(a.sq.size());
        a.dig(0, 1);
        String aa = "-   -\n- - -\n- - -\n";
        //System.out.print(a);
        assertEquals(a.toString(), aa);   
        a.dig(2, 1);
        aa = "-   -\n- - -\n-   -\n";
        assertEquals(a.toString(), aa);   
        a.dig(2, 2);
        aa = "-   -\n- - -\n-    \n";
        assertEquals(a.toString(), aa);   
    }
    /*
     * test strategy:
     * make a board from file
     * check number of bombs in a board
     * check correctness of number of bombs
     */
    @Test
    public void testFromFIle() throws IOException {
      //  Path path = FileSystems.getDefault().getPath(".");
      //  System.out.println(path.toAbsolutePath());
         Board a = Board.fromFile("test/minesweeper/board_file_5");
        // System.out.println(a); 
         int bombs=0;
         for(int i=0;i<a.rows;i++) {
             for(int j=0;j<a.cols;j++) {
                 if(a.isBombAt(i, j)) bombs++;
             }
         }
         assertEquals(2, bombs);
   
         a = Board.fromFile("test/minesweeper/myboard");
        // System.out.println(a);
         bombs =0;
         for(int i=0;i<a.rows;i++) {
             for(int j=0;j<a.cols;j++) {
                 if(a.isBombAt(i, j)) bombs++;
             }
         }
         assertEquals(6, bombs);
         a.dig(1, 1);
         System.out.println(a);
         a.dig(0, 2);
         System.out.println(a);
         a.dig(3, 0);
         System.out.println(a);
        /* a = Board.fromFile("test/minesweeper/board_file_5");
         a.dig(3, 1);
         System.out.println(a);*/
    }
        
    
}
