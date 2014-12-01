import java.util.Random;
import java.util.HashMap;
/**
 * This is where you play the Game!
 * 
 * @author Ethan Dowler
 * @version this is where i'm working on our main code
 */
public class Game
{

    private Hero thisHero;
    private Weapon thisWeapon;
    private Monster thisMonster;
    private Room thisRoom;

    private int currentHealth;
    private Weapon currentWeapon;
    private Room currentRoom;
    private HashMap<String, Weapon> inventory;

    private Random rand;
    public static HashMap<String, Weapon> armory;
    private HashMap<String, Monster> monsterpedia;

    private Parser parser;
    private ParserWithFileInput parserWithFileInput;

    /*rooms are named based on which square they fall on in the grid
            and what "floor" they are on
            grid is a 5x5 square using letters a-e for the first row, etc */
    //rooms on floor 1
    static Room b1, c1, e1, g1, h1, i1, j1, k1, l1, n1, p1, q1, r1, s1, t1, u1;
    //rooms on floor 2
    static Room d2, e2, i2, j2, k2, l2, m2, n2, q2, r2, w2, x2;
    //rooms on floor 3
    static Room a3, f3, g3, h3, i3, l3, q3, r3, s3, x3;

    public Game()
    {
        //creates an armory HashMap full of weapons
        armory = new HashMap<String, Weapon>();
        createArmory();

        inventory = new HashMap<String, Weapon>();
        inventory.put(armory.get("gloves").getDesc(), armory.get("gloves"));

        //creates our monsters in a HashMap
        monsterpedia = new HashMap<String, Monster>();
        createMonsterpedia();
        

        createRooms();
        createExits();
        fillRooms();
        currentRoom = u1;
        parser = new Parser();
        parserWithFileInput = new ParserWithFileInput();
    }
    //****************************************
    public void playWithFileInput() 
    {            
        chooseHero("knight");
        currentHealth = thisHero.getMaxHealth();
        currentWeapon = armory.get("gloves");
        rand = new Random(1);
        printWelcome();
        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.     
        boolean finished = false;
        while (! finished) {
            Command command = parserWithFileInput.getCommand();
            finished = processCommand(command);
        }
        if(currentRoom.getLongDescription().contains("princess"))
        {
            System.out.println("Congratulations! You have rescued the princess and have beaten the game!");
        }
        System.out.println("Thank you for playing.  Good bye.");
    }
    //*****************************************
    
    private void attack()
    {
        if(!currentRoom.hasMonster())
        {
            System.out.println("There is nothing in the room to attack...");
            return;
        }
        thisMonster = currentRoom.getMonster();
        System.out.println("You valiantly attack the "+thisMonster.getDesc()+"!");

        //damage calculations
        int damageToHero = (thisMonster.getPower()-thisHero.getDefense()-currentWeapon.getDefense());
        int damageToMonster = (thisHero.getPower()+currentWeapon.getPower()-thisMonster.getDefense());

        if(damageToHero == 0 && damageToMonster == 0)
        {
            System.out.println("You and the monster have come to a draw \n"
            +"and you barely escaped with your life");
            currentRoom = u1;
            return;
        }
        //loop that will run until the monster is dead
        while(thisMonster.getHealth() > 0)
        {

            //Battle results; prints the damage Hero deals
            if(damageToMonster > 0)
            {
                thisMonster.takeDamage(damageToMonster);
                System.out.println("you deal "+damageToMonster+" damage");
            }
            else
            {
                System.out.println("You deal 0 damage to the monster");
            }

            //Hero takes damage
            if(damageToHero > 0)
            {
                currentHealth -= damageToHero;
                System.out.println("     and take "+damageToHero+" damage");
            }
            else
            {
                System.out.println("     and take 0 damage");
            }

            //if your health drops below zero, you lose
            if(currentHealth <= 0)
            {
                System.out.println();
                System.out.println("You have been slain! Better luck next time...");
                System.out.println("GAME OVER!!!! MUAHAHAHAHAHAHAH!!!!");
                return;
                //we need to add an "end game" methond here or something
            }
        }

        //monster is removed from the room
        thisMonster.refreshHealth();
        currentRoom.removeItem(thisMonster.getDesc());
        thisMonster = null;

        //victory condition
        System.out.println("The monster has been slain!");
        System.out.println("your Hp = "+currentHealth);
        thisHero.gainExp(10); //every monster gives 10 exp for now
        checkLevelUp(thisHero.getExp());
        System.out.println();
        System.out.println(currentRoom.getLongDescription());

    }

