package org.abgehoben.QuartettVaadin;

import com.vaadin.flow.server.VaadinSession;

import java.util.ArrayList;

public class Player {

    private String name;

    private VaadinSession SessionId;
    private final ArrayList<Integer> Deck;
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


    public void AddCardToDeck(Integer cardId) {
        Deck.add(0); //let's just try with index
    }
    public void RemoveCardFromDeck(Integer cardId) {
        Deck.remove(cardId);
    }
    public void MoveFirstCardToBackOfDeck() {
        Integer cardId = Deck.get(0);
        Deck.remove(0);
        Deck.add(cardId);
    }

}
