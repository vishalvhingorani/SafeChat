package com.hingorani.safechat.Model;

public class Chatlist {
    public String id;
    public long lastmessage;
    public boolean seen;

    public Chatlist(String id,long lastmessage, boolean seen) {
        this.id = id;
        this.lastmessage = lastmessage;
        this.seen = seen;
    }

    public Chatlist() {
    }

    public boolean getSeen(){ return seen;}

    public void setSeen(boolean seen){
        this.seen = seen;
    }

    public long getLastmessage(){ return lastmessage;}

    public void setLastmessage(long lastmessage){
        this.lastmessage = lastmessage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
