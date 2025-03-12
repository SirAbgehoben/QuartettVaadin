package org.abgehoben.QuartettVaadin;

import java.util.ArrayList;

public class QuartettService {

    ArrayList<Integer> DeckPlayerOne = new ArrayList<Integer>();

    public QuartettService() {
    }
    public void startGame() {


    }
    public void joinGame() {
    }
    public ArrayList<ArrayList<Integer>> createCardsDeck() {
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
}
