
/**
 * Write a description of class Weapon here.
 * 
 * @author Ethan Dowler 
 * @version prototype Weapon class
 */
public class Weapon extends Item
{
    private String desc;
    private int wPower;
    private int wDefense;


    /**
     * Constructor for objects of class Weapon
     */
    public Weapon(String pDesc, int pPower, int pDefense)
    {
        super(pDesc, pPower, pDefense);
        wPower = pPower;
        wDefense = pDefense;
    }
    
    public void print()
    {
        System.out.print("Weapon: " ); 
        super.print();
    }

    public boolean isWeapon()
    {
        return true;
    }
    
    public int getPower()
    {
        return wPower;
    }

    public int getDefense()
    {
       return wDefense; 
    }
}
