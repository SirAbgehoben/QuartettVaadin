package org.abgehoben.QuartettVaadin;

import java.util.ArrayList;

public class Player {

    private String name;

    private int SessionId;
    private ArrayList<Integer> Deck;

    public Player(ArrayList<Integer> Deck) {
        this.Deck = Deck;
    }

}
