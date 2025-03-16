package org.abgehoben.QuartettVaadin;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;

@Route("quartett")
public class QuartettView extends VerticalLayout implements BeforeEnterObserver, AfterNavigationObserver {

    public QuartettView() {
        VaadinSession session = VaadinSession.getCurrent();
        QuartettSession quartettSession = QuartettService.getQuartettSessionForPlayer(session);

        if (quartettSession == null) {
            add(new Span("No active game found.")); //so, normally this should never happen...
            return;
        }

        setSizeFull(); // Make the view take up the full browser window
        setAlignItems(Alignment.CENTER); // Center content horizontally
        setJustifyContentMode(JustifyContentMode.CENTER); // Center content vertically
        this.getStyle().set("color", "white");
        this.getStyle().setBackground("hsl(200, 10%, 8%)");


        UI CurrentUI = UI.getCurrent();
        Span sessionInfo = new Span("Session ID: " + session.getSession().getId() + ", UI ID: " + CurrentUI);
        sessionInfo.getStyle().set("position", "absolute");
        sessionInfo.getStyle().set("bottom", "10px");
        sessionInfo.getStyle().set("left", "10px");
        sessionInfo.getStyle().set("color", "hsla(214, 96%, 91%, 0.9)");
        sessionInfo.getStyle().set("font-size", "12px");
        this.add(sessionInfo);

        // Create a horizontal layout to hold the player cards
        HorizontalLayout playerCardLayout = new HorizontalLayout();
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
        Div currentPlayerCard = createPlayerCard(currentPlayer.getName(), currentPlayer.card);
        playerCardLayout.add(currentPlayerCard);

        // Create the opponent's card (on the right)
        Div opponentPlayerCard = createPlayerCard(opponentPlayer.getName(), opponentPlayer.card);
        playerCardLayout.add(opponentPlayerCard);
        add(playerCardLayout);
    }

    private Div createPlayerCard(String playerName, Card card) {
        Div PlayerCard = new Div();
        PlayerCard.getStyle().set("border", "2px solid white");
        PlayerCard.getStyle().set("border-radius", "10px");
        PlayerCard.getStyle().set("padding", "10px");
        PlayerCard.getStyle().set("margin", "10px");
        PlayerCard.getStyle().set("background-color", "hsl(200, 10%, 15%)");
        PlayerCard.setWidth("300px");
        PlayerCard.setHeight("400px");

        // Player name
        Span nameLabel = new Span(playerName);
        nameLabel.getStyle().set("font-weight", "bold");
        nameLabel.getStyle().set("display", "block");
        nameLabel.getStyle().set("text-align", "center");
        nameLabel.getStyle().set("color", "white");
        PlayerCard.add(nameLabel);

        // Image
        Image image = new Image(card.getImagePath(), "Card Image");
        image.getStyle().set("display", "block");
        image.getStyle().set("margin-left", "auto");
image.getStyle().set("margin-right", "auto").setMaxWidth("300px").setMaxHeight("240px");
        PlayerCard.add(image);

        return PlayerCard;
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
