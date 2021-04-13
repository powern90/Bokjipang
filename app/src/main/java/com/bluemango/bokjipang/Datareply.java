package com.bluemango.bokjipang;

public class Datareply {
    private String nickname;
    private String content;
    private String date;
    private String rereply;

    public String getRereply(){return  rereply;}
    public String getNickname(){return nickname;}
    public String getContent(){return content;}
    public String getDate(){return date;}

    public void setRereply(String i){this.rereply = i;}
    public void setNickname(String nickname){this.nickname = nickname;}
    public void setDate(String date){this.date = date;}
    public void setContent(String content){this.content = content;}

}
