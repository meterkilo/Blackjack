package blackjack;

import java.util.*;

public class Blackjack implements BlackjackEngine {
	int accountBal;
	int iniBet;
	int decks;
	int status;
	boolean playerAce = false;
	boolean dealerAce = false;
	Random r = new Random();
	ArrayList<Card> deck = new ArrayList<Card>();
	ArrayList<Card> pDeck = new ArrayList<Card>();
	ArrayList<Card> dDeck = new ArrayList<Card>();

	

	public Blackjack(Random randomGenerator, int numberOfDecks) {
		accountBal = 200;
		iniBet = 5;
		decks = numberOfDecks;
		r = randomGenerator;

	}

	public int getNumberOfDecks() {
		return decks;
	}

	public void createAndShuffleGameDeck() {

		// loop for creating deck
		for (int j = 0; j < 4; j++) {

			// creates suits
			String val = "";
			String suit = "";

			if (j == 0) {
				suit = "SPADES";
			} else if (j == 1) {
				suit = "DIAMONDS";
			} else if (j == 2) {
				suit = "HEARTS";
			} else if (j == 3) {
				suit = "CLUBS";
			}
			// creates values
			for (int i = 0; i < 13; i++) {

				if (i == 0) {
					val = "Ace";
				} else if (i == 1) {
					val = "Two";
				} else if (i == 2) {
					val = "Three";
				} else if (i == 3) {
					val = "Four";
				} else if (i == 4) {
					val = "Five";
				} else if (i == 5) {
					val = "Six";
				} else if (i == 6) {
					val = "Seven";
				} else if (i == 7) {
					val = "Eight";
				} else if (i == 8) {
					val = "Nine";
				} else if (i == 9) {
					val = "Ten";
				} else if (i == 10) {
					val = "Jack";
				} else if (i == 11) {
					val = "Queen";
				} else if (i == 12) {
					val = "King";
				}
				// adds card to arraylist
				Card c = new Card(CardValue.valueOf(val), CardSuit.valueOf(suit));
				deck.add(c);
			}

		}

		// shuffles deck
		Collections.shuffle(deck, r);

	}

	public Card[] getGameDeck() {
		return deck.toArray(new Card[0]);
	}

	public void deal() {
		// creates deck
		playerAce = false;
		dealerAce = false;
		deck.clear();
		pDeck.clear();
		dDeck.clear();
		createAndShuffleGameDeck();

		// assigns cards
		pDeck.add(getGameDeck()[0]);

		getGameDeck()[1].setFaceDown();
		dDeck.add(getGameDeck()[1]);
		pDeck.add(getGameDeck()[2]);
		dDeck.add(getGameDeck()[3]);

		// removes 4 cards
		for (int i = 0; i < 4; i++) {
			deck.remove(0);
		}

		accountBal -= iniBet;
		status = GAME_IN_PROGRESS;

	}

	public Card[] getDealerCards() {

		return dDeck.toArray(new Card[0]);

	}

	public int[] getDealerCardsTotal() {
		ArrayList<Integer> total = new ArrayList<Integer>();
		int aceCounter = 0;
		int totalVal = 0;

		// totals up non ace values and counts aces
		for (int i = 0; i < getDealerCards().length; i++) {
			if (getDealerCards()[i].getValue().getIntValue() == 1) {
				dealerAce = true;
				aceCounter++;
			} else {
				totalVal += getDealerCards()[i].getValue().getIntValue();
			}
		}

		// adds aces as ones to the total
		if (totalVal + aceCounter <= 21) {
			total.add(totalVal + aceCounter);
		}

		// adds aces as 11s or 1s to the total
		if (totalVal <= 10 && aceCounter > 0) {
			if (totalVal + 11 + (aceCounter - 1) <= 21) {
				total.add(totalVal + 11 + (aceCounter - 1));
			}
		}

		// returns as int array
		if (total.isEmpty() || totalVal > 21) {
			return null;
		} else {
			return total.stream().mapToInt(i -> i).toArray();
		}

	}

