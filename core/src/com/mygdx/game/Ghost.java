package com.mygdx.game;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.util.ArrayList;

public class Ghost extends BaseActor{
    public float speed = 60;
    public Ghost(float x, float y, Stage s) {
        super(x, y, s);
        loadAnimationFromSheet("assets/ghost.png", 1, 3, 0.2f, true);
        setOpacity(0.8f);
    }


    public void findPath(Room startRoom, Room targetRoom) {
        Room currentRoom = startRoom;
        ArrayList<Room> roomList = new ArrayList<>();
        currentRoom.setPreviousRoom(null);
        currentRoom.setVisited(true);
        roomList.add(currentRoom);

        while (roomList.size() > 0) {
            currentRoom = roomList.remove(0);
            for (Room nextRoom : currentRoom.unvisitedPathList()) {
                nextRoom.setPreviousRoom(currentRoom);
                if (nextRoom == targetRoom) {
                    roomList.clear();
                    break;
                } else  {
                    nextRoom.setVisited(true);
                    roomList.add(nextRoom);
                }
            }
        }

        // create a list of rooms corresponding to shortest path
        ArrayList<Room> pathRoomList = new ArrayList<>();

        currentRoom = targetRoom;
        while (currentRoom != null) {
            pathRoomList.add(0, currentRoom);
            currentRoom = currentRoom.getPreviousRoom();
        }

        int maxStepCount = 2;

        for (int i = 0; i < pathRoomList.size(); i++) {
            if (i == maxStepCount)
                break;
            Room nextRoom = pathRoomList.get(i);
            Action move = Actions.moveTo(nextRoom.getX(), nextRoom.getY(), 64 / speed);
            addAction(move);
        }
    }
}
