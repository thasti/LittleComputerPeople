package com.example.Demo_Subj;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/*
 *Autor: Jürgen Ullmann
 */

public class World {

    private static TreeMap<Integer, Room> roomTreeMap = new TreeMap<Integer, Room>();
    private static TreeMap<Integer, Item> itemTreeMap = new TreeMap<Integer, Item>();
    private static List<Room> Rooms = new ArrayList<Room>();
    private static List<Item> Items = new ArrayList<Item>();

    /*******************************************************************************************
        Puts a room with the given ID in a TreeMap and fills the List Rooms
     ******************************************************************************************/
    public static void setRoom(int key, Room room){
        roomTreeMap.put(key, room);
        Rooms.add(room);
    }

    /*******************************************************************************************
     * From Documentation: A return value of null does not necessarily indicate that the map
     * contains no mapping for the key; it's also possible that the map explicitly maps the key to null.

     Returns a boolean value, whether the given ID has a room
     Parameter: ID of the room
     ******************************************************************************************/
    public static boolean assertRoomId(int id){
        return roomTreeMap.containsKey(id);
    }

    /*******************************************************************************************
        Returns a room that belongs to the given ID or null, if no room owns the given ID
        Parameter: ID of the room
     ******************************************************************************************/
    public static Room getRoomById(int id){
        return roomTreeMap.get(id);
    }


    /*******************************************************************************************
        Puts an object with the given ID in a TreeMap
     ******************************************************************************************/
    public static void setItem(int key, Item item){
        itemTreeMap.put(key, item);
        Items.add(item);
    }

    /*******************************************************************************************
     * From Documentation: A return value of null does not necessarily indicate that the map
     * contains no mapping for the key; it's also possible that the map explicitly maps the key to null.

     Returns a boolean value, whether the given ID has an object
     Parameter: ID of the Object
     ******************************************************************************************/
    public static boolean assertItemId(int id){
        return itemTreeMap.containsKey(id);
    }

    /*******************************************************************************************
        Returns an object that belongs to the given ID or null, if no object owns the given ID
        Parameter: ID of the Object
     ******************************************************************************************/
    public static Item getItemById(int id){
        return itemTreeMap.get(id);
    }

    public static List<Room> getAllRooms(){return Rooms;}

    public static List<Item> getAllItems(){return Items;}
}
