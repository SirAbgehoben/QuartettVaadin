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

        LoginService.usersInQueue.forEach(quartettSession::addPlayer); //addPlayer(session, name) for each usersInQueue

        quartettSession.InitializePlayers();

        for (VaadinSession session : LoginService.usersInQueue.keySet()) {
            session.access(() -> {
                UI ui = LoginService.getUIForSession(session);
                if (ui != null && ui.isAttached()) {
                    System.out.println("Navigating to quartett for session: " + session.getSession().getId() + " with UI: " + ui);
                    ui.navigate(QuartettView.class);
                } else {
                    System.out.println("UI is null for session: " + session.getSession().getId());
                }
            });
        }
        LoginService.usersInQueue.clear();
    }

    public static void joinGame(VaadinSession session) {
        session.access(() -> {
            UI ui = LoginService.getUIForSession(session);
            if (ui != null) {
                System.out.println("Navigating to quartett for session: " + session.getSession().getId() + " with UI: " + ui);
                ui.navigate("quartett");
            } else {
                System.out.println("UI is null for session: " + session.getSession().getId());
            }
        });
    }
    public static void leaveGame(VaadinSession session) {
        QuartettSession quartettSession = getQuartettSessionForPlayer(session);
        System.out.println("Player "+ LoginService.usersInGame.get(session) + " left the game");

        if (quartettSession == null) {
            return;
        }

        if (quartettSession.getPlayers().isEmpty()) {
            endGame(quartettSession);
        } else if (quartettSession.getPlayers().containsKey(session)) {
            quartettSession.removePlayer(session);
            LoginService.usersInGame.remove(session);
        }
    }

    public static void endGame(QuartettSession quartettSession) {
        for (VaadinSession session : quartettSession.getPlayers().keySet()) {
            session.access(() -> {
                UI ui = LoginService.getUIForSession(session);
                if (ui != null && ui.isAttached()) {
                    System.out.println("Navigating to login for session: " + session.getSession().getId() + " with UI: " + ui);
                    ui.navigate(MainView.class);
                } else {
                    System.out.println("UI is null for session: " + session.getSession().getId());
                }
            });
        }
        quartettSession.endSession();
        //noinspection ReassignedVariable,UnusedAssignment
        quartettSession = null;
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
        for (QuartettSession quartettSession : QuartettSession.AktiveSessions) {
            if (quartettSession.getPlayers().containsKey(session)) {
                return quartettSession;
            }
        }
        return null;
    }
}
