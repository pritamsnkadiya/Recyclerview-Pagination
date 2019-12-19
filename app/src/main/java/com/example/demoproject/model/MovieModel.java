package com.example.demoproject.model;

import java.io.Serializable;

public class MovieModel implements Serializable {
   public String title;
   public String rating;
   public String type;

    public MovieModel(String type) {
        this.type = type;
    }
}
