package org.abgehoben.QuartettVaadin;


import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.DetachNotifier;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;

import java.util.*;

@Route("quartett")
public class QuartettView extends VerticalLayout implements BeforeEnterObserver, AfterNavigationObserver, DetachNotifier {

    private HorizontalLayout playerCardLayout;
    private Div opponentCardContainer;
    private Div ActivePlayerIndicator;

    private Player currentPlayer;
    private Player opponentPlayer;

    public static Map<VaadinSession, QuartettView> sessionViewMap = new HashMap<>();

    public QuartettView() {
        VaadinSession session = VaadinSession.getCurrent();
        QuartettSession quartettSession = QuartettService.getQuartettSessionForPlayer(session);

        if (UI.getCurrent() == null) {
            UI.setCurrent(LoginService.getUIForSession(session));
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

        Button tertiaryButton = new Button(new Icon(VaadinIcon.SIGN_OUT));
        tertiaryButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        tertiaryButton.getStyle().setPosition(Style.Position.ABSOLUTE);
        tertiaryButton.getStyle().setTop("5px");
        tertiaryButton.getStyle().setLeft("5px");
        tertiaryButton.getStyle().set("cursor", "pointer");
        tertiaryButton.getStyle().setColor("hsla(214, 78%, 88%, 0.5)");
        tertiaryButton.addClickListener(event -> QuartettHelper.LeaveConfirmDialog(UI.getCurrent(), session).open());
        this.add(tertiaryButton);


        Span sessionInfo = new Span("Session ID: " + session.getSession().getId() + ", UI ID: " + UI.getCurrent());
        sessionInfo.getStyle().set("position", "absolute");
        sessionInfo.getStyle().set("bottom", "10px");
        sessionInfo.getStyle().set("left", "10px");
        sessionInfo.getStyle().set("color", "hsla(214, 96%, 91%, 0.9)");
        sessionInfo.getStyle().set("font-size", "12px");
        this.add(sessionInfo);

        // Determine which player is the current user and which is the opponent
        VaadinSession currentSession = VaadinSession.getCurrent();
        if (quartettSession.playerOne.getSessionId().getSession().getId().equals(currentSession.getSession().getId())) {
            currentPlayer = quartettSession.playerOne;
            opponentPlayer = quartettSession.playerTwo;
        } else {
            currentPlayer = quartettSession.playerTwo;
            opponentPlayer = quartettSession.playerOne;
        }

        if(quartettSession.GameEnded) {
            this.add(QuartettHelper.createGameEndDisplay(quartettSession, currentPlayer, opponentPlayer, UI.getCurrent()));
            return;
        }

        // Create a horizontal layout to hold the player cards
        playerCardLayout = new HorizontalLayout();
        playerCardLayout.setWidthFull();
        playerCardLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        playerCardLayout.setAlignItems(Alignment.CENTER);

        // Create the current player's card (on the left)
        Div currentPlayerCard = QuartettHelper.createPlayerCard(quartettSession, currentPlayer, opponentPlayer, currentPlayer.card, "You");
        playerCardLayout.add(currentPlayerCard);

        // Create the opponent's card (on the right)
        opponentCardContainer = QuartettHelper.createOpponentCard(opponentPlayer); // Store the container
        playerCardLayout.add(opponentCardContainer);
        add(playerCardLayout);

        ActivePlayerIndicator = QuartettHelper.ActivePlayerIndicator(quartettSession, currentPlayer);
        add(ActivePlayerIndicator);
        updateActivePlayerIndicator(currentPlayer);
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
        Div newCurrentPlayerCard = QuartettHelper.createPlayerCard(quartettSession, currentPlayer, opponentPlayer, currentPlayer.card, "You"); //create a new card
        playerCardLayout.add(newCurrentPlayerCard); //add a new card

        opponentCardContainer = QuartettHelper.createOpponentCard(opponentPlayer); // Store the container
        playerCardLayout.add(opponentCardContainer); //add a new card
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

    public void flipOpponentCard() {
        if (opponentCardContainer == null) return;

        Div cardFlipper = (Div) opponentCardContainer.getChildren().findFirst().orElse(null);
        if (cardFlipper == null) return;

        opponentCardContainer.addClassName("card-flipped"); // Start the flip

        UI currentUI = UI.getCurrent();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                currentUI.access(() -> {
                    opponentCardContainer.removeClassName("card-flipped"); // Reverse the flip
                });
            }
        }, 5000); // 5 seconds
    }

    public void updateActivePlayerIndicator(Player player) {
        VaadinSession session = VaadinSession.getCurrent();
        QuartettSession quartettSession = QuartettService.getQuartettSessionForPlayer(session);

        UI.getCurrent().access(() -> {
            // Update the indicator text (it's the first child)
            if (ActivePlayerIndicator.getElement().getChildCount() > 0) {
                ActivePlayerIndicator.getElement().getChild(0)
                        .setText(Objects.requireNonNull(quartettSession).getAktivePlayer().equals(player) ? "Your turn" : "Opponents turn");
            }
            // Update background color
            ActivePlayerIndicator.getStyle().set("background-color", Objects.requireNonNull(quartettSession).getAktivePlayer().equals(player) ? "green" : "red");

            // Update position
            if (quartettSession.getAktivePlayer().equals(player)) {
                ActivePlayerIndicator.getStyle().set("left", "calc(50% - 320px)");
                ActivePlayerIndicator.getStyle().set("transform", "translateX(0%)");
            } else {
                ActivePlayerIndicator.getStyle().set("left", "calc(50% + 20px)");
                ActivePlayerIndicator.getStyle().set("transform", "translateX(0%)");
            }
            ActivePlayerIndicator.getStyle().set("transition", "left 0.5s ease, transform 0.5s ease");
        });
    }

    public void addOpponentCardInfo(QuartettSession quartettSession) {
        UI.getCurrent().access(() -> {
            Div cardFlipper = (Div) opponentCardContainer.getChildren().findFirst().orElse(null);

            Div cardFaceBack = new Div();
            cardFaceBack.addClassName("card-face");
            cardFaceBack.addClassName("card-face-back");
            cardFaceBack.add(QuartettHelper.createPlayerCard(quartettSession, opponentPlayer, currentPlayer, opponentPlayer.card, "Opponent")); //currentPlayer is the cardOwner
            Objects.requireNonNull(cardFlipper).add(cardFaceBack);
        });
    }

    public void showGameEndDisplay() {
        VaadinSession session = VaadinSession.getCurrent();
        QuartettSession quartettSession = QuartettService.getQuartettSessionForPlayer(session);
        UI.getCurrent().access(() -> {
            this.remove(Objects.requireNonNull(playerCardLayout));
            this.remove(Objects.requireNonNull(ActivePlayerIndicator));
            this.add(QuartettHelper.createGameEndDisplay(Objects.requireNonNull(quartettSession), currentPlayer, opponentPlayer, UI.getCurrent()));
        });
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

    @Override
    public void onDetach(DetachEvent event) {
        VaadinSession session = VaadinSession.getCurrent();
        sessionViewMap.remove(session, this);
        //handle player alone in game
        //TODO
    }
}
