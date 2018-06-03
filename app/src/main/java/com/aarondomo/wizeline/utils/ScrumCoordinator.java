package com.aarondomo.wizeline.utils;

import java.util.List;
import java.util.Random;

public class ScrumCoordinator {

    private List<String> teamMembers;
    private Random random;

    public ScrumCoordinator(List<String> teamMembers) {
        this.teamMembers = teamMembers;

        random = new Random();
    }

    public String getNextToSpeak(){
        int position = getRandom();
        String next = teamMembers.get(position);
        teamMembers.remove(position);
        return next;
    }

    private int getRandom(){
        return random.nextInt((teamMembers.size()));
    }

    public boolean hasNext(){
        return !teamMembers.isEmpty();
    }
}
