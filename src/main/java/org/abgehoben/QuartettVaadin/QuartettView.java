package org.abgehoben.QuartettVaadin;


import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
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
            UI.setCurrent(LoginService.getUIForSession(session)); //Okay, so with this the particles work, at least for the user that pressed the button in mainview and started the session, but the particles-config isnt loaded and redirects back to main view do not work.
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
        Div currentPlayerCard = createPlayerCard(quartettSession, currentPlayer, opponentPlayer, currentPlayer.card, "You");
        playerCardLayout.add(currentPlayerCard); //add

        // Create the opponent's card (on the right)
        Div opponentPlayerCard = createPlayerCard(quartettSession, opponentPlayer, currentPlayer, opponentPlayer.card, "Opponent");
        playerCardLayout.add(opponentPlayerCard); //add
        add(playerCardLayout);
    }


    private Div createPlayerCard(QuartettSession quartettSession, Player currentPlayer, Player opponentPlayer, Card card, String role) {
        Div PlayerCard = new Div();
        PlayerCard.getStyle().set("border", "2px solid hsla(214, 64%, 82%, 0.23)");
        PlayerCard.getStyle().set("border-radius", "10px");
        PlayerCard.getStyle().set("padding", "10px");
        PlayerCard.getStyle().set("margin", "10px");
        PlayerCard.getStyle().set("background-color", "hsla(214, 10%, 0%, 0.1)");
        PlayerCard.getStyle().set("box-shadow", "0 4px 6px rgba(0, 0, 0, 0.15)");
        PlayerCard.getStyle().set("backdrop-filter", "blur(40px)");
        PlayerCard.setWidth("300px");
        PlayerCard.setHeight("450px");

        HorizontalLayout NameAndRole = CreateNameAndRole(currentPlayer, role);
        PlayerCard.add(NameAndRole);

        Div CardImage = CreateQuartettCardImage(card);
        PlayerCard.add(CardImage); //update

        Span CardName = CreateCardName(card);
        PlayerCard.add(CardName);

        if (role.equals("You")) {
            VerticalLayout playerButtons = createPlayerButtons(quartettSession, currentPlayer, opponentPlayer, card); //update
            PlayerCard.add(playerButtons);
        } else {
            VerticalLayout attributesLayout = createPlayerAttributesSpan(card); //update
            PlayerCard.add(attributesLayout);
        }

        Span CardsLeft = CardsLeft(currentPlayer);
        PlayerCard.add(CardsLeft);

        return PlayerCard;
    }

    public Span CardsLeft(Player player) {
        Span CardsLeft = new Span("Cards Left: " + player.getCardsLeft());
        CardsLeft.getStyle().set("font-weight", "bold")
                .set("display", "block")
                .set("text-align", "left")
                .setPaddingLeft("8px")
                .set("color", "hsla(214, 87%, 92%, 0.8)")
                .setBackground("linear-gradient(to left, rgba(255,0,0,0), hsla(214, 90%, 55%, 0.13))")
                .set("padding", "2px 8px")
                .set("border-radius", "4px");
        return CardsLeft;
    }

    public VerticalLayout createPlayerAttributesSpan(Card card) {
        VerticalLayout attributesLayout = new VerticalLayout();
        for (String attribute : card.getAttributes().keySet()) {
            Div attributeDiv = CreateAttributeDiv(card, attribute);
            attributesLayout.add(attributeDiv);
        }
        attributesLayout.setSpacing(false);
        attributesLayout.setPadding(false);
        attributesLayout.getStyle().set("gap", "8px");
        attributesLayout.getStyle().setPaddingTop("4px");
        attributesLayout.getStyle().setPaddingBottom("4px").setHeight("152px");
        return attributesLayout;
    }

    public Div CreateAttributeDiv(Card card, String attribute) {

        HorizontalLayout attributeContent = new HorizontalLayout();
        attributeContent.setWidthFull();
        attributeContent.setJustifyContentMode(JustifyContentMode.BETWEEN);
        attributeContent.setAlignItems(Alignment.CENTER);
        attributeContent.getStyle().set("padding", "0 4px");

        Span attributeName = new Span(attribute + ": ");
        attributeName.getStyle().set("color", "white");
        attributeName.getStyle().set("text-align", "left");
        attributeName.setWidth("50%");

        Span attributeValue = new Span(card.getAttributes().get(attribute).toString());
        attributeValue.getStyle().set("color", "white");
        attributeValue.getStyle().set("text-align", "right");
        attributeValue.setWidth("50%");

        Div attributes = new Div(attributeContent);
        attributes.setWidthFull();
        attributes.getStyle().set("color", "white");
        attributes.getStyle().set("background-color", "hsla(214, 10%, 0%, 0.1)");
        attributes.getStyle().set("backdrop-filter", "blur(20px)");
        attributes.getStyle().setBorder("0.5px solid rgba(40, 44, 26, 0.3)");
        attributes.getStyle().setBoxShadow("0 4px 6px rgba(0, 0, 0, 0.1)");
        attributes.getStyle().setHeight("29px");
        attributeContent.add(attributeName, attributeValue);
        attributes.getStyle().setBorderRadius("4px");


        return attributes;
    }

    public VerticalLayout createPlayerButtons(QuartettSession quartettSession, Player currentPlayer, Player opponentPlayer, Card card) {
        VerticalLayout attributesLayout = new VerticalLayout();
        attributesLayout.setWidthFull();
        attributesLayout.setPadding(false);
        attributesLayout.setSpacing(false);
        for (String attribute : card.getAttributes().keySet()) {
            Button attributeButton = CreateButton(card, attribute);
            attributesLayout.add(attributeButton);

            attributeButton.addClickListener(event -> {
                attributesLayout.getChildren().forEach(component -> {
                    if (component instanceof Button) {((Button) component).setEnabled(false);}
                });
                boolean playerWon = quartettSession.onButtonClick(attribute, currentPlayer, opponentPlayer);
                if (playerWon) {
                    attributeButton.getStyle().set("background-color", "green");
                } else {
                    attributeButton.getStyle().set("background-color", "red");
                }
            });

        }
        return attributesLayout;
    }

    public Button CreateButton(Card card, String attribute) {
        HorizontalLayout buttonContent = new HorizontalLayout();
        buttonContent.setWidthFull();
        buttonContent.setJustifyContentMode(JustifyContentMode.BETWEEN);
        buttonContent.setAlignItems(Alignment.CENTER);

        Span attributeName = new Span(attribute + ": ");
        attributeName.getStyle().set("color", "white");
        attributeName.getStyle().set("text-align", "left");
        attributeName.setWidth("50%");

        Span attributeValue = new Span(card.getAttributes().get(attribute).toString());
        attributeValue.getStyle().set("color", "white");
        attributeValue.getStyle().set("text-align", "right");
        attributeValue.setWidth("50%");

        Button attributeButton = new Button(buttonContent);
        attributeButton.setWidthFull();
        attributeButton.getStyle().set("color", "white");
        attributeButton.getStyle().set("background-color", "hsla(214, 10%, 0%, 0.1)");
        attributeButton.getStyle().set("backdrop-filter", "blur(20px)");
        attributeButton.getStyle().setBorder("0.5px solid rgba(40, 44, 26, 0.3)");
        attributeButton.getStyle().setBoxShadow("0 4px 6px rgba(0, 0, 0, 0.1)");
        attributeButton.getStyle().setTransition("all 0.3s ease");
        attributeButton.getStyle().setHeight("30px");
        attributeButton.setDisableOnClick(true);


        buttonContent.add(attributeName, attributeValue);
        buttonContent.getStyle().setMinWidth("290px");
        return attributeButton;
    }

    public Span CreateCardName(Card card) {
        Span nameLabel = new Span(card.getName());
        nameLabel.getStyle().set("font-weight", "bold")
                .set("display", "block")
                .set("text-align", "left")
                .setPaddingLeft("8px")
                .set("color", "hsla(214, 87%, 92%, 0.8)")
                .setBackground("linear-gradient(to left, rgba(255,0,0,0), hsla(214, 90%, 55%, 0.13))")
                .set("padding", "2px 8px")
                .set("border-radius", "4px");
        return nameLabel;
    }

    public Div CreateQuartettCardImage(Card card) {
        Div CardImage = new Div();
        Image image = new Image(card.getImagePath(), "Card Image");
        image.getStyle().set("display", "block");
        image.getStyle().set("margin-left", "auto");
        image.getStyle().set("margin-right", "auto").setWidth("300px").setHeight("200px").set("object-fit", "cover");
        image.getStyle().setBorderRadius("4px");
        image.getStyle().setMarginTop("4px");
        image.getStyle().setMarginBottom("2px");
        CardImage.add(image);
        return CardImage;
    }

    public HorizontalLayout CreateNameAndRole(Player currentPlayer, String role) {
        HorizontalLayout NameAndRole = new HorizontalLayout();
        // Player name
        Span nameLabel = new Span(currentPlayer.getName());
        nameLabel.getStyle().set("font-weight", "bold")
                .set("display", "block")
                .set("text-align", "left")
                .setPaddingLeft("8px")
                .set("color", "hsla(214, 87%, 92%, 0.8)")
                .setBackground("linear-gradient(to left, rgba(255,0,0,0), hsla(214, 90%, 55%, 0.13))")
                .set("padding", "2px 8px")
                .set("border-radius", "4px");
        NameAndRole.add(nameLabel);

        Span PlayerRole = new Span(role);
        PlayerRole.getStyle().set("font-weight", "bold")
                .set("display", "block")
                .set("text-align", "left")
                .setPaddingLeft("8px")
                .set("color", "hsla(214, 87%, 92%, 0.8)")
                .setBackground("linear-gradient(to left, rgba(255,0,0,0), hsla(214, 90%, 55%, 0.13))")
                .set("padding", "2px 8px")
                .set("border-radius", "4px");
        NameAndRole.add(PlayerRole);

        return NameAndRole;
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
        Div newCurrentPlayerCard = createPlayerCard(quartettSession, currentPlayer, opponentPlayer, currentPlayer.card, "You"); //create new card
        playerCardLayout.add(newCurrentPlayerCard); //add new card

        Div newOpponentPlayerCard = createPlayerCard(quartettSession, opponentPlayer, currentPlayer, opponentPlayer.card, "Opponent"); //create new card
        playerCardLayout.add(newOpponentPlayerCard); //add new card
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
