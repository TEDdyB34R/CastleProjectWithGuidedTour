
/**
 * Write a description of class Hero here.
 * 
 * @author Ethan Dowler
 * @version prototype Character class
 */
public class Hero extends Item
{
    private int maxHealth;
    private int exp;
    private int level;
    private int hPower;
    private int hDefense;

    /**
     * Constructor for objects of class Character
     */
    public Hero(String heroName, int Hp, int pow, int def)
    {
        super(heroName, pow, def);
        maxHealth = Hp;
        exp = 0;
        level = 1;
        hPower = pow;
        hDefense = def;
    }
    
        public void print()
    {
        System.out.print("Hero: ");
        super.print();
        System.out.println("Max Health: "+maxHealth);
        System.out.println();
    }
    
    public int getMaxHealth()
    {
        return maxHealth;
    }
    
    public int getPower()
    {
        return hPower;
    }

    public int getDefense()
    {
       return hDefense; 
    }
    
    public int getExp()
    {
        return exp;
    }
    
    public int getLevel()
    {
        return level;
    }
    
    public void addMaxHealth(int amount)
    {
        maxHealth += amount;
    }
    
    public void gainExp(int amount)
    {
        exp += amount;
        System.out.println("You have gained "+amount+" experience points from battle");
    }
    
    public void levelUp()
    {
        level += 1;
        System.out.println("Congratulations! You are now level "+level);
        System.out.println("     your wounds have magically been healed");
        System.out.println("You have become stronger and wiser in battle");
        System.out.println("HP+10   Power+1   Defense+1");
        maxHealth += 10;
        hPower++;
        hDefense++;
        System.out.println("MaxHP: "+getMaxHealth()+"   "+
                            "Power: "+getPower()+"   "+
                            "Defense: "+getDefense());
    }
}
