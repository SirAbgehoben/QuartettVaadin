package org.abgehoben.QuartettVaadin;

import com.vaadin.flow.server.VaadinSession;

import java.util.ArrayList;

public class Player {

    private String name;

    private VaadinSession SessionId;
    private ArrayList<Integer> Deck;
    public Card card;

    public Player(ArrayList<Integer> Deck) {
        this.Deck = Deck;
        this.card = new Card();
    }
    public void setName (String name) {
        this.name = name;
    }

    public String getName () {
        return name;
    }
    public void setSessionId (VaadinSession SessionId) {
        this.SessionId = SessionId;
    }
    public VaadinSession getSessionId() {
        return SessionId;
    }
}
