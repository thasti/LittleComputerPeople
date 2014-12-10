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
        Returns a room that belongs to the given ID or null, if no room owns the given ID
        Parameter: ID of the room
     ******************************************************************************************/
    public static Room getRoomById(int id){
        Room room = null;
        if(roomTreeMap.containsKey(id)){
            room = roomTreeMap.get(id);
        }
        return room;
    }


    /*******************************************************************************************
        Puts an object with the given ID in a TreeMap
     ******************************************************************************************/
    public static void setObject(int key, Object obj){
        objectTreeMap.put(key, obj);
    }

    /*******************************************************************************************
        Returns an object that belongs to the given ID or null, if no object owns the given ID
        Parameter: ID of the Object
     ******************************************************************************************/
    public static Object getObjectById(int id){
        Object obj = null;
        if(objectTreeMap.containsKey(id)){
            obj = objectTreeMap.get(id);
        }
        return obj;
    }
}
