package fr.pantheonsorbonne.miage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import fr.pantheonsorbonne.miage.exception.NoMoreCardException;
import fr.pantheonsorbonne.miage.game.Card;
import fr.pantheonsorbonne.miage.game.Deck;
import fr.pantheonsorbonne.miage.enums.CardValue;


/**
 * this class is a abstract version of the engine, to be used locally on through
 * the network
 */
public abstract class ScopaEngine {

	public static final int CARDS_IN_HAND_INITIAL_COUNT = 3;

	Map<String, Queue<Card>> playerCollectedCards = new HashMap<>();
	Map<String, Integer> playerCollectedScopa = new HashMap<>();

	 protected abstract Map<String, Queue<Card>> allPlayerCards();
	/**
	 * play a scopa with the provided players
	 */
	public void play() {
		// send the initial hand to every players

		giveInitialHandToPLayers(playerCollectedCards, playerCollectedScopa);

        Queue<Card> roundDeck = new LinkedList<>(); // la table du jeu
        do{
            roundDeck.addAll(getInitialRoundDeck());
        }
        while(checkOverThreeSameCardValue(roundDeck));


		// make a queue with all the players
		final Queue<String> players = new LinkedList<>();
		players.addAll(this.getInitialPlayers());
		for (int i = 0; i < players.size(); i++) {
			players.offer(players.poll());
		}

		// repeat until there are no more cards in deck
		while (Deck.deckSize > 1) {

			// take the first player form the queue
			String currentPlayer = players.poll();
			System.out.print("player " + currentPlayer + ": ");
			getPlayerCards(currentPlayer).stream().forEach(c -> System.out.print(c.toFancyString()));
			System.out.println();
			System.out.print("RoundDeck: ");
			roundDeck.stream().forEach(c -> System.out.print(c.toFancyString()));
			System.out.println();

			// and put it immediately at the end
			players.offer(currentPlayer);

			if (getPlayerCards(currentPlayer).size() > 0) {
				Card pairCard = makePair(getPlayerCards(currentPlayer), roundDeck);
				//Card pairCard = makePair(currentPlayer, roundDeck);
				if (pairCard != null) {
					Card retiredCard = removeRoundDeckCard(pairCard, roundDeck);
					getPlayerCards(currentPlayer).remove(pairCard);
					playerCollectedCards.get(currentPlayer).offer(retiredCard);
					playerCollectedCards.get(currentPlayer).offer(pairCard);
					if (roundDeck.isEmpty()) {
						int counter = playerCollectedScopa.get(currentPlayer) + 1;
						playerCollectedScopa.put(currentPlayer, counter);
					}
				} else {
					try {
						// apply avoid to put 7D strategy
						Card selectedCard = getCardFromPlayer(currentPlayer);
						// put the card in the round deck
						roundDeck.offer(selectedCard);
					} catch (NoMoreCardException e) {
						;
					}
				}
			} else {
				Card[] cards = Deck.getRandomCards(CARDS_IN_HAND_INITIAL_COUNT);
				String hand = Card.cardsToString(cards); // changer ça si on utilise pas le string
				giveCardsToPlayer(currentPlayer, hand);
			}

		}

		while (!roundDeck.isEmpty()) {
			String currentPlayer = players.poll();
			playerCollectedCards.get(currentPlayer).offer(roundDeck.poll());
			players.offer(currentPlayer);
		}
		
		
		players.stream().forEach(player -> {playerCollectedCards.get(player).addAll(getPlayerCards(player));getPlayerCards(player).clear();});
		
		System.out.println("Collected Cards");
		for (String currentPlayer : players) {
			System.out.print("player " + currentPlayer + ": ");
			playerCollectedCards.get(currentPlayer).stream().forEach(c -> System.out.print(c.toFancyString()));
			System.out.println();
		}
		
		System.out.println("\nHand Cards");
		for (String currentPlayer : players) {
			System.out.print("player " + currentPlayer + ": ");
			getPlayerCards(currentPlayer).stream().forEach(c -> System.out.print(c.toFancyString()));
			System.out.println();
		}
		System.out.print("RoundDeck: ");
		roundDeck.stream().forEach(c -> System.out.print(c.toFancyString()));
		System.out.println();
		// since we've left the loop, we don't have any cards in the deck
		String winner = getWinner(playerCollectedCards);
		// send the winner the gameover and leave
		declareWinner(winner);
		//System.exit(0);
	}

