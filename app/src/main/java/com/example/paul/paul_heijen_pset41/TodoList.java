package com.example.paul.paul_heijen_pset41;

import java.io.Serializable;
import java.util.ArrayList;


public class TodoList implements Serializable{
    private int id;
    private String listName;

    // constructors
    public TodoList() {
    }

    public TodoList(int id) {
        this.id = id;
    }

    public TodoList(String listName) {
        this.listName = listName;
    }

    public TodoList(int id, String listName) {
        this.id = id;
        this.listName = listName;
    }

    // setters
    public void setId(int newId) {
        id = newId;
    }

    public void setListName(String newListName) {
        listName = newListName;
    }

    // getters
    public long getId() {
        return id;
    }

    public String getListName() {
        return listName;
    }
}