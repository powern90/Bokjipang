package com.bluemango.bokjipang;

public class mypost_listview_item {
    private String title, content, like, comment;

    public void set_title(String Title){this.title=Title;}
    public void set_content(String Content){this.content=Content;}
    public void set_like(String Like){this.like=Like;}
    public void set_comment(String Comment){this.comment=Comment;}
    public String get_title(){return this.title;}
    public String get_content(){return this.content;}
    public String get_like(){return this.like;}
    public String get_comment(){return this.comment;}
}