	protected List<Card> getInitialRoundDeck() {
		return Arrays.asList(Deck.getRandomCards(4));
	}

    /**
     * informs if there are three or more cards of the same value on the table
     *
     * @return true if so, false if not
     */
    protected boolean checkOverThreeSameCardValue(Queue<Card> queue){
        HashMap<CardValue,Integer> map = new HashMap<>();
        for (Card card : queue){
            CardValue value = card.getValue();
            if (map.containsKey(value)){
                map.put(value,map.get(value)+1);
                if (map.get(value)>=3){
                    return true;
                }
            }
            else{
                map.put(value,1);
            }
        }
        return false;
    }

    protected void giveInitialHandToPLayers(Map<String, Queue<Card>> playerCollectedCards, Map<String, Integer> playerCollectedScopa) {
        for (String playerName : getInitialPlayers()) {
            //get random cards
            Card[] cards = Deck.getRandomCards(CARDS_IN_HAND_INITIAL_COUNT);
            // transform them to String
            String hand = Card.cardsToString(cards);  //changer ça si on utilise pas le string
            //send them to this players
            giveCardsToPlayer(playerName, hand);
            playerCollectedCards.put(playerName, new LinkedList<>());
            playerCollectedScopa.put(playerName, 0);
            
        }
    }

	Card removeRoundDeckCard(Card matchCard, Queue<Card> roundDeck) {
		Card returnCard;
		for (Card card : roundDeck) {
			if (card.getValue().getRank() == matchCard.getValue().getRank()) {
				returnCard = card;
				roundDeck.remove(card);
				return returnCard;
			}
		}
		return null;
	}

	Card makePair(Queue<Card> playerCards, Queue<Card> roundDeck) {
	//Card makePair(String player, Queue<Card> roundDeck) {
		//Queue<Card> playerCards = getPlayerCards(player);
		Card selectedCard = null;

		// apply 7D strategy
		for (Card card : roundDeck) {
			if (card.toString().equals("7D")) {
				for (Card pcard : playerCards) {
					if (pcard.getValue().getStringRepresentation().equals("7")) {
						return pcard;
					}
				}
			}
		}

		// apply take max pair strategy
		int maxValue = 0;
		for(Card card : playerCards) {
			if (roundDeck.stream().map(crd -> crd.getValue()).filter(val -> val.equals(card.getValue())).count() > 0) {
				if (card.getValue().getRank() > maxValue) {
					maxValue = card.getValue().getRank();
					selectedCard = card;
				}
			}
		}
		return selectedCard;
	}

	/**
	 * provide the list of the initial players to play the game
	 *
	 * @return
	 */
	protected abstract Set<String> getInitialPlayers();

	protected abstract Queue<Card> getPlayerCards(String playerName);

	/**
	 * give some card to a player
	 *
	 * @param playerName the player that will receive the cards
	 * @param hand       the cards as a string (to be converted later)
	 */
	protected abstract void giveCardsToPlayer(String playerName, String hand);

	/**
	 * get a card from a player. If the player doesn't have a card, it will be
	 * declared loser and all the left over cards will be given to his opponent
	 *
	 * @param leftOverCard               card left over from another round
	 * @param cardProviderPlayer         the player that should give a card
	 * @param cardProviderPlayerOpponent the Opponent of this player
	 * @return a card of null if player cardProviderPlayer is gameover
	 */
	protected abstract Card getCardOrGameOver(Collection<Card> leftOverCard, String cardProviderPlayer,
			String cardProviderPlayerOpponent);

	/**
	 * give the winner of a round
	 *
	 * @param contestantA     a contestant
	 * @param contestantB     another contestant
	 * @param contestantACard its card
	 * @param contestantBCard its card
	 * @return the name of the winner or null if it's a tie
	 */
	protected String getWinner(Map<String, Queue<Card>> playerCollectedCards) {
		int maxCount = 0;
		String winner = "";
		Map<String, Integer> playersScores = countPlayersScores(playerCollectedCards);
		for (Map.Entry<String, Integer> player : playersScores.entrySet()) {
			System.out.println(player.getKey() + " a " + player.getValue() + " points.");
			if (player.getValue() > maxCount) {
				maxCount = player.getValue();
				winner = player.getKey();
			}
			// faire condition si 2 gagnants
			else if (player.getValue() == maxCount) {
				winner = "nobody";
			}
		}
		return winner;
	}

