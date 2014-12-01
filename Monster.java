
/**
 * Write a description of class Monster here.
 * 
 * @author Ethan Dowler 
 * @version prototype for Monster class
 */
public class Monster extends Item
{
    private int maxHealth;
    private int currentHP;
    private int mPower;
    private int mDefense;
    private String mDesc;
    
    /**
     * Constructor for objects of class Monster
     */
    public Monster(String pDesc, int pHealth, int pPower, int pDefense)
    {
        super(pDesc, pPower, pDefense);
        maxHealth = pHealth;
        currentHP = maxHealth;
        mPower = pPower;
        mDefense = pDefense;
        mDesc = pDesc;
    }
    
    public int getHealth()
    {
        return currentHP;
    }
    
    public int getPower()
    {
        return mPower;
    }
    
    public int getDefense()
    {
        return mDefense;
    }
    
    //this is used in the damage calculation method in Game
    public void takeDamage(int pDamage)
    {
        if(pDamage > 0)
        {
            currentHP -= pDamage;
        }
    }
    
    public void print()
    {
        System.out.println("Monster: "+mDesc);
        System.out.println("Power: "+mPower+"   Defense: "+mDefense);
        System.out.println("Health: "+currentHP);
        System.out.println();
    }
    
    public boolean isMonster()
    {
        return true;
    }
    
    public void refreshHealth()
    {
        currentHP = maxHealth;
    }
    
}
