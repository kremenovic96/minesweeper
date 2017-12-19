package minesweeper;

/*
 * A immutable datatype representing place on board
 * Square = Flagged(bomb:bool)
 *          +Dugged()
 *          +Untouched(bomb:bool)
 */
public interface Square {

    public boolean isBomb();
    
    @Override 
    public String toString();
    
    public Square dig();
    
    public Square flag();
    
    public Square deflag();
    
    default boolean isDug() {
        return false;
    }
    default boolean isFlag() {
        return false;
    }
    default boolean isUntouched() {
        return false;
    }
    public int getCount();
    public void incCount();
    public void decCount();
}
