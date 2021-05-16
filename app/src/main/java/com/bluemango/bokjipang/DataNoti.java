package com.bluemango.bokjipang;

public class DataNoti {
        private String title;
        private String content;
        private String type;
        private boolean read;
        private String idx;
        private String datetime;

        public String getTitle(){return title;}
        public String getContent(){return content;}
        public String getType(){return type;}
        public boolean getread(){return read;}
        public String getIdx(){return idx;}
        public String getDatetime(){return datetime;}


        public void setIdx(String idx){this.idx = idx;}
        public void setDatetime(String datetime){this.datetime = datetime;}
        public void setRead(boolean read){this.read = read;}
        public void setTitle(String title){this.title = title;}
        public void setContent(String content){this.content = content;}
        public void setType(String type){this.type = type;}


}
