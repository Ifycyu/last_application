package com.jnu.last_application.data;

import java.io.Serializable;

public class BookItem implements Serializable {
    private String title;
    private int image_R_id;
    public BookItem(String name, int id){
        title=name;
        image_R_id=id;
    }
    public String getTitle(){
        return title;
    }
    public int getCoverResourceId(){
        return image_R_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
