package com.bluemango.bokjipang;

public class DataComu {
    private String title;
    private String content;
    private String  good_num;
    private String reply_num;
    private String id;
    private String datetime;

    public String getDatetime(){ return datetime;}
    public String getId(){return id;}
    public String getTitle(){return title;}
    public String getContent(){return content;}
    public String getGood_num(){return good_num;}
    public String getReply_num(){return reply_num;}

    public void setDatetime(String datetime){this.datetime = datetime;}
    public void setTitle(String title){this.title = title;}
    public void setContent(String content){this.content = content;}
    public void setGood_num(String good_num){this.good_num = good_num;}
    public void setReply_num(String reply_num){this.reply_num = reply_num;}
    public void setId(String id){this.id = id;}
}
