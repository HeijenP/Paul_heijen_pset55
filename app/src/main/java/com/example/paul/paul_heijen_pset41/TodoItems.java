package com.example.paul.paul_heijen_pset41;

import java.io.Serializable;

public class TodoItems implements Serializable {

    private int id;
    private String itemName;
    private int listId;

    // con
    public TodoItems() {
    }

    public TodoItems(int id) {
        this.id = id;
    }

    public TodoItems(String itemName) {
        this.itemName = itemName;
    }

    public TodoItems(int id, String itemName) {
        this.id = id;
        this.itemName = itemName;
    }

    public TodoItems(int id, String itemName, int listId) {
        this.id =  id;
        this.itemName = itemName;
        this.listId = listId;
    }

    // set
    public void setId(int newId) {
        id = newId;
    }

    public void setItemName(String newItemName) {
        itemName = newItemName;
    }

    public void setListId(int newListId) {
        listId = newListId;
    }

    // get
    public long getId() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }

    public int getListId() {
        return listId;
    }
}