	/**
	 * gives the score of each player
	 *
	 * @param playerCollectedCards
	 * @return a map of the players associated with their score
	 */
	protected Map<String, Integer> countPlayersScores(Map<String, Queue<Card>> playerCollectedCards) {
		Map<String, Integer> playerScore = new HashMap<>();
		for (Map.Entry<String, Queue<Card>> player : playerCollectedCards.entrySet()) {
			int count = 0;
			ArrayList<String> bestCountPlayers = bestCount(playerCollectedCards);
			for(String joueur : bestCountPlayers){
				if (player.getKey().equals(joueur)) {
					count++;
				}
			}
			ArrayList<String> mostDenierCountPlayers = bestCount(playerCollectedCards);
			for(String joueur : mostDenierCountPlayers){
				if (player.getKey().equals(joueur)) {
					count++;
				}
			}
			if (player.getKey().equals(havingSettebello(playerCollectedCards))) {
				count++;
			}
			playerScore.put(player.getKey(), count);
		}
		return playerScore;
	}

	/**
	 * gives the name of the player having the most pairs at the end of the game; null if 2 players have the same count
	 *
	 * @param playerCollectedCards
	 * @return name of the player or null
	 */
	ArrayList<String> bestCount(Map<String, Queue<Card>> playerCollectedCards) {
		int maxcount = 0;
		ArrayList<String> bestPlayers = new ArrayList<>();
		for (String player : playerCollectedCards.keySet()) {
			if (playerCollectedCards.get(player).size() > maxcount) {
				maxcount = playerCollectedCards.get(player).size();
			}
		}
		for (String player : playerCollectedCards.keySet()) {
			if (playerCollectedCards.get(player).size() == maxcount) {
				bestPlayers.add(player);
			}
		}

		return bestPlayers;
	}

	/**
	 * gives the name of the player having the most cards of deniers at the end of the game; null if 2 players have the same count
	 *
	 * @param playerCollectedCards
	 * @return the player having the most cards of deniers
	 */
	ArrayList<String> mostDenierCount(Map<String, Queue<Card>> playerCollectedCards) {
		long maxcount = 0;
		ArrayList<String> bestPlayers = new ArrayList<>();
		for (String player : playerCollectedCards.keySet()) {
			long counter = playerCollectedCards.get(player).stream()
					.filter(card -> card.getColor().name().equals("DIAMOND")).count();
			if (counter > maxcount) {
				maxcount = counter;
			}
		}
		for (String player : playerCollectedCards.keySet()) {
				if( playerCollectedCards.get(player).stream()
				.filter(card -> card.getColor().name().equals("DIAMOND")).count() == maxcount){
					bestPlayers.add(player);
				}
		}
		return bestPlayers;
	}

	/**
	 * gives the name of the player having settebello (7 of deniers) at the end of
	 * the game
	 *
	 * @param playerCollectedCards
	 * @return the player having the settebello
	 */
	String havingSettebello(Map<String, Queue<Card>> playerCollectedCards) {
		for (String player : playerCollectedCards.keySet()) {
			if (playerCollectedCards.get(player).stream().filter(card -> card.toString().equals("7D")).count() > 0) {
				return player;
			}
		}
		return null;
	}

	// not used for now
	protected void getCountPlayersScores(Map<String, Queue<Card>> playerCollectedCards) {
		Map<String, Integer> playerScore = countPlayersScores(playerCollectedCards);
		for (Map.Entry player : playerScore.entrySet()) {
			System.out.println(player.getKey() + " a " + player.getValue() + " points.");
		}
	}

	/**
	 * this method must be called when a winner is identified
	 *
	 * @param winner the final winner of the same
	 */
	protected abstract void declareWinner(String winner);

	/**
	 * give some card to a player
	 *
	 * @param playerName the player that will receive the cards
	 * @param cards      the cards as a collection of cards
	 */
	protected abstract void giveCardsToPlayer(Collection<Card> cards, String playerName);

	/**
	 * get a card from a player
	 *
	 * @param player the player to give card
	 * @return the card from the player
	 * @throws NoMoreCardException if the player does not have a remaining card
	 */
	protected abstract Card getCardFromPlayer(String player) throws NoMoreCardException;
}
