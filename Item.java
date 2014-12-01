
/**
 * Write a description of class Item here.
 * 
 * @author Ethan Dowler
 * @version this is to practice with inheritance and make the code better
 */
public abstract class Item
{
    // instance variables - replace the example below with your own
    private String desc;
    static int power;
    static int defense;

    /**
     * Constructor for objects of class Item
     */
    public Item(String pDesc, int pPower, int pDefense)
    {
        desc = pDesc;
        power = pPower;
        defense = pDefense;
    }
    
    public void print()
    {
        System.out.println(desc);
        System.out.print("Power: "+this.power+"   ");
        System.out.println("Defense: "+this.defense);
    }

    public String getDesc()
    {
        return desc;
    }
    
    public int getPower()
    {
        return power;
    }

    public int getDefense()
    {
       return defense; 
    }
    
    public void changeDesc(String newDesc)
    {
        desc = newDesc;
    }
    
    public void addPower(int pPower)
    {
        power += pPower;
    }
    
    public void addDefense(int pDefense)
    {
        defense += pDefense;
    }
    
    public boolean isWeapon(){return false;}
    
    public boolean isMonster(){return false;}
}
