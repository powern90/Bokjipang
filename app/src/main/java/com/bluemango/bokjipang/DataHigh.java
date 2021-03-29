package com.bluemango.bokjipang;

public class DataHigh {

    private int id;
    private int like;
    private String category;
    private String title;
    private String content;


    public int getId(){return id;}
    public int getLike(){return like;}
    public String getTitle(){return title;}
    public String getCategory(){return category;}
    public String getContent(){return content;}

    public void setLike(int like){this.like = like;}
    public void setCategory(String category){this.category = category;}
    public void setContent(String content){this.content = content;}
    public void setTitle(String title){this.title = title;}
    public void setId(int id){this.id = id;}
}