    //this is a method used with potions, elixers, etc. if we add them
    private void heal(int healAmount)
    {
        currentHealth += healAmount;
        int excessHeal = currentHealth - thisHero.getMaxHealth();
        if(currentHealth <= thisHero.getMaxHealth())
        {
            System.out.println("You have been healed "+healAmount+" health!");
        }
        if(currentHealth > thisHero.getMaxHealth())
        {
            currentHealth = thisHero.getMaxHealth();
            System.out.println("You have been healed "+excessHeal+" health!");
        }
        System.out.println("New Hp: "+currentHealth);
    }

    //this method will be used to randomly spawn a monster in a room
    private void spawn(String potentialMonster)
    {
        int x = rand.nextInt(4);
        if(x == 0)
        {
            thisMonster = monsterpedia.get(potentialMonster);
            currentRoom.addItem(thisMonster.getDesc(), thisMonster);
        }
    }

    //this will let the player level up when they hit certain exp milestones
    private void checkLevelUp(int experiencePoints)
    {
        switch(experiencePoints)
        {
            case 30:
            case 60:
            case 100:
            case 140:
            case 180:
            case 230:
            case 280:
            thisHero.levelUp();
            currentHealth = thisHero.getMaxHealth(); //heals hero on level up
            break;
            default:
            return;
        }
    }

    //this method sets 'thisHero' to the hero of the players choice
    private void chooseHero(String heroType)
    {
        switch(heroType.toLowerCase())
        {
            case "rouge":
            thisHero = new Hero("Rouge",90, 13, 5);
            break;
            case "knight":
            thisHero = new Hero("Knight",100, 10, 7);
            break;
            case "paladin":
            thisHero = new Hero("Paladin", 110, 7, 9);
            break;
            case "nikhil":
            thisHero = new Hero("Nikhil, destroyer of worlds",300, 100, 50);
            break;
            default:
            thisHero = new Hero("Peasant",50, 0, 0);
        }
    }

    //Welcome message..
    private void printWelcome()
    {
        System.out.println("Welcome to the World of the Dark Castle!");
        System.out.println("There are many secrets to discover");
        System.out.println("         and many adventures to be had");
        System.out.println("May you find great glory in the destiny that awaits you");
        System.out.println();
        if(thisHero.getDesc().equals("Peasant"))
        {
            System.out.println("You must be an illiterate peasant who can't read");
            thisHero.print();
            System.out.println("This Hero is very weak. :(");
            System.out.println("You may want to restart your game and choose a stronger Hero class;");
            System.out.println("unless you want to die a quick painful death...");
            System.out.println("Available Hero classes are written in the README file");

        }
        else
        {
            System.out.println("Armed with only your "+currentWeapon.getDesc());
            System.out.println("Your goal is to rescue the princess");
            System.out.println("               at the top of the castle");
            System.out.println("Your base stats are: ");
            thisHero.print();

        }
        System.out.println("");
        System.out.println("You have just enterd the Dark Castle, and you begin to look around");
        System.out.println(currentRoom.getExitString());
    }

