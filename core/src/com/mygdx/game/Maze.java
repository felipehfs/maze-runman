package com.mygdx.game;

import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;

public class Maze {
    private Room[][] roomGrid;

    private final int roomCountX = 12;
    private final int roomCountY = 10;
    private final int roomWidth = 64;
    private final int roomHeight = 64;

   public Maze(Stage stage) {
       roomGrid = new Room[roomCountX][roomCountY];

       for (int gridY = 0; gridY < roomCountY; gridY++) {
           for (int gridX = 0; gridX < roomCountX; gridX++) {
               float pixelX = gridX * roomWidth;
               float pixelY = gridY * roomHeight;

               Room room = new Room(pixelX, pixelY, stage);

               roomGrid[gridX][gridY] = room;
           }
       }

       // neighbor relations
       for (int gridY = 0; gridY < roomCountY; gridY++) {
           for (int gridX = 0; gridX < roomCountX; gridX++) {
               Room room = roomGrid[gridX][gridY];
               if (gridY > 0)
                   room.setNeighbor(Room.SOUTH, roomGrid[gridX][gridY - 1]);
               if (gridY < roomCountY - 1)
                   room.setNeighbor(Room.NORTH, roomGrid[gridX][gridY + 1]);
               if (gridX > 0)
                   room.setNeighbor(Room.WEST, roomGrid[gridX - 1][gridY]);
               if (gridX < roomCountX - 1)
                   room.setNeighbor(Room.EAST, roomGrid[gridX + 1][gridY]);
           }
       }

       ArrayList<Room> activeRoomList = new ArrayList<>();

       Room currentRoom = roomGrid[0][0];
       currentRoom.setConnected(true);
       activeRoomList.add(0, currentRoom);

       float branchProbality = 0.5f;

       while (activeRoomList.size() > 0) {
           if (Math.random() < branchProbality) {
               int randomIndex = (int) (Math.random() * activeRoomList.size());
               currentRoom = activeRoomList.get(randomIndex);
           } else {
               currentRoom = activeRoomList.get(activeRoomList.size() - 1);
           }

           if (currentRoom.hasUnconnectedNeighbor()) {
               Room nextRoom = currentRoom.getRandomUnconnectedNeighbor();
               currentRoom.removeWallsBetween(nextRoom);
               nextRoom.setConnected(true);
               activeRoomList.add(0, nextRoom);
           } else {
               activeRoomList.remove(currentRoom);
           }
       }

       int wallsToremove = 24;
       while (wallsToremove > 0) {
           int gridX = (int) Math.floor(Math.random() * roomCountX);
           int gridY = (int) Math.floor(Math.random() * roomCountY);
           int direction = (int) Math.floor(Math.random() * 4);

           Room room = roomGrid[gridX][gridY];
           if (room.hasNeighbor(direction) && room.hasWall(direction)) {
                room.removeWalls(direction);
                wallsToremove--;
           }
       }
   }

   public Room getRoom(int gridX, int gridY) {
       return roomGrid[gridX][gridY];
   }

   public Room getRoom(BaseActor actor) {
       int gridX = (int)Math.round(actor.getX() / roomWidth);
       int gridY = (int)Math.round(actor.getY() / roomHeight);
       return getRoom(gridX, gridY);
   }

   public void resetRooms() {
       for (int gridY = 0; gridY < roomCountY; gridY++) {
           for (int gridX = 0; gridX < roomCountX; gridX++) {
               roomGrid[gridX][gridY].setVisited(false);
               roomGrid[gridX][gridY].setPreviousRoom(null);
           }
       }
   }
}