	public int getDealerCardsEvaluation() {
		// checks bust
		if (getDealerCardsTotal() == null) {
			status = PLAYER_WON;
			return BUST;
		} else {
			boolean ace = false;
			boolean face = false;
			for (int i = 0; i < getDealerCardsTotal().length; i++) {
				if (getDealerCardsTotal()[i] == 21) {
					// checks for blackjack
					for (int j = 0; j < dDeck.size(); j++) {
						if (dDeck.get(j).getValue().getIntValue() == 1) {
							ace = true;
						} else if (dDeck.get(j).getValue().getIntValue() == 10) {
							face = true;
						}
					}
					if (ace && face) {
						if (dDeck.size() == 2) {
							return BLACKJACK;
						}
					} else {
						return HAS_21;
					}
				}
			}
			return LESS_THAN_21;
		}
	}

	public Card[] getPlayerCards() {
		return pDeck.toArray(new Card[0]);
	}

	public int[] getPlayerCardsTotal() {
		ArrayList<Integer> total = new ArrayList<Integer>();
		int aceCounter = 0;
		int totalVal = 0;

		// totals up non ace values and counts aces
		for (int i = 0; i < getPlayerCards().length; i++) {
			if (getPlayerCards()[i].getValue().getIntValue() == 1) {
				playerAce = true;
				aceCounter++;
			} else {
				totalVal += getPlayerCards()[i].getValue().getIntValue();
			}
		}

		// adds aces as ones to the total
		if (totalVal + aceCounter <= 21) {
			total.add(totalVal + aceCounter);
		}

		// adds aces as 11s or 1s to the total
		if (totalVal <= 10 && aceCounter > 0) {
			if (totalVal + 11 + (aceCounter - 1) <= 21) {
				total.add(totalVal + 11 + (aceCounter - 1));
			}
		}

		// returns as int array
		if (total.isEmpty() || totalVal > 21) {
			return null;
		} else {
			return total.stream().mapToInt(i -> i).toArray();
		}
	}

	public int getPlayerCardsEvaluation() {
		// checks conditions
		if (getPlayerCardsTotal() == null) {
			status = DEALER_WON;
			return BUST;
		} else {
			boolean ace = false;
			boolean face = false;
			for (int i = 0; i < getPlayerCardsTotal().length; i++) {
				if (getPlayerCardsTotal()[i] == 21) {
					// checks for blackjack
					for (int j = 0; j < pDeck.size(); j++) {
						if (pDeck.get(j).getValue().getIntValue() == 1) {
							ace = true;
						} else if (pDeck.get(j).getValue().getIntValue() == 10) {
							face = true;
						}
					}
					if (ace && face) {
						if (pDeck.size() == 2) {
							return BLACKJACK;
						}
					} else {
						return HAS_21;
					}
				}
			}
			return LESS_THAN_21;
		}
	}

	public void playerHit() {
		// adds
		if (status == DEALER_WON) {
			return;
		}
		pDeck.add(getGameDeck()[0]);
		deck.remove(0);
		getPlayerCardsEvaluation();

	}

