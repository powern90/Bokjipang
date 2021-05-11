package com.bluemango.bokjipang;

public class home_listview_item {
    private int no,image,idx;
    private String name, title;




    public void setNo(int no) {
        this.no = no ;
    }
    public void setBoard_name(String text) {
        this.name = text ;
    }
    public void setBoard_title(String text) {
        this.title = text ;
    }
    public void setBoard_image(int image){this.image=image;}
    public void setIdx(int idx) {
        this.idx = idx ;
    }

    public int getNo() {
        return no ;
    }
    public int getIdx() {
        return idx ;
    }
    public String getBoard_name() {
        return name;
    }
    public String getBoard_title() {
        return title ;
    }
    public int getBoard_image(){return this.image;}
}
