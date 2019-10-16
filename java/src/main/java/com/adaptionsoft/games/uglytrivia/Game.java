package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.function.Consumer;

public class Game {
    ArrayList players = new ArrayList();
    int[] places = new int[6];
    int[] purses = new int[6];
    boolean[] inPenaltyBox = new boolean[6];

    LinkedList popQuestions = new LinkedList();
    LinkedList scienceQuestions = new LinkedList();
    LinkedList sportsQuestions = new LinkedList();
    LinkedList rockQuestions = new LinkedList();

    int currentPlayer = 0;
    boolean isGettingOutOfPenaltyBox;

    public Game() {
        for (int i = 0; i < 50; i++) {
            popQuestions.addLast("Pop Question " + i);
            scienceQuestions.addLast(("Science Question " + i));
            sportsQuestions.addLast(("Sports Question " + i));
            rockQuestions.addLast(createRockQuestion(i));
        }
    }

    public String createRockQuestion(int index) {
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
        foo(roll, System.out::println, (String) players.get(currentPlayer),
            currentPlayer, b -> isGettingOutOfPenaltyBox = b);
    }

    private void foo(int roll, Consumer<Object> println, String playerName, int currentPlayer,
                     Consumer<Boolean> gettingOutOfPenaltyBox) {
        println.accept(currentPlayerRoledMessage(roll, playerName));

        if (inPenaltyBox[currentPlayer]) {
            if (isEven(roll)) {
                gettingOutOfPenaltyBox.accept(true);

                int oldPlace = places[currentPlayer];
                int newPlace1 = nextPlace(roll, oldPlace);
                String newCategory = currentCategory(newPlace1);

                PlayerState playerState = new PlayerState(playerName, newPlace1, newCategory);
                // TODO extract 1 function with argument println composed Message Supplier
                println.accept(playerGetingOutOfPeneltyBoxMessage(playerState));
                askQuestion(newCategory, println);
                int newPlace = newPlace1;

                places[currentPlayer] = newPlace;
            } else {
                println.accept(playerNotGettingOutOfPenaltyBoxMessage(playerName));
                gettingOutOfPenaltyBox.accept(false);
            }

        } else {

            int oldPlace = places[currentPlayer];
            int newPlace = nextPlace(roll, oldPlace);
            String newCategory = currentCategory(newPlace);

            PlayerState playerState = new PlayerState(playerName, newPlace, newCategory);
            // TODO extract 1 function with argument println composed Message Supplier
            println.accept(getNewPlaceMessage(playerState));
            askQuestion(newCategory, println);

            places[currentPlayer] = newPlace;

        }
    }

    private static String getNewPlaceMessage(PlayerState playerState2) {
        return playerState2.getPlayerName()
            + "'s new location is "
            + playerState2.getNewPlace() + "\nThe category is " + playerState2.getNewCategory();
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

    private static String playerGetingOutOfPeneltyBoxMessage(PlayerState playerState) {
        return playerState.getPlayerName() + " is getting out of the penalty box\n" + playerState.getPlayerName()
            + "'s new location is "
            + playerState.getNewPlace() + "\nThe category is " + playerState.getNewCategory();
    }

    private static boolean isEven(int roll) {
        return roll % 2 != 0;
    }

    private static String currentPlayerRoledMessage(int roll, String playerName) {
        return playerName + " is the current player\nThey have rolled a " + roll;
    }

    private void askQuestion(String category, Consumer<Object> println) {
        if (category == "Pop") {
            println.accept(popQuestions.removeFirst());
        }
        if (category == "Science") {
            println.accept(scienceQuestions.removeFirst());
        }
        if (category == "Sports") {
            println.accept(sportsQuestions.removeFirst());
        }
        if (category == "Rock") {
            println.accept(rockQuestions.removeFirst());
        }
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
        if (inPenaltyBox[currentPlayer]) {
            if (isGettingOutOfPenaltyBox) {
                System.out.println("Answer was correct!!!!");
                purses[currentPlayer]++;
                System.out.println(players.get(currentPlayer)
                    + " now has "
                    + purses[currentPlayer]
                    + " Gold Coins.");

                boolean winner = didPlayerWin();
                currentPlayer++;
                if (currentPlayer == players.size())
                    currentPlayer = 0;

                return winner;
            } else {
                currentPlayer++;
                if (currentPlayer == players.size())
                    currentPlayer = 0;
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
            if (currentPlayer == players.size())
                currentPlayer = 0;

            return winner;
        }
    }

    public boolean wrongAnswer() {
        System.out.println("Question was incorrectly answered");
        System.out.println(players.get(currentPlayer) + " was sent to the penalty box");
        inPenaltyBox[currentPlayer] = true;

        currentPlayer++;
        if (currentPlayer == players.size())
            currentPlayer = 0;
        return true;
    }

    private boolean didPlayerWin() {
        return !(purses[currentPlayer] == 6);
    }

    private static class PlayerState {
        private final String playerName;
        private final int newPlace;
        private final String newCategory;

        private PlayerState(String playerName, int newPlace, String newCategory) {
            this.playerName = playerName;
            this.newPlace = newPlace;
            this.newCategory = newCategory;
        }

        public String getPlayerName() {
            return playerName;
        }

        public int getNewPlace() {
            return newPlace;
        }

        public String getNewCategory() {
            return newCategory;
        }
    }

    private static class PlayerState2 {
        private final String playerName;
        private final int oldPlace;
        private final String category;

        private PlayerState2(String playerName, int oldPlace, String category) {
            this.playerName = playerName;
            this.oldPlace = oldPlace;
            this.category = category;
        }

        public String getPlayerName() {
            return playerName;
        }

        public int getOldPlace() {
            return oldPlace;
        }

        public String getCategory() {
            return category;
        }
    }
}
