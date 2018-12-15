package com.boilerplate.missionDatabase.mission;

public enum MissionType {
    DCS ("DCS",".miz"),
    ARMA ("ARMA",".pbo")
    ;


    private final String game;
    private final String extension;

    MissionType(String game, String extension) {
        this.game = game;
        this.extension = extension;
    }

    public String getGame() {
        return this.game;
    }

    public String getExtension(){
        return this.extension;
    }
}
