package minesweeper;

public class Untouched implements Square{

    private boolean bomb = false;
    int count = 0;
    //count represent number of adjecent bombs
    public Untouched(boolean bomb) {
        this.bomb = bomb; 
    }
    
    public int getCount() {
        return count;
    }
    public void incCount() {
        count++;
    }
    public void decCount() {
        if(count>0)count--;
    }
    
    @Override
    public boolean isBomb() {
        // TODO Auto-generated method stub
        return bomb;
    }

    @Override
    public Square dig() {
        // TODO Auto-generated method stub
        return new Dugged(count);
    }

    @Override
    public Square flag() {
        // TODO Auto-generated method stub
        return new Flagged(bomb);
    }

    @Override
    public Square deflag() {
        // TODO Auto-generated method stub
        return this;
    }
    
    @Override
    public String toString() {
        return "-";
    }

    @Override
    public boolean isUntouched() {
        return true;
    }
    /*
     * removes bomb, used only when reading from file and actually there is no
     * bomb where .25probbability made it be there.
     */
    @Override
    public void dePlaceBomb() {
        this.bomb = false;
    }
    @Override
    public void placeBomb() {
        this.bomb = true;
    }
}
