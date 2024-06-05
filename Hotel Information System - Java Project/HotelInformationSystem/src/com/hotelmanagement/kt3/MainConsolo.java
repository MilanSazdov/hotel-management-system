package com.hotelmanagement.kt3;

import com.hotelmanagement.controller.DataController;
import com.hotelmanagement.controller.RoomTypeController;
import com.hotelmanagement.model.RoomCategory;
import com.hotelmanagement.model.RoomType;

public class MainConsolo {
    public static void main(String[] args) {
        System.out.println("Welcome to the Hotel !!!");
        DataController dataController = DataController.getInstance();
        dataController.loadData();
        
        
        
        Menu menu = new Menu();
        Menu.main(args);
    }
}
