package com.example.aaaaaaaa_dodelat_makej;

import java.util.ArrayList;

public class Game {

    long date;
    String[] team1,team2;
    Data data;

    public Game(long date, String[] team1, String[] team2, Data data) {
        this.date = date;
        this.team1 = team1;
        this.team2 = team2;
        this.data = data;
    }

    static class Data {
        String title;
        int score1;
        int score2;

        public Data(String title, int team1, int team2) {
            this.title = title;
            this.score1 = team1;
            this.score2 = team2;
        }
    }

}
