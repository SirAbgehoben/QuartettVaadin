package org.abgehoben.QuartettVaadin;


import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;

import java.util.HashMap;
import java.util.Map;

@Route("quartett")
public class QuartettView extends VerticalLayout implements BeforeEnterObserver, AfterNavigationObserver {

    private HorizontalLayout playerCardLayout;


    public static Map<VaadinSession, QuartettView> sessionViewMap = new HashMap<>();

    public QuartettView() {
        VaadinSession session = VaadinSession.getCurrent();
        QuartettSession quartettSession = QuartettService.getQuartettSessionForPlayer(session);

        if (UI.getCurrent() == null) {
            UI.setCurrent(LoginService.getUIForSession(session)); //Okay, so with this the particles work, at least for the user that pressed the button in MainView and started the session, but the particles-config isn't loaded and redirects back to main view do not work.
        }

        if (quartettSession == null) {
            add(new Span("No active game found.")); //so, normally this should never happen...
            UI.getCurrent().navigate("");
            return;
        }

        sessionViewMap.put(session, this);

        setSizeFull(); // Make the view take up the full browser window
        setAlignItems(Alignment.CENTER); // Center content horizontally
        setJustifyContentMode(JustifyContentMode.CENTER); // Center content vertically
        this.getStyle().set("color", "white");
        this.getStyle().setBackground("hsl(200, 10%, 8%)");
        ParticleBackground particleBackground = new ParticleBackground();
        particleBackground.getStyle().set("position", "absolute");
        particleBackground.getStyle().set("top", "0");
        particleBackground.getStyle().set("left", "0");
        particleBackground.getStyle().setZIndex(0);
        particleBackground.getStyle().setWidth("100%");
        particleBackground.getStyle().setHeight("100%");
        particleBackground.getStyle().set("overflow", "hidden");
        add(particleBackground);

        Span sessionInfo = new Span("Session ID: " + session.getSession().getId() + ", UI ID: " + UI.getCurrent());
        sessionInfo.getStyle().set("position", "absolute");
        sessionInfo.getStyle().set("bottom", "10px");
        sessionInfo.getStyle().set("left", "10px");
        sessionInfo.getStyle().set("color", "hsla(214, 96%, 91%, 0.9)");
        sessionInfo.getStyle().set("font-size", "12px");
        this.add(sessionInfo);

        // Create a horizontal layout to hold the player cards
        playerCardLayout = new HorizontalLayout();
        playerCardLayout.setWidthFull();
        playerCardLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        playerCardLayout.setAlignItems(Alignment.CENTER);

        // Determine which player is the current user and which is the opponent
        VaadinSession currentSession = VaadinSession.getCurrent();
        Player currentPlayer;
        Player opponentPlayer;
        if (quartettSession.playerOne.getSessionId().getSession().getId().equals(currentSession.getSession().getId())) {
            currentPlayer = quartettSession.playerOne;
            opponentPlayer = quartettSession.playerTwo;
        } else {
            currentPlayer = quartettSession.playerTwo;
            opponentPlayer = quartettSession.playerOne;
        }

        // Create the current player's card (on the left)
        Div currentPlayerCard = QuartettHelper.createPlayerCard(quartettSession, currentPlayer, opponentPlayer, currentPlayer.card, "You");
        playerCardLayout.add(currentPlayerCard);

        // Create the opponent's card (on the right)
        Div opponentPlayerCard = QuartettHelper.createPlayerCard(quartettSession, opponentPlayer, currentPlayer, opponentPlayer.card, "Opponent");
//        Div opponentPlayerCard = QuartettHelper.createOpponentBlankCard(opponentPlayer);
        playerCardLayout.add(opponentPlayerCard);
        add(playerCardLayout);
    }

    public void updatePlayerCards() {
        VaadinSession session = VaadinSession.getCurrent();
        QuartettSession quartettSession = QuartettService.getQuartettSessionForPlayer(session);

        if (quartettSession == null) {
            return;
        }

        // Determine which player is the current user and which is the opponent
        Player currentPlayer;
        Player opponentPlayer;
        if (quartettSession.playerOne.getSessionId().getSession().getId().equals(session.getSession().getId())) {
            currentPlayer = quartettSession.playerOne;
            opponentPlayer = quartettSession.playerTwo;
        } else {
            currentPlayer = quartettSession.playerTwo;
            opponentPlayer = quartettSession.playerOne;
        }

        playerCardLayout.removeAll();
        Div newCurrentPlayerCard = QuartettHelper.createPlayerCard(quartettSession, currentPlayer, opponentPlayer, currentPlayer.card, "You"); //create new card
        playerCardLayout.add(newCurrentPlayerCard); //add new card

        Div newOpponentPlayerCard = QuartettHelper.createPlayerCard(quartettSession, opponentPlayer, currentPlayer, opponentPlayer.card, "Opponent"); //create new card
        playerCardLayout.add(newOpponentPlayerCard); //add new card
    }

    public void showTimeNotification() {
        Span countdownSpan = new Span();

        Notification notification = new Notification(countdownSpan);
        notification.setDuration(5000);
        notification.setPosition(Notification.Position.TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
        notification.open();

        UI currentUI = UI.getCurrent();

        new Thread(() -> {
            for (int i = 4; i >= 0; i--) {
                final String secondsLeft = String.valueOf(i);
                currentUI.access(() -> countdownSpan.setText(secondsLeft));
                try {Thread.sleep(1000);}
                catch (InterruptedException e) {Thread.currentThread().interrupt(); return;}
            }
            currentUI.access(notification::close);
        }).start();
    }


    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        VaadinSession sessionId = VaadinSession.getCurrent();
        QuartettSession quartettSession = QuartettService.getQuartettSessionForPlayer(sessionId);
        if (!LoginService.usersInGame.containsKey(sessionId) || quartettSession == null) {
            System.out.println(sessionId.getSession().getId() + " with UI: " + LoginService.getUIForSession(sessionId) + " couldn't enter");
            event.forwardTo("");
        }
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        VaadinSession session = VaadinSession.getCurrent();
        UI currentUI = event.getLocationChangeEvent().getUI();
        if (currentUI != null) {
            LoginService.sessionUIMap.put(session, currentUI);
            System.out.println("Updated UI map for session: " + session.getSession().getId() + " with UI: " + currentUI);
        } else {
            System.out.println("Could not update UI map for session: " + session.getSession().getId() + " because UI is null");
        }
    }
}
