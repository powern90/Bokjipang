package com.bluemango.bokjipang;

public class DataBoard {
    private int id;
    private String board;
    private String title;

    public int getId(){return id;}
    public String getTitle(){return title;}
    public String getBoard(){return board;}

    public void setBoard(String board){this.board = board;}
    public void setTitle(String title){this.title = title;}
    public void setId(int id){this.id = id;}
}
