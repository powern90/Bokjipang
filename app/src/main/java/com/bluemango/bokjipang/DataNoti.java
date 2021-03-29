package com.bluemango.bokjipang;

public class DataNoti {
        private String title;
        private String content;
        private String type;
        private boolean read;

        public String getTitle(){return title;}
        public String getContent(){return content;}
        public String getType(){return type;}
        public boolean getread(){return read;}

        public void setRead(boolean read){this.read = read;}
        public void setTitle(String title){this.title = title;}
        public void setContent(String content){this.content = content;}
        public void setType(String type){this.type = type;}


}
