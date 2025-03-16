package org.abgehoben.QuartettVaadin;

import com.vaadin.flow.server.VaadinSession;

import java.util.ArrayList;
import java.util.HashMap;

public class QuartettSession {

    public static ArrayList<QuartettSession> AktiveSessions = new ArrayList<QuartettSession>();
    public static int QuartettSessionIdCounter = 0; //using a static means that it is shared between all instances of the class

    private HashMap<VaadinSession, String> usersInSession = new HashMap(); //session, name

    private int id;
    private ArrayList<Integer> DeckPlayerOne = new ArrayList<Integer>();
    private ArrayList<Integer> DeckPlayerTwo = new ArrayList<Integer>();
    public Player playerOne;
    public Player playerTwo;

    public QuartettSession(int id, ArrayList<ArrayList<Integer>> decks) {
        this.id = id;
        AktiveSessions.add(id,this);
        QuartettSessionIdCounter++;

        this.DeckPlayerOne = decks.get(0);
        this.DeckPlayerTwo = decks.get(1);
        playerOne = new Player(DeckPlayerOne);
        playerTwo = new Player(DeckPlayerTwo);
    }
    public void InitializePlayers() {
        ArrayList<String> userNamesInSession = new ArrayList<>();
        ArrayList<VaadinSession> userSessionsInSession = new ArrayList<>();
        usersInSession.forEach((session, name) -> {
            userNamesInSession.add(name);
        });
        usersInSession.forEach((session, name) -> {
            userSessionsInSession.add(session);
        });
        playerOne.setName(userNamesInSession.get(0));
        playerOne.setSessionId(userSessionsInSession.get(0));
        playerTwo.setName(userNamesInSession.get(1));
        playerTwo.setSessionId(userSessionsInSession.get(1));
        System.out.println("Initialized players: " + playerOne.getSessionId() + " and " + playerTwo.getSessionId());

        System.out.println("Initialized players: " + playerOne.getName() + " and " + playerTwo.getName());
    }

    public void endSession() {
        AktiveSessions.remove(this);
    }
    public int getId() {
        return id;
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
