package com.bluemango.bokjipang;

import java.io.Serializable;

public class DataSup implements Serializable {
    private String title;
    private String content;
    private String date;
    private String idx;
    private String url;

    public String getTitle(){return title;}
    public String getContent(){return content;}
    public String getDate(){return date;}
    public String getIdx(){return idx;}
    public String getUrl(){return url;}

    public void setIdx(String idx){this.idx = idx;}
    public void setUrl(String url){this.url = url;}
    public void setTitle(String title){this.title = title;}
    public void setContent(String content){this.content = content;}
    public void setDate(String date){this.date = date;}

}

