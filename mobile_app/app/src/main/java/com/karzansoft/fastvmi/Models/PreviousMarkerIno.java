package com.karzansoft.fastvmi.Models;

/**
 * Created by Yasir on 4/19/2016.
 */
public class PreviousMarkerIno {
    int x1,y1,x2,y2, type;
    String id;

    public boolean isDeleteable() {
        return isDeleteable;
    }

    public void setIsDeleteable(boolean isDeleteable) {
        this.isDeleteable = isDeleteable;
    }

    boolean isDeleteable;

    public PreviousMarkerIno(int x1, int y1, int x2, int y2, int type, String id) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.type = type;
        this.id = id;
    }

    public PreviousMarkerIno(int x1, int y1, int x2, int y2, int type, String id, boolean isDeleteable) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.type = type;
        this.id = id;
        this.isDeleteable = isDeleteable;
    }

    public int getX1() {
        return x1;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public int getY1() {
        return y1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public int getX2() {
        return x2;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public int getY2() {
        return y2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
