package org.abgehoben.QuartettVaadin;

import com.vaadin.flow.server.VaadinSession;

import java.util.ArrayList;
import java.util.HashMap;

public class QuartettSession {

    public static ArrayList<QuartettSession> AktiveSessions = new ArrayList<>();
    public static int QuartettSessionIdCounter = 0; //using a static means that it is shared between all instances of the class

    private final HashMap<VaadinSession, String> usersInSession = new HashMap<>(); //session, name

    private final int id;
    public Player playerOne;
    public Player playerTwo;

    public QuartettSession(int id, ArrayList<ArrayList<Integer>> decks) {
        this.id = id;
        AktiveSessions.add(id,this);
        QuartettSessionIdCounter++;

        ArrayList<Integer> deckPlayerOne = decks.get(0);
        ArrayList<Integer> deckPlayerTwo = decks.get(1);
        playerOne = new Player(deckPlayerOne);
        playerTwo = new Player(deckPlayerTwo);
    }
    public void InitializePlayers() {
        ArrayList<String> userNamesInSession = new ArrayList<>();
        ArrayList<VaadinSession> userSessionsInSession = new ArrayList<>();
        usersInSession.forEach((session, name) -> userNamesInSession.add(name));
        usersInSession.forEach((session, name) -> userSessionsInSession.add(session));
        playerOne.setName(userNamesInSession.get(0));
        playerOne.setSessionId(userSessionsInSession.get(0));
        playerTwo.setName(userNamesInSession.get(1));
        playerTwo.setSessionId(userSessionsInSession.get(1));
        playerOne.setNextCard();
        playerTwo.setNextCard();
        System.out.println("Initialized players: " + playerOne.getSessionId() + " and " + playerTwo.getSessionId());

        System.out.println("Initialized players: " + playerOne.getName() + " and " + playerTwo.getName());
    }

    public void NextRound(Player looserPlayer, Player winnerPlayer) {
        Integer cardOfLostPlayer = looserPlayer.card.getId();//get player card id of lost player
        winnerPlayer.AddCardToDeck(cardOfLostPlayer);//add card id to playerDeck of player who won at the back of the deck
        looserPlayer.RemoveCardFromDeck(cardOfLostPlayer);//next card for player who lost the round (remove current card from deck)

        winnerPlayer.MoveFirstCardToBackOfDeck();//next card of player who won the round //cards get moved to the back of the deck

        System.out.println("Player " + winnerPlayer.getName() + " won the round");
        playerOne.setNextCard();
        playerTwo.setNextCard();

        playerOne.UpdateCardDisplay();
        playerTwo.UpdateCardDisplay();
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


    public boolean onButtonClick(String attribute, Player playerClicker, Player Opponent) {
        Float clickedAttributeValue = playerClicker.card.get(attribute);
        Float opponentAttributeValue = Opponent.card.get(attribute);

        //Smaller == better
        boolean GameResult;
        if (attribute.equals("Consumption") || attribute.equals("zeroTo100")) {
            if (clickedAttributeValue < opponentAttributeValue) {
                NextRound(Opponent, playerClicker);
                GameResult = true;
            } else {
                NextRound(playerClicker, Opponent);
                GameResult = false;
            }
        } else {
            if (clickedAttributeValue < opponentAttributeValue) {
                NextRound(playerClicker, Opponent); //looser, winner
                GameResult = false;
            } else {
                NextRound(Opponent, playerClicker);
                GameResult = true;
            }
        }
        return GameResult;
    }
}
