package org.abgehoben.QuartettVaadin;

import com.vaadin.flow.server.VaadinSession;

import java.util.ArrayList;
import java.util.HashMap;

public class QuartettSession {

    public static ArrayList<QuartettSession> AktiveSessions = new ArrayList<QuartettSession>();
    public static int QuartettSessionIdCounter = 0; //using a static means that it is shared between all instances of the class

    private HashMap<VaadinSession, String> usersInSession = new HashMap();

    private int id;
    private ArrayList<Integer> DeckPlayerOne = new ArrayList<Integer>();
    private ArrayList<Integer> DeckPlayerTwo = new ArrayList<Integer>();
    private Player playerOne;
    private Player playerTwo;

    public QuartettSession(int id, ArrayList<ArrayList<Integer>> decks) {
        this.id = id;
        AktiveSessions.add(id,this);
        QuartettSessionIdCounter++;

        this.DeckPlayerOne = decks.get(0);
        this.DeckPlayerTwo = decks.get(1);
        playerOne = new Player(DeckPlayerOne);
        playerTwo = new Player(DeckPlayerTwo);
    }

    public void endSession() {
        AktiveSessions.remove(this);
    }

    public void addPlayer(VaadinSession session, String name) {
        usersInSession.put(session, name);
    }
    public void removePlayer(VaadinSession session) {
        usersInSession.remove(session);
    }
    public HashMap<VaadinSession, String> getPlayers() {
        return usersInSession;
    }

}



/*        // Create a new QuartettSession, assigning a new session ID
        QuartettSession newSession = new QuartettSession();
        activeSessions.add(newSession); // Add to the list of active sessions*/

//okay, so an object is like a copy of a class, i can access everything in it and set variables independently
