package com.example.android_homeassignment2.entities;

public class User {
    private String name;
    private int distance;
    private int numCoins;
    private Location location;

    public User() {
    }

    public User(String name, int distance, int numCoins, Location location) {
        this.name = name;
        this.distance = distance;
        this.numCoins = numCoins;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public int getDistance() {
        return distance;
    }

    public User setDistance(int distance) {
        this.distance = distance;
        return this;
    }

    public int getNumCoins() {
        return numCoins;
    }

    public User setNumCoins(int numCoins) {
        this.numCoins = numCoins;
        return this;
    }

    public double calculateScore(){
        return getDistance() * 0.2 + getNumCoins() * 0.8;
    }

    public Location getLocation() {
        return location;
    }

    public User setLocation(Location location) {
        this.location = location;
        return this;
    }
}
