package org.abgehoben.QuartettVaadin;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.server.VaadinSession;

import java.util.*;

public class QuartettSession {

    public static ArrayList<QuartettSession> AktiveSessions = new ArrayList<>();
    public static int QuartettSessionIdCounter = 0;

    private final HashMap<VaadinSession, String> usersInSession = new HashMap<>(); //session, name

    private final int id;
    public Player playerOne;
    public Player playerTwo;
    private Player AktivePlayer;
    public boolean GameEnded = false;
    private Player GlobalWinnerPlayer;

    public QuartettSession(int id, ArrayList<ArrayList<Integer>> decks) {
        this.id = id;
        AktiveSessions.add(id,this);
        QuartettSessionIdCounter++;

        ArrayList<Integer> deckPlayerOne = decks.get(0);
        ArrayList<Integer> deckPlayerTwo = decks.get(1);
        playerOne = new Player(deckPlayerOne);
        playerTwo = new Player(deckPlayerTwo);
        AktivePlayer = playerOne;
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
        playerOne.updateActivePlayerIndicator();
        playerTwo.updateActivePlayerIndicator();
        System.out.println("Initialized players: " + playerOne.getSessionId() + " and " + playerTwo.getSessionId());

        System.out.println("Initialized players: " + playerOne.getName() + " and " + playerTwo.getName());
    }

    public void NextRound(Player looserPlayer, Player winnerPlayer) {
        Integer cardOfLostPlayer = looserPlayer.card.getId();//get player card id of lost player
        winnerPlayer.AddCardToDeck(cardOfLostPlayer);//add card id to playerDeck of player who won at the back of the deck
        looserPlayer.RemoveCardFromDeck(cardOfLostPlayer);//next card for the player who lost the round (remove current card from deck)

        winnerPlayer.MoveFirstCardToBackOfDeck();//next card of player who won the round //cards get moved to the back of the deck

        System.out.println("Player " + winnerPlayer.getName() + " won the round");

        playerOne.addOpponentCardInfo(this);
        playerTwo.addOpponentCardInfo(this);

        playerOne.flipOpponentCard();
        playerTwo.flipOpponentCard();

        if(looserPlayer.getCardsLeft() < 1) {
            System.out.println("Player " + looserPlayer.getName() + " has no cards left");
            scheduleEndOfGame(winnerPlayer);
            return;
        }

        scheduleNextCard(winnerPlayer);
    }

    private void scheduleEndOfGame(Player winnerPlayer) {
        GlobalWinnerPlayer = winnerPlayer;
        GameEnded = true;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                playerOne.showGameEndDisplay();
                playerTwo.showGameEndDisplay();


                timer.cancel(); // Stop the timer after execution
            }
        }, 4000);
    }

    private void scheduleNextCard(Player winnerPlayer) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                setAktivePlayer(winnerPlayer);

                playerOne.setNextCard();
                playerTwo.setNextCard();

                playerOne.UpdateCardDisplay();
                playerTwo.UpdateCardDisplay();
                playerOne.updateActivePlayerIndicator();
                playerTwo.updateActivePlayerIndicator();
                AlreadyClicked = false;
                //flip the opponent card back
                timer.cancel(); // Stop the timer after execution
            }
        }, 4000);
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

    public Player getAktivePlayer() {
        return AktivePlayer;
    }
    public void setAktivePlayer(Player aktivePlayer) {
        AktivePlayer = aktivePlayer;
    }
    public Player getWinner() {
        return GlobalWinnerPlayer;
    }

    public Boolean AlreadyClicked = false;

    public boolean onButtonClick(String attribute, Player playerClicker, Player Opponent) {
        AlreadyClicked = true;
        Float clickedAttributeValue = playerClicker.card.get(attribute);
        Float opponentAttributeValue = Opponent.card.get(attribute);
        playerClicker.setLastClickedAttribute(attribute);

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

    public void playAgain(Player currentPlayer, Player OtherPlayer) {
        currentPlayer.clickedPlayAgain = true;
        if(usersInSession.size() < 2) {
            QuartettService.leaveGame(currentPlayer.getSessionId());
            String greeting = LoginService.greet(currentPlayer.getName(), currentPlayer.getSessionId());
            if (greeting.equals("Joined Queue") || greeting.equals("already in Queue") || greeting.equals("already in Game")) {

                Notification notification = new Notification(greeting, 4000);
                if (greeting.equals("Joined Queue")) {
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                }

                if (greeting.equals("already in Queue") || greeting.equals("already in Game")) {
                    notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
                }

                UI ui = LoginService.getUIForSession(currentPlayer.getSessionId());
                ui.access(notification::open);
            }
        } else {
            if(OtherPlayer.clickedPlayAgain) {
                QuartettService.startNewGame(usersInSession);
                QuartettService.endGame(this);
            } else {
                //wait for the other player
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (OtherPlayer.clickedPlayAgain) {
                            timer.cancel(); // Stop the timer after execution
                            return;
                        }
                        QuartettService.leaveGame(currentPlayer.getSessionId());
                        LoginService.greet(currentPlayer.getName(), currentPlayer.getSessionId());
                    }
                }, 15000);
            }
        }
    }
}
