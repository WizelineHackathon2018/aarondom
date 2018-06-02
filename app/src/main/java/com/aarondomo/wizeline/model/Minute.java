package com.aarondomo.wizeline.model;

import java.util.List;
import java.util.Map;

public class Minute {

    private String date;
    private String hour;
    private String team;
    private List<String> members;
    private Map<String, String> userUpdate;
    private String post;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public Map<String, String> getUserUpdate() {
        return userUpdate;
    }

    public void setUserUpdate(Map<String, String> userUpdate) {
        this.userUpdate = userUpdate;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

}