    //this method will create a list of weapons/shields we can pull from
    private void createArmory() 
    {
        //default weapon
        thisWeapon = new Weapon("gloves", 0, 0);
        armory.put(thisWeapon.getDesc(),thisWeapon);

        //offensive weapons
        thisWeapon = new Weapon("knife", 2, 0);
        armory.put(thisWeapon.getDesc(),thisWeapon);
        thisWeapon = new Weapon("sword", 5, 0);
        armory.put(thisWeapon.getDesc(),thisWeapon);
        thisWeapon = new Weapon("crossbow", 8, 4);
        armory.put(thisWeapon.getDesc(),thisWeapon);
        thisWeapon = new Weapon("golden sword", 15, 6);
        armory.put(thisWeapon.getDesc(),thisWeapon);

        //defensive weapons
        thisWeapon = new Weapon("body armor", 4, 8);
        armory.put(thisWeapon.getDesc(),thisWeapon);
        thisWeapon = new Weapon("golden aegis", 7, 15);
        armory.put(thisWeapon.getDesc(),thisWeapon);

    }

    private void createMonsterpedia()
    {
        //Regular Monsters

        thisMonster = new Monster("whisp", 40, 9, 5);
        monsterpedia.put(thisMonster.getDesc(), thisMonster);
        thisMonster = new Monster("giant roach", 40, 10, 6);
        monsterpedia.put(thisMonster.getDesc(), thisMonster);
        thisMonster = new Monster("serpent", 40, 11, 4);
        monsterpedia.put(thisMonster.getDesc(), thisMonster);
        thisMonster = new Monster("troll", 50, 15, 3);
        monsterpedia.put(thisMonster.getDesc(), thisMonster);

        //Boss Monsters
        thisMonster = new Monster("evil witch", 65, 18, 8);
        monsterpedia.put(thisMonster.getDesc(), thisMonster);
        thisMonster = new Monster("zombie warrior", 75, 23, 10);
        monsterpedia.put(thisMonster.getDesc(), thisMonster);
        thisMonster = new Monster("mutant tortoise", 85, 28, 12);
        monsterpedia.put(thisMonster.getDesc(), thisMonster);
    }

