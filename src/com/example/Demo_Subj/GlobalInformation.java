package com.example.Demo_Subj;

/**
 * Created by thasti-note on 04.12.2014.
 * A static class to determine system information from everywhere
 */
public class GlobalInformation {
    private static int screenWidth;
    private static int screenHeight;
    private static int currentRoom;

    private GlobalInformation(){};

    public static int getScreenHeight() {
        return screenHeight;
    }

    public static void setScreenHeight(int screenHeight) {
        GlobalInformation.screenHeight = screenHeight;
    }

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static void setScreenWidth(int screenWidth) {
        GlobalInformation.screenWidth = screenWidth;
    }

    public static int getCurrentRoom() {
        return currentRoom;
    }

    public static void setCurrentRoom(int currentRoom) {
        GlobalInformation.currentRoom = currentRoom;
    }
}
