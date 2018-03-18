package minesweeper;

public class Dugged implements Square{

    int count = 0;
    
    @Override
    public int getCount() {
        return count;
    }
    @Override
    public void incCount() {
        count++;
    }
    @Override
    public void decCount() {
        if(count>0)count--;
    }
    @Override
    public boolean isBomb() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Square dig() {
        // TODO Auto-generated method stub
        return this;
    }
    public Dugged(int givecount) {
        this.count = givecount;
    }

    @Override
    public Square flag() {
        // TODO Auto-generated method stub
        return this;
    }

    @Override
    public Square deflag() {
        // TODO Auto-generated method stub
        return this;    
    }
    
    @Override
    public String toString() {
        return (this.count>=0) ? Integer.toString(this.count) : " ";
    }
    
    @Override
    public boolean isDug() {
        return true;
    }
    @Override
    public void dePlaceBomb(){
        return;
    }
    @Override
    public void placeBomb() {
        return;
    }

}
