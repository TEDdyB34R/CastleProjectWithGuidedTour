import java.util.Set;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  For each existing exit, the room 
 * stores a reference to the neighboring room.
 * 
 * @author  Michael Kölling and David J. Barnes; modified by Ethan Dowler
 * @version 2011.08.08
 */

public class Room 
{
    private String description;
    private HashMap<String, Room> exits;        // stores exits of this room.
    private HashMap<String, Item> roomContents;

    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * @param description The room's description.
     */
    public Room(String description)
    {
        this.description = description;
        exits = new HashMap<String, Room>();
        roomContents = new HashMap<String, Item>();
    }

    /**
     * Define an exit from this room.
     * @param direction The direction of the exit.
     * @param neighbor  The room to which the exit leads.
     */
    public void setExit(String direction, Room neighbor) 
    {
        exits.put(direction, neighbor);
    }

    /**
     * @return The short description of the room
     * (the one that was defined in the constructor).
     */
    public String getShortDescription()
    {
        return description;
    }

    /**
     * Return a description of the room in the form:
     *     You are in the kitchen.
     *     Exits: north west
     * @return A long description of this room
     */
    public String getLongDescription()
    {
        return "You are in " + description + "\n" + getExitString() +"\n"+ getRoomContentsDesc();
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     * @return Details of the room's exits.
     */
    public String getExitString()
    {
        String returnString = "Exits:";
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            returnString += " " + exit;
        }
        return returnString;
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * @param direction The exit's direction.
     * @return The room in the given direction.
     */
    public Room getExit(String direction) 
    {
        return exits.get(direction);
    }

    public void pull(Command command)
    {
        System.out.println("Do What?");
    }

    public void changeDesc(String newDescription)
    {
        description = newDescription;
    }

    //**********************************************************************************
    //below this line are the methods I have added

    public void addItem(String key, Item newItem)
    {
        roomContents.put(key, newItem);
    }
    
    public void removeItem(String key)
    {
        roomContents.remove(key);
    }

    public String getRoomContentsDesc()
    {
        String returnString = "";
        if(roomContents != null)
        {
            Iterator it = roomContents.entrySet().iterator();

            while(it.hasNext())
            {
                Map.Entry entry = (Map.Entry) it.next();
                returnString += "There is a "+entry.getKey()+" in the room \n";
            }
        }
        return returnString;
    }

    public boolean hasMonster()
    {
        if(roomContents.isEmpty())
        {
            return false;
        } 
        
        
        for(Item i : roomContents.values())
        {
            if(i.isMonster())
            {
                return true;
            }
        }

        return false;
    }
    
    public boolean hasWeapon()
    {
        if(roomContents.isEmpty())
        {
            return false;
        } 
        
        
        for(Item i : roomContents.values())
        {
            if(i.isWeapon())
            {
                return true;
            }
        }

        return false;
    }
    
    public Weapon getWeapon()
    {
        for(Item i : roomContents.values())
        {
            if(i.isWeapon())
            {
                return (Weapon) i;
            }
        }
        return null;
    }
    
    public Monster getMonster()
    {
        for(Item i : roomContents.values())
        {
            if(i.isMonster())
            {
                return (Monster) i;
            }
        }
        return null;
    }
    
    public boolean isBossRoom()
    {
        return false;
    }

}



