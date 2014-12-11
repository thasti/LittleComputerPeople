package com.example.Demo_Subj;

import java.util.TreeMap;


public class World {

    private static TreeMap<Integer, Room> roomTreeMap = new TreeMap<Integer, Room>();
    private static TreeMap<Integer, Object> objectTreeMap = new TreeMap<Integer, Object>();

    /*******************************************************************************************
        Puts a room with the given ID in a TreeMap
     ******************************************************************************************/
    public static void setRoom(int key, Room room){
        roomTreeMap.put(key, room);
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
    public static void setObject(int key, Object obj){
        objectTreeMap.put(key, obj);
    }

    /*******************************************************************************************
     * From Documentation: A return value of null does not necessarily indicate that the map
     * contains no mapping for the key; it's also possible that the map explicitly maps the key to null.

     Returns a boolean value, whether the given ID has an object
     Parameter: ID of the Object
     ******************************************************************************************/
    public static boolean assertObjectId(int id){
        return objectTreeMap.containsKey(id);
    }

    /*******************************************************************************************
        Returns an object that belongs to the given ID or null, if no object owns the given ID
        Parameter: ID of the Object
     ******************************************************************************************/
    public static Object getObjectById(int id){
        return objectTreeMap.get(id);
    }
}
