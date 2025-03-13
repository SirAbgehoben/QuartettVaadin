package org.abgehoben.QuartettVaadin;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;

import java.util.ArrayList;

import static org.abgehoben.QuartettVaadin.QuartettSession.QuartettSessionIdCounter;

public class QuartettService {

    public QuartettService() {
    }

    public static void startNewGame() {
        ArrayList<ArrayList<Integer>> decks = createCardsDeck();
        QuartettSession quartettSession = new QuartettSession(QuartettSessionIdCounter, decks);

        LoginService.usersInQueue.forEach( (session, name) -> {;
            quartettSession.addPlayer(session, name);
        });

        // Navigate users in the queue to QuartettView
        for (VaadinSession session : LoginService.usersInQueue.keySet()) {
            session.access(() -> UI.getCurrent().navigate("QuartettView"));
        }
    }

    public static void joinGame(VaadinSession session) {
        //do this later
        session.access(() -> UI.getCurrent().navigate("QuartettView"));
    }

    public void endGame(QuartettSession quartettSession) {
        quartettSession = null;
        //send users to LoginView
    }

    public static ArrayList<ArrayList<Integer>> createCardsDeck() {
        ArrayList<Integer> deck1 = new ArrayList<>();
        ArrayList<Integer> deck2 = new ArrayList<>();
        ArrayList<Integer> allCards = new ArrayList<>();

        for (int i = 1; i <= 16; i++) {
            allCards.add(i);
        }

        java.util.Collections.shuffle(allCards);

        for (int i = 0; i < 8; i++) {
            deck1.add(allCards.get(i));
            deck2.add(allCards.get(i + 8));
        }

        ArrayList<ArrayList<Integer>> decks = new ArrayList<>();
        decks.add(deck1);
        decks.add(deck2);
        //now each deck has 8 cards randomly shuffled

        return decks;
    }

    public static QuartettSession getQuartettSessionForPlayer(VaadinSession session) {
        for (QuartettSession quartettSession : QuartettSession.AktiveSessions) { //because i cant fucking remember it, the first is generating a new variable with the name : (in) the aktive sessions hashmap
            if (quartettSession.getPlayers().containsKey(session)) {
                return quartettSession;
            }
        }
        return null;
    }
}