	public void playerStand() {
		// sets dealer card face up
		for (int i = 0; i < dDeck.size(); i++) {
			dDeck.get(i).setFaceUp();
		}

		if (getDealerCardsEvaluation() != BUST) {

			// checks draws
			if (getDealerCardsEvaluation() == BLACKJACK && getPlayerCardsEvaluation() == BLACKJACK) {
				status = DRAW;
				accountBal += iniBet;
				return;
			}

			if (getDealerCardsEvaluation() == HAS_21 && getPlayerCardsEvaluation() == HAS_21) {
				status = DRAW;
				accountBal += iniBet;
				return;
			}

			if (getDealerCardsEvaluation() == HAS_21 && getPlayerCardsEvaluation() == BLACKJACK) {
				status = DRAW;
				accountBal += iniBet;
				return;
			}

			if (getDealerCardsEvaluation() == BLACKJACK && getPlayerCardsEvaluation() == HAS_21) {
				status = DRAW;
				accountBal += iniBet;
				return;
			}

			if (getDealerCardsEvaluation() == LESS_THAN_21) {

				boolean cont = true;

				if (dealerAce) {

					cont = false;
					if (getDealerCardsTotal().length > 1 && getDealerCardsTotal()[1] < 16) {
						cont = true;

					}
				}

				if (cont) {

					boolean under = true;

					// adds until above 16
					while (under) {
						for (int i = 0; i < getDealerCardsTotal().length; i++) {
							if (getDealerCardsTotal()[i] < 16) {
								under = true;
								dDeck.add(getGameDeck()[0]);
								deck.remove(0);
								if (getDealerCardsEvaluation() == BUST) {
									status = PLAYER_WON;
									return;
								}
							} else {
								under = false;
							}
						}

					}
				}
			}

			// checks winner if no aces
			if (!dealerAce && !playerAce) {
				if (getDealerCardsTotal()[0] >= 16 && getDealerCardsTotal()[0] <= 21) {
					if (getDealerCardsTotal()[0] > getPlayerCardsTotal()[0]) {
						status = DEALER_WON;
					} else if (getDealerCardsTotal()[0] < getPlayerCardsTotal()[0]) {
						status = PLAYER_WON;
						accountBal += iniBet * 2;
					} else if (getDealerCardsTotal()[0] == getPlayerCardsTotal()[0]) {
						status = DRAW;
						accountBal += iniBet;
					}

				}
				// checks dealers decks if they have ace
			} else if (dealerAce && !playerAce) {
				// checks highest pair with ace

				int greatest = getDealerCardsTotal()[0];

				if (getDealerCardsTotal().length > 1) {
					if (getDealerCardsTotal()[1] >= 16 && getDealerCardsTotal()[1] <= 21) {
						greatest = getDealerCardsTotal()[1];

					}
				}
				if (greatest > getPlayerCardsTotal()[0]) {
					status = DEALER_WON;
				} else if (greatest < getPlayerCardsTotal()[0]) {
					status = PLAYER_WON;
					accountBal += iniBet * 2;
				} else if (greatest == getPlayerCardsTotal()[0]) {
					status = DRAW;
					accountBal += iniBet;
				}

				// checks player ace
			} else if (!dealerAce && playerAce) {
				// checks highest pair with ace
				int greatest = 0;

				if (getPlayerCardsEvaluation() != BLACKJACK) {

					greatest = getPlayerCardsTotal()[0];

					if (getPlayerCardsTotal().length > 1) {
						if (getPlayerCardsTotal()[1] >= 16 && getPlayerCardsTotal()[1] <= 21) {
							greatest = getPlayerCardsTotal()[1];

						}
					}
				} else {
					greatest = 21;
				}

				if (greatest > getDealerCardsTotal()[0]) {
					status = PLAYER_WON;
					accountBal += iniBet * 2;
				} else if (greatest < getDealerCardsTotal()[0]) {
					status = DEALER_WON;
				} else if (greatest == getDealerCardsTotal()[0]) {
					status = DRAW;
					accountBal += iniBet;
				}

			} // checks both ace
			else if (dealerAce && playerAce) {
				int pGreat = getPlayerCardsTotal()[0];
				int dGreat = getDealerCardsTotal()[0];

				// finds dealer greatest
				if (getDealerCardsTotal().length > 1) {
					if (getDealerCardsTotal()[1] >= 16 && getDealerCardsTotal()[1] <= 21) {
						dGreat = getDealerCardsTotal()[1];

					}
				}
				// finds player greatest
				if (getPlayerCardsTotal().length > 1) {
					if (getPlayerCardsTotal()[1] >= 16 && getPlayerCardsTotal()[1] <= 21) {
						pGreat = getPlayerCardsTotal()[1];
					}
				}
				// checks winner
				if (dGreat > pGreat) {
					status = DEALER_WON;
				} else if (dGreat < pGreat) {
					status = PLAYER_WON;
					accountBal += iniBet * 2;
				} else if (dGreat == pGreat) {
					status = DRAW;
					accountBal += iniBet;

				}

			}
		}

	}

	public int getGameStatus() {
		return status;
	}

	public void setBetAmount(int amount) {
		iniBet = amount;
	}

	public int getBetAmount() {
		return iniBet;
	}

	public void setAccountAmount(int amount) {
		accountBal = amount;
	}

	public int getAccountAmount() {
		return accountBal;
	}

	
}