    //****************************************************************************
    //the methods below this line are from the Zuul game and are under construction
    /**
     * /**
     * Select your Hero class. Avaiable classes include:
     * className     Health         Power          Defense
     * rouge              90             13             5
     * knight             100            10             7
     * paladin            110            7              9
     */
    public void play(String heroType) 
    {            
        //I modified the printWelcome() (incomplete) and added chooseHero()
        chooseHero(heroType);
        currentHealth = thisHero.getMaxHealth();
        currentWeapon = armory.get("gloves");
        rand = new Random();
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.       
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        else if (commandWord.equals("attack")) {
            if(currentRoom.hasMonster())
            {
                attack();
                if(currentHealth <= 0)
                {
                    wantToQuit = true;
                }
            }
            else
            {
                System.out.println("attack what? the air?");
            }
        }
        else if (commandWord.equals("pull")) {
            currentRoom.pull(command); //still need to add pull method to leverRoom class
        }
        else if (commandWord.equals("pickup")) {

            Weapon newWeapon = currentRoom.getWeapon();
            if(newWeapon == null)
            {
                System.out.println("There is nothing to pickup!");
            }
            else
            {
                System.out.println("You have picked up the "+newWeapon.getDesc());
                currentRoom.removeItem(newWeapon.getDesc());
                System.out.println("     and have dropped your "+currentWeapon.getDesc());
                currentRoom.addItem(currentWeapon.getDesc(), currentWeapon);
                currentWeapon = newWeapon;
            }

        }
        else if (commandWord.equals("run")) {
            if(currentRoom.isBossRoom())
            {
                System.out.println("You cannot escape this monster! It's too strong!");
            }
            else
            {
                int x = rand.nextInt(5);
                if(x == 0)
                {
                    System.out.println("you were able to escape the monster; \n quick, pick an exit!");
                    currentRoom.removeItem(thisMonster.getDesc());
                    System.out.println(currentRoom.getLongDescription());
                }
                else
                {
                    System.out.println("you failed to escape and have taken 3 damage from the monster");
                    currentHealth -= 3;
                    if(currentHealth <= 0)
                    {
                        wantToQuit = true;
                    }
                }
            }
        }

        if(currentRoom == a3)
        {
            wantToQuit = true;
        }
        // else command not recognised.
        return wantToQuit;
    }

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You must hurry to save the princess!");
        System.out.println("Here are your available commands");
        parser.showCommands();
    }

    /** 
     * Try to in to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {

        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        if(currentRoom.hasMonster())
        {
            System.out.println("You cannot leave; a " + currentRoom.getMonster().getDesc() + "is blocking the door!");
            System.out.println("    However, you MAY be able to run away...");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) 
        {
            System.out.println("There is no door!");
            return;
        }

        currentRoom = nextRoom;

        if(currentRoom.isBossRoom())
        {

        }
        else
        {
            int x = rand.nextInt(4);
            switch(x)
            {
                case 0:
                spawn("whisp");
                break;
                case 1:
                spawn("giant roach");
                break;
                case 2:
                spawn("serpent");
                break;
                case 3:
                spawn("troll");
                break;
            }

        }

        if(command.getSecondWord() == "up")
        {
            System.out.println("You have walked up the stairs to the next floor");
        }
        if(command.getSecondWord() == "down")
        {
            System.out.println("You have walked down the stairs to the lower floor");
        }
        System.out.println(currentRoom.getLongDescription());
        if(currentRoom.hasMonster())
        {
            thisMonster = currentRoom.getMonster();
            thisMonster.print();
        }

    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }

    /**
     * Create all the rooms.
     */
    private void createRooms()
    {
        createFloor1();
        createFloor2();
        createFloor3();
    }

    private void createFloor1()
    {
        //floor one rooms
        b1 = new Room("a room with several bookshelves \n if only you had time to read...");
        c1 = new Room("a room with...what's that smell? Eww!");
        e1 = new LeverRoom("a plain room with no distinctive features \n except... \n there is a mysterious lever in the corner...");
        g1 = new Room("the northwestern hallway");
        h1 = new Room("the northern hallway");
        i1 = new Room("the northeastern hallway \n you see a menacing door to the south");
        j1 = new Room("a room with a window \n you can see dead plants outside");
        k1 = new Room("a room with a gargoyle");
        l1 = new Room("a small corridor");
        n1 = new BossRoom("a large open room with blood on every wall \n there is a spiral staircase that leads up");
        p1 = new Room("a room with a chandalier");
        q1 = new Room("the southwestern hallway");
        r1 = new Room("the southern hallway");
        s1 = new Room("the southeastern hallway \n you see a menacing door to the north");
        t1 = new Room("a room with a window \n you can see the dreary weather outside");
        u1 = new Room("the Castle foyer");
    }

    private void createFloor2() {
        //create rooms
        d2 = new Room("a room painted brown");
        e2 = new Room("a room painted white \n that doesn't seem to have any unique qualities");
        i2 = new Room("a skinny corridor");
        j2 = new Room("a room painted purple");
        k2 = new Room("a room painted grey");
        l2 = new Room("a room painted green");
        m2 = new Room("a room painted blue");
        n2 = new Room("a room with a staircase leading down");
        q2 = new Room("a room painted red");
        r2 = new Room("a room painted yellow");
        w2 = new Room("a room painted black \n you see a menacing door to the east");
        x2 = new BossRoom("a circular room with much debris and rubble \n you can see a spiral staircase leading up");
    }

    private void createFloor3() {
        a3 = new BossRoom("a room with piles of gold \n and a beautiful princess inside!");
        f3 = new BossRoom("a room full of dead animals and rotting corpses");
        g3 = new Room("an empty room that inspires curiosity \n you see a menacing door to the west");
        h3 = new Room("a curious room full emptiness");
        i3 = new Room("hole, fall"); //change
        l3 = new Room("an empty corridor");
        q3 = new Room("a room of curious emptiness");
        r3 = new Room("a room that's curiously empty");
        s3 = new Room("a curiously empty room");
        x3 = new Room("a room with a staircase leading down");
    }

    private void createExits() {
        //floor 1 exits
        u1.setExit("north", p1);
        p1.setExit("north", k1);
        p1.setExit("east", q1);
        p1.setExit("south", u1);
        k1.setExit("east", l1);
        k1.setExit("south", p1);
        q1.setExit("north", l1);
        q1.setExit("east", r1);
        q1.setExit("west", p1);
        l1.setExit("north", g1);
        l1.setExit("south", q1);
        l1.setExit("west", k1);
        g1.setExit("north", b1);
        g1.setExit("east", h1);
        g1.setExit("south", l1);
        b1.setExit("east", c1);
        b1.setExit("south", g1);
        r1.setExit("east", s1);
        r1.setExit("west", q1);
        h1.setExit("east", i1);
        h1.setExit("north",c1);
        h1.setExit("west", g1);
        c1.setExit("south", h1);
        c1.setExit("west", b1);
        s1.setExit("east", t1);
        s1.setExit("north",n1);
        s1.setExit("west", r1);
        n1.setExit("south", s1);
        n1.setExit("north", i1);
        n1.setExit("up", n2);
        i1.setExit("west", h1);
        i1.setExit("south", n1);
        i1.setExit("east", j1);
        t1.setExit("west", s1);
        j1.setExit("west", i1);
        j1.setExit("north", e1);
        e1.setExit("south", j1);

        //floor 2 exits
        q2.setExit("north", l2);
        q2.setExit("east", r2);
        l2.setExit("south", q2);
        l2.setExit("east", m2);
        l2.setExit("west", k2);
        k2.setExit("east", l2);
        w2.setExit("north", r2);
        w2.setExit("east", x2);
        r2.setExit("north", m2);
        r2.setExit("west", q2);
        r2.setExit("south", w2);
        m2.setExit("south", r2);
        m2.setExit("east", n2);
        m2.setExit("west", l2);
        x2.setExit("west", w2);
        x2.setExit("up", x3);
        n2.setExit("west", m2);
        n2.setExit("down", n1);
        n2.setExit("north", i2);
        i2.setExit("north", d2);
        i2.setExit("south", n2);
        i2.setExit("east", j2);
        d2.setExit("south", i2);
        d2.setExit("east", e2);
        j2.setExit("west", i2);
        j2.setExit("north", e2);
        e2.setExit("west", d2);
        e2.setExit("south", j2);

        //floor 3 exits
        a3.setExit("south", f3);
        f3.setExit("east", g3);
        f3.setExit("north", a3);
        g3.setExit("east", h3);
        g3.setExit("south", l3);
        g3.setExit("west", f3);
        h3.setExit("west", g3);
        h3.setExit("east", i3);
        i3.setExit("west", h3);
        l3.setExit("south", q3);
        l3.setExit("north", g3);
        q3.setExit("east", r3);
        q3.setExit("north", l3);
        r3.setExit("east", s3);
        r3.setExit("west", q3);
        s3.setExit("south", x3);
        s3.setExit("west", r3);
        x3.setExit("north", s3);
        x3.setExit("down", x2);
    }

    private void fillRooms()
    {
        //floor 1 weapons
        p1.addItem(armory.get("knife").getDesc(),armory.get("knife"));
        t1.addItem(armory.get("sword").getDesc(),armory.get("sword"));
        c1.addItem(armory.get("body armor").getDesc(),armory.get("body armor"));

        //floor 2 weapons
        d2.addItem(armory.get("golden aegis").getDesc(),armory.get("golden aegis"));
        k2.addItem(armory.get("crossbow").getDesc(),armory.get("crossbow"));

        //floor 3 weapons. and the princess...
        i3.addItem(armory.get("golden sword").getDesc(),armory.get("golden sword"));

        //adding monster to boss rooms
        n1.addItem(monsterpedia.get("evil witch").getDesc(),monsterpedia.get("evil witch"));
        x2.addItem(monsterpedia.get("zombie warrior").getDesc(),monsterpedia.get("zombie warrior"));
        f3.addItem(monsterpedia.get("mutant tortoise").getDesc(),monsterpedia.get("mutant tortoise"));
    }
}
