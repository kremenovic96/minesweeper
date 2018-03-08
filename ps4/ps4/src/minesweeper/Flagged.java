package minesweeper;

public class Flagged implements Square {

    
    boolean bomb = false;
    int count = 0;
    public Flagged(boolean hasBomb) {
        this.bomb = hasBomb;
    }
    @Override
    public int getCount() {
        return count;
    }
    @Override
    public void incCount() {
        count ++;
    }
    @Override
    public void decCount() {
        if(count>0)count--;
    }
    @Override
    public boolean isBomb() {
        // TODO Auto-generated method stub
        return bomb;
    }
    
    @Override
    public String toString() {
        return "F";
    }

    @Override
    public Square dig() {
        // TODO Auto-generated method stub
        return this;
    }

    @Override
    public Square flag() {
        // TODO Auto-generated method stub
        return this;
    }

    @Override
    public Square deflag() {
        // TODO Auto-generated method stub
        return new Untouched(bomb);
    }

    @Override
    public boolean isFlag() {
        return true;
    }
    @Override
    public void dePlaceBomb() {
        return;
    }
    @Override
    public void placeBomb() {
        return;
    }
}
