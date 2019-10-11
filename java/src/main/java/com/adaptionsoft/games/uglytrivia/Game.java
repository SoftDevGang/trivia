package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.LinkedList;

public class Game {
    ArrayList players = new ArrayList();
    int[] places = new int[6];
    int[] purses  = new int[6];
    boolean[] inPenaltyBox  = new boolean[6];
    
    LinkedList popQuestions = new LinkedList();
    LinkedList scienceQuestions = new LinkedList();
    LinkedList sportsQuestions = new LinkedList();
    LinkedList rockQuestions = new LinkedList();
    
    int currentPlayer = 0;
    boolean isGettingOutOfPenaltyBox;
    
    public  Game(){
    	for (int i = 0; i < 50; i++) {
			popQuestions.addLast("Pop Question " + i);
			scienceQuestions.addLast(("Science Question " + i));
			sportsQuestions.addLast(("Sports Question " + i));
			rockQuestions.addLast(createRockQuestion(i));
    	}
    }

	public String createRockQuestion(int index){
		return "Rock Question " + index;
	}

	public boolean isPlayable() {
		return (howManyPlayers() >= 2);
	}

	public boolean add(String playerName) {
		
		
	    players.add(playerName);
	    places[howManyPlayers()] = 0;
	    purses[howManyPlayers()] = 0;
	    inPenaltyBox[howManyPlayers()] = false;
	    
	    System.out.println(playerName + " was added");
	    System.out.println("They are player number " + players.size());
		return true;
	}
	
	public int howManyPlayers() {
		return players.size();
	}

	public void roll(int roll) {
        final String playerName = (String) players.get(currentPlayer);
        System.out.println(currentPlayerRoledMessage(roll, playerName));
		
		if (inPenaltyBox[currentPlayer]) {
			if (isEven(roll)) {
				isGettingOutOfPenaltyBox = true;
                int oldPlace = places[currentPlayer];

                int newPlace = nextPlace(roll, oldPlace);
                String newCategory = currentCategory(newPlace);

                System.out.println(playerGetingOutOfPeneltyBoxMessage(playerName, newPlace, newCategory));
				askQuestion(newCategory);

                places[currentPlayer] = newPlace;
			} else {
				System.out.println(playerNotGettingOutOfPenaltyBoxMessage(playerName));
				isGettingOutOfPenaltyBox = false;
				}
			
		} else {

            places[currentPlayer] = nextPlace(roll, places[currentPlayer]);

            System.out.println(playerName
					+ "'s new location is " 
					+ places[currentPlayer]);
			System.out.println("The category is " + currentCategory(places[currentPlayer]));
			askQuestion(currentCategory(places[currentPlayer]));
		}
		
	}

    private static String playerNotGettingOutOfPenaltyBoxMessage(String playerName) {
        return playerName + " is not getting out of the penalty box";
    }

    private static int nextPlace(int roll, int oldPlace) {
        int newPlace = oldPlace + roll;
        if (newPlace > 11) {
            return newPlace - 12;
        }
        return newPlace;
    }

    private static String playerGetingOutOfPeneltyBoxMessage(String playerName, int newPlace, String newCategory) {
        return playerName + " is getting out of the penalty box\n" + playerName
                + "'s new location is "
                + newPlace + "\nThe category is " + newCategory;
    }

    private static boolean isEven(int roll) {
        return roll % 2 != 0;
    }

    private static String currentPlayerRoledMessage(int roll, String playerName) {
        return playerName + " is the current player\nThey have rolled a " + roll;
    }

    private void askQuestion(String category) {
		if (category == "Pop")
			System.out.println(popQuestions.removeFirst());
		if (category == "Science")
			System.out.println(scienceQuestions.removeFirst());
		if (category == "Sports")
			System.out.println(sportsQuestions.removeFirst());
		if (category == "Rock")
			System.out.println(rockQuestions.removeFirst());		
	}
	
	
	private static String currentCategory(int place) {
		if (place == 0) return "Pop";
		if (place == 4) return "Pop";
		if (place == 8) return "Pop";
		if (place == 1) return "Science";
		if (place == 5) return "Science";
		if (place == 9) return "Science";
		if (place == 2) return "Sports";
		if (place == 6) return "Sports";
		if (place == 10) return "Sports";
		return "Rock";
	}

	public boolean wasCorrectlyAnswered() {
		if (inPenaltyBox[currentPlayer]){
			if (isGettingOutOfPenaltyBox) {
				System.out.println("Answer was correct!!!!");
				purses[currentPlayer]++;
				System.out.println(players.get(currentPlayer) 
						+ " now has "
						+ purses[currentPlayer]
						+ " Gold Coins.");
				
				boolean winner = didPlayerWin();
				currentPlayer++;
				if (currentPlayer == players.size()) currentPlayer = 0;
				
				return winner;
			} else {
				currentPlayer++;
				if (currentPlayer == players.size()) currentPlayer = 0;
				return true;
			}
			
			
			
		} else {
		
			System.out.println("Answer was corrent!!!!");
			purses[currentPlayer]++;
			System.out.println(players.get(currentPlayer) 
					+ " now has "
					+ purses[currentPlayer]
					+ " Gold Coins.");
			
			boolean winner = didPlayerWin();
			currentPlayer++;
			if (currentPlayer == players.size()) currentPlayer = 0;
			
			return winner;
		}
	}
	
	public boolean wrongAnswer(){
		System.out.println("Question was incorrectly answered");
		System.out.println(players.get(currentPlayer)+ " was sent to the penalty box");
		inPenaltyBox[currentPlayer] = true;
		
		currentPlayer++;
		if (currentPlayer == players.size()) currentPlayer = 0;
		return true;
	}


	private boolean didPlayerWin() {
		return !(purses[currentPlayer] == 6);
	}
}
