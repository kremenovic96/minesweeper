package minesweeper;

/*
 * A immutable datatype representing place on board
 * Square = Flagged(bomb:bool)
 *          +Dugged()
 *          +Untouched(bomb:bool)
 */
public interface Square {
    /*
     * removes bomb, used only when reading from file and actually there is no
     * bomb where .25probbability made it be there.
     * USED ONLY ON Untouched class.
     */
    public void dePlaceBomb();
    public void placeBomb();
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
