package org.abgehoben.QuartettVaadin;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;

import java.util.ArrayList;

public class Player {

    private String name;

    private VaadinSession SessionId;
    private final ArrayList<Integer> Deck;
    public Card card;
    private String LastClickedAttribute;
    public Boolean clickedPlayAgain = false;

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
    public void setNextCard() {
        Integer cardId = Deck.get(0);
        String CardName = Cards.getName(cardId);
        Float TopSpeed = Cards.getTopSpeed(cardId);
        Float zeroTo100 = Cards.getZeroTo100(cardId);
        Float PS = Cards.getPS(cardId);
        Float Consumption = Cards.getConsumption(cardId);
        System.out.println(CardName + " with cardId " + cardId);
        card.set(cardId, CardName, TopSpeed, zeroTo100, PS, Consumption);
    }
    public void UpdateCardDisplay() {
        UI ui = LoginService.getUIForSession(SessionId);
        QuartettView quartettView = QuartettView.sessionViewMap.get(SessionId);
        if (quartettView != null) {
            ui.access(quartettView::updatePlayerCards);
        }
    }

    public void flipOpponentCard() {
        UI ui = LoginService.getUIForSession(SessionId);
        QuartettView quartettView = QuartettView.sessionViewMap.get(SessionId);
        if (quartettView != null) {
            ui.access(quartettView::flipOpponentCard);
        }
    }

    public void updateActivePlayerIndicator() {
        UI ui = LoginService.getUIForSession(SessionId);
        QuartettView quartettView = QuartettView.sessionViewMap.get(SessionId);
        if (quartettView != null) {
            ui.access(() -> quartettView.updateActivePlayerIndicator(this)); //this takes a Player object
        }
    }

    public void addOpponentCardInfo(QuartettSession quartettSession) {
        UI ui = LoginService.getUIForSession(SessionId);
        QuartettView quartettView = QuartettView.sessionViewMap.get(SessionId);
        if (quartettView != null) {
            ui.access(() -> quartettView.addOpponentCardInfo(quartettSession));
        }
    }

    public void AddCardToDeck(Integer cardId) {
        Deck.add(cardId);
    }
    public void RemoveCardFromDeck(Integer cardId) {
        Deck.remove(cardId);
    }
    public void MoveFirstCardToBackOfDeck() {
        Integer cardId = Deck.get(0);
        Deck.remove(0);
        Deck.add(cardId);
    }

    public Integer getCardsLeft() {
        return Deck.size();
    }

    public String getLastClickedAttribute() {
        return LastClickedAttribute;
    }
    public void setLastClickedAttribute(String attribute) {
        LastClickedAttribute = attribute;
    }

    public void showGameEndDisplay() {
        UI ui = LoginService.getUIForSession(SessionId);
        QuartettView quartettView = QuartettView.sessionViewMap.get(SessionId);
        if (quartettView != null) {
            ui.access(quartettView::showGameEndDisplay);
        }
    }
}
