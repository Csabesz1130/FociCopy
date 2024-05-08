package com.example.robotfoci;//package com.example.robotfoci;

import java.util.concurrent.ConcurrentHashMap;

public class GameState {
    private final ConcurrentHashMap<String, Player> players = new ConcurrentHashMap<>();
    private final Ball ball;

    public GameState() {
        this.ball = new Ball(400, 300, 0, 0); // Kezdeti pozíció és sebesség
    }

    public void addPlayer(String id, Player player) {
        players.put(id, player);
    }

    public Player getPlayerById(String id) {
        return players.get(id);
    }

    public ConcurrentHashMap<String, Player> getPlayers() {
        return players;
    }

    public Ball getBall() {
        return ball;
    }

}
