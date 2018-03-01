/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package minesweeper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * TODO: Specification
 */
public class Board {
    public static String BOOM_MESSAGE = "Boom!";
    // TODO: Abstraction function, rep invariant, rep exposure, thread safety
    /*
     * ADT for representing board consisting of Square objects
     * rep invariant: board != null
     * Abstraction function: map representing Square objects 
     * Thread safety argument:using thread-safe data structure map to store 
     * Square objects
     */
    
    // TODO: Specify, test, and implement in problem 2
    private class Coordinate{
        private final int row;
        private final int col;
        
        public Coordinate(int x, int y) {
            this.row = x;
            this.col =y;
        }
        @Override
        public boolean equals(Object thatObject) {
            if (!(thatObject instanceof Coordinate)) return false;
            Coordinate c = (Coordinate) thatObject;
            return this.row==c.row && this.col==c.col;
        }
        @Override
        public int hashCode() {
            int hash = 7;
            hash = 71 * hash + this.row;
            hash = 71 * hash + this.col;
            return hash;
        }
    }
    
    private final int rows;
    private final int cols;
    //private final Square[][] board;
    private ConcurrentMap<Coordinate, Square> sq;
    public Board(int rows, int cols) {
        sq = new ConcurrentHashMap<>(rows*cols);
        this.rows = rows;
        this.cols = cols;
        for(int i = 0; i < rows; i++) {
            for(int j = 0;j<cols; j++) {
                sq.put(new Coordinate(i,j), new Untouched(Math.random()<0.25));
            }
        }
    }
       
    private synchronized boolean checkCoord(int x, int y) {
        return x>=0 && y>=0 && x<rows && y<cols;
    }

    
    public  String flag(int x, int y) {
        if (checkCoord(x, y)){
            Coordinate c = new Coordinate(x,y);
        //Square sq =  board[x][y];
        if (sq.get(c).isUntouched()) 
            sq.put(c, sq.get(c).flag());
        //  board[x][y] = sq.flag(); 

        }
        return this.toString();
    }
    
    public  String deflag(int x, int y) {
        if (checkCoord(x,y)) {
           // Square sq = board[x][y];
            Coordinate c = new Coordinate(x,y);
            if(sq.get(c).isFlag())
                sq.put(c, sq.get(c).deflag());
              //  board[x][y] = sq.deflag();
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
    public synchronized String dig(int x, int y) {
        String message = null;
        if (checkCoord(x,y)) {
            Coordinate c = new Coordinate(x,y);
            //Square sq = board[x][y];
            boolean hasBomb = sq.get(c).isBomb();//continue this method
            if (sq.get(c).isUntouched())
                sq.put(c, sq.get(c).dig());
            if (hasBomb) message = BOOM_MESSAGE;
            List<Coordinate> adj = getNeighboors(x,y);
            for(Coordinate f:adj) sq.get(f).decCount();//board[f.row][f.col].decCount();
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
            if(sq.get(f).isUntouched())/*if(board[f.row][f.col].isUntouched())*/ {
                /*Square sq = board[f.row][f.col];
                board[f.row][f.col] = sq.dig();*/
                sq.put(f, sq.get(f).dig());
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
            if(sq.get(f).isBomb())
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
                rep += sq.get(new Coordinate(i,j));
            }
            rep += "\n";
        }
        return rep;
    }
    
 
    
}
