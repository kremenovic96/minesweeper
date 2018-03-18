/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package minesweeper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.io.*;
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
        @Override
        public String toString() {
        	return Integer.toString(row) +","+ Integer.toString(col) + " ";
        }
    }
    
    public final int rows;
    public final int cols;
    public Coordinate makeCoord(int x, int y) {
        return new Coordinate(x,y);
    }
    //private final Square[][] board;
    private ConcurrentMap<Coordinate, Square> sq;
    public Board(int rowss, int colss) {
        if(rowss == -1 && colss==-1) {
            this.rows = 10; this.cols = 10;
        }
        else {
        this.rows = rowss;
        this.cols = colss;
        }
        sq = new ConcurrentHashMap<>(rows*cols);
        for(int i = 0; i < rows; i++) {
            for(int j = 0;j<cols; j++) {
                sq.put(new Coordinate(i,j), new Untouched(Math.random()<0.25));
            }
        }
    }
    public static Board fromFile(String s) throws IOException {
        File f = new File(s);
        BufferedReader br = new BufferedReader(new FileReader(f));
        String line = br.readLine();
        String[] dimensions = line.split(" ");
        int x = Integer.parseInt(dimensions[0]); int y = Integer.parseInt(dimensions[1]);
        Board bb = new Board(x, y);
        int lineNum = 0;
        while((line = br.readLine()) != null) {
            String[] bombInfo=line.split(" ");
            for(int i =0;i<bombInfo.length;i++) {
                if(Integer.parseInt(bombInfo[i])==1) {
                    Coordinate c = bb.makeCoord(lineNum, i);
                    bb.sq.get(c).placeBomb();
                }
                else {
                    Coordinate c = bb.makeCoord(lineNum, i);
                    bb.sq.get(c).dePlaceBomb();
                }
            }
            lineNum++;
        }br.close();
        return bb;
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
            /*System.out.println(sq.get(c).isDug());
            System.out.println(sq.get(c).getCount());//A*/
            if (hasBomb) message = BOOM_MESSAGE;
            List<Coordinate> adj = getNeighboors(x,y);
            
            for(Coordinate f:adj) {if(!sq.get(f).isDug()) sq.get(f).decCount();}//board[f.row][f.col].decCount();
            for(Coordinate f:adj) {//dodata ova petlja
            	System.out.println(sq.get(c).getCount()+" first, size of adj: "+adj.size());
                if(sq.get(f).isBomb()) sq.get(c).incCount();
            } System.out.println(adj);
            //System.out.println(hasBombsAround(x,y));
          //  System.out.println(sq.get(new Coordinate(6,0)).isBomb()+ "6,1bomb");

            autoDigAround(x, y);
            
        } 
        //if (message == null) return this.toString();
        //return message;
        return this.toString();
    }
    
    /*
     * If the square x,y has no neighbor squares with bombs,
     *  then for each of x,y’s untouched neighbor squares, 
     *  change said square to dug and repeat this step recursively 
     *  for said neighbor square unless said neighbor square was 
     *  already dug before said change.
     */
    private synchronized void autoDigAround(int x, int y) {
        List<Coordinate> adj = getNeighboors(x,y);
        if (hasBombsAround(x, y)) {System.out.println("BOOMBBBB AROUND");return;}
        for(Coordinate f:adj) {
            if(sq.get(f).isUntouched())/*if(board[f.row][f.col].isUntouched())*/ {
                /*Square sq = board[f.row][f.col];
                board[f.row][f.col] = sq.dig();*/
            	sq.put(f, sq.get(f).dig());
            }
            /*if(!hasBombsAround(f.row, f.col)) {
                for(Coordinate s:getNeighboors(f.row, f.col))
                    adj.add(s);
            }*/
        }
       System.out.println("autodigaroundwashere");
    }
    
    /*
     * Checks if there is Square with bomb that is adjecent to x,y pair.
     */
    private synchronized boolean hasBombsAround(int x, int y) {
        //List<Coordinate> adj = getNeighboors(x, y);
        CopyOnWriteArrayList<Coordinate> adj =  getNeighboors(x,y);
    	for(Coordinate f : adj) {
            if(sq.get(f).isBomb())
                return true;                
        }
        return false;
    }
    
    public CopyOnWriteArrayList<Coordinate> getNeighboors(int x, int y){
       // List<Coordinate> ls = new ArrayList<>();
        CopyOnWriteArrayList<Coordinate> ls =  new CopyOnWriteArrayList<>();

    	if (x>0 && y>0) {
            ls.add(new Coordinate(x-1, y-1));
            ls.add(new Coordinate(x-1, y));
            ls.add(new Coordinate(x, y-1));
        }
        if(x>=0 && y<cols-1) {
            ls.add(new Coordinate(x, y+1));
        }
        if(x<rows-1 && y>=0) {
            ls.add(new Coordinate(x+1, y));
        }
        
        if(x<rows-1 && y<cols-1) {
            ls.add(new Coordinate(x+1, y+1));

        }
        if(x<rows-1 && y>0) {
            ls.add(new Coordinate(x+1, y-1));

        }
        if(x>0 && y < cols-1) {
            ls.add(new Coordinate(x-1, y+1));

        }//
        /*if(y+1<cols && x+1<rows) {
        	ls.add(new Coordinate(x+1,y+1));
        }*/
       /* if(y-1>0 && x-1 >0) {
        	ls.add(new Coordinate(x-1,y-1));
        }*/
       /* 
        if(x+1<rows && y-1>0) {
            ls.add(new Coordinate(x+1,y-1));
        }*/
        ///*
     /*   if(y+1<cols && x+1<rows) {
        	ls.add(new Coordinate(x+1,y+1));
        }*/
        /*if(y-1>0 && x-1 >0) {
        	ls.add(new Coordinate(x-1,y-1));
        }
        if(x+1<rows && y-1>0) {
        	ls.add(new Coordinate(x+1, y-1));
        }
        if(x-1>0 && y+1<cols) {
        	ls.add(new Coordinate(x-1, y+1));
        }*/
        
    return ls;
    }
    
    @Override
    public String toString() {
        String rep = "";
        for(int i =0;i<rows;i++) {
            for(int j=0;j<cols;j++) {
                rep += sq.get(new Coordinate(i,j));
                if(j<cols-1) rep += " ";
            }
            rep += "\n";
        }
        return rep;
    }
    
 /*
  * few methods bellow to help with testing
  */
    public boolean isBombAt(int x, int y) {
        Coordinate c = new Coordinate(x,y);
        return sq.get(c).isBomb();
    }
    
}
