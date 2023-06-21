package com.mygdx.game;

import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;

public class Room extends BaseActor{
    public static  final int NORTH = 0;
    public static final int SOUTH = 1;
    public static final int EAST = 2;
    public static final int WEST = 3;
    public static int[] directionArray = {NORTH,SOUTH, EAST, WEST};

    private Wall[] wallArray;
    private Room[] neighborArray;
    private boolean connected;
    private boolean visited;
    private Room previousRoom;
    public Room(float x, float y, Stage s) {
        super(x, y, s);
        loadTexture("assets/dirt.png");
        float w = getWidth();
        float h = getHeight();

        // t - wall thickness in pixels
        float t = 6;

        wallArray = new Wall[4];
        wallArray[SOUTH] = new Wall(x, y, w, t, s);
        wallArray[WEST] = new Wall(x, y, t, h, s);
        wallArray[NORTH] = new Wall(x, y + h - t, w, t, s);
        wallArray[EAST] = new Wall(x+w-t, y, t, h, s);

        neighborArray = new Room[4];
        connected = false;
        visited = false;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public void setNeighbor(int direction, Room neighbor) {
        neighborArray[direction] = neighbor;
    }

    public  boolean hasNeighbor(int direction) {
        return neighborArray[direction] != null;
    }

    public Room getNeighbor(int direction) {
        return  neighborArray[direction];
    }

    public  boolean hasWall(int direction) {
        return  wallArray[direction].getStage() != null;
    }

    public void removeWalls(int direction) {
        removeWallsBetween(neighborArray[direction]);
    }

    public  void removeWallsBetween(Room other) {
        if (other == neighborArray[NORTH]) {
            this.wallArray[NORTH].remove();
            other.wallArray[SOUTH].remove();
        }
        else if (other == neighborArray[SOUTH]) {
            this.wallArray[SOUTH].remove();
            other.wallArray[NORTH].remove();
        } else if (other == neighborArray[EAST]) {
            this.wallArray[EAST].remove();
            other.wallArray[WEST].remove();
        } else {
            this.wallArray[WEST].remove();
            other.wallArray[EAST].remove();
        }
    }

    public boolean hasUnconnectedNeighbor() {
        for (int direction : directionArray) {
            if (hasNeighbor(direction) && !getNeighbor(direction).isConnected())
                return true;
        }
        return false;
    }

    public Room getRandomUnconnectedNeighbor() {
        ArrayList<Integer> directionList = new ArrayList<>();
        for (int direction : directionArray) {
            if (hasNeighbor(direction) && !getNeighbor(direction).isConnected())
                directionList.add(direction);
        }
        int directionIndex = (int) Math.floor(Math.random() * directionList.size());
        int direction  = directionList.get(directionIndex);
        return getNeighbor(direction);
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public Room getPreviousRoom() {
        return previousRoom;
    }

    public void setPreviousRoom(Room previousRoom) {
        this.previousRoom = previousRoom;
    }

    public ArrayList<Room> unvisitedPathList() {
        ArrayList<Room> list = new ArrayList<>();
        for (int direction: directionArray) {
            if (hasNeighbor(direction) && !hasWall(direction) && !getNeighbor(direction).isVisited()) {
                list.add(getNeighbor(direction));
            }
        }

        return list;
    }
}
