/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package minesweeper;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Specification
 */
public class Board {
    public static String BOOM_MESSAGE = "Boom!";
    // TODO: Abstraction function, rep invariant, rep exposure, thread safety
    /*
     * ADT for representing board consisting of Square objects
     * rep safety: board is nonnegative size 
     * Abstraction function: Array of Square objects representing 2D space.
     */
    
    // TODO: Specify, test, and implement in problem 2
    private class Coordinate{
        public int row;
        public int col;
        
        public Coordinate(int x, int y) {
            this.row = x;
            this.col =y;
        }
    }
    
    private final int rows;
    private final int cols;
    private final Square[][] board;
  
    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.board = new Square[rows][cols];
        for(int i = 0; i < rows; i++) {
            for(int j = 0;j<cols; j++) {
                board[i][j] = new Untouched(Math.random()<0.25);
            }
        }
        checkRep();
    }
       
    private boolean checkCoord(int x, int y) {
        return x>=0 && y>=0 && x<rows && y<cols;
    }
    public void checkRep() {
        assert(rows>-1);
        assert(cols>-1);
    }
    
    public String flag(int x, int y) {
        if (checkCoord(x, y)){
        Square sq =  board[x][y];
        if (sq.isUntouched()) 
            board[x][y] = sq.flag();       
        }
        return this.toString();
    }
    
    public String deflag(int x, int y) {
        if (checkCoord(x,y)) {
            Square sq = board[x][y];
            if(sq.isFlag())
                board[x][y] = sq.deflag();
        }
        return this.toString();
    }
    //this is where i left
    /*
     * If square x,y contains a bomb, change it so that it contains 
     * no bomb and send a BOOM message to the user. Then, if the 
     * debug flag is missing (see Question 4), terminate the user’s 
     * connection. See again the section below for the exact required 
     * format of the BOOM message. Note: When modifying a square from 
     * containing a bomb to no longer containing a bomb, make sure that
     * subsequent BOARD messages show updated bomb counts in the adjacent 
     * squares. After removing the bomb continue to the next step.
     */
    public String dig(int x, int y) {
        String message = null;
        if (checkCoord(x,y)) {
            Square sq = board[x][y];
            boolean hasBomb = sq.isBomb();//continue this method
            if (sq.isUntouched())
                board[x][y] = sq.dig();
            if (hasBomb) message = BOOM_MESSAGE;
            List<Coordinate> adj = getNeighboors(x,y);
            for(Coordinate f:adj) board[f.row][f.col].decCount();
            autoDigAround(x, y);
            
        }
        if (message == null) return this.toString();
        return message;
    }
    
    /*
     * If the square x,y has no neighbor squares with bombs,
     *  then for each of x,y’s untouched neighbor squares, 
     *  change said square to dug and repeat this step recursively 
     *  for said neighbor square unless said neighbor square was 
     *  already dug before said change.
     */
    private void autoDigAround(int x, int y) {
        List<Coordinate> adj = getNeighboors(x,y);
        if (hasBombsAround(x, y)) return;
        for(Coordinate f:adj) {
            if(board[f.row][f.col].isUntouched()) {
                Square sq = board[f.row][f.col];
                board[f.row][f.col] = sq.dig();
            }
            if(!hasBombsAround(f.row, f.col)) {
                for(Coordinate s:getNeighboors(f.row, f.col))
                    adj.add(s);
            }
        }
    }
    
    /*
     * Checks if there is Square with bomb that is adjecent to x,y pair.
     */
    private boolean hasBombsAround(int x, int y) {
        List<Coordinate> adj = getNeighboors(x, y);
        for(Coordinate f : adj) {
            if(board[f.row][f.col].isBomb())
                return true;                
        }
        return false;
    }
    
    private List<Coordinate> getNeighboors(int x, int y){
        List<Coordinate> ls = new ArrayList<>();
        if (x>0 && y>0) {
            ls.add(new Coordinate(x-1, y-1));
            ls.add(new Coordinate(x-1, y));
            ls.add(new Coordinate(x, y-1));
        }
        if(x>0 && y==0) {
            ls.add(new Coordinate(x, y+1));
        }
        if(x==0 && y>0) {
            ls.add(new Coordinate(x+1, y));
        }
        
        if(x<rows-1 && y<cols-1) {
            ls.add(new Coordinate(x+1, y+1));

        }
        if(x<rows-1 && y==cols-1) {
            ls.add(new Coordinate(x+1, y-1));

        }
        if(x==rows-1 && y < cols-1) {
            ls.add(new Coordinate(x-1, y+1));

        }
        return ls;
    }
    
    @Override
    public String toString() {
        String rep = "";
        for(int i =0;i<rows;i++) {
            for(int j=0;j<cols;j++) {
                rep += board[i][j];
            }
            rep += "\n";
        }
        return rep;
    }
    
 
    
}
