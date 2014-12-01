
/**
 * Write a description of class BossRoom here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class BossRoom extends Room
{
    public boolean locked;
    
    /**
     * Constructor for objects of class BossRoom
     */
    public BossRoom(String pDesc)
    {
        super(pDesc);
        locked = false;
    }
    
    public boolean isBossRoom()
    {
        return true;
    }
    
}
