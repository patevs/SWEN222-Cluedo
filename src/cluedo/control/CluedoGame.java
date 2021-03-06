package cluedo.control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import cluedo.board.Board;
import cluedo.tokens.Card;
import cluedo.tokens.CharacterToken;
import cluedo.tokens.WeaponToken;

/**
 * Contains the game equipment, cards and players.
 * 
 * @author Patrick Evans and Maria Legaspi
 *
 */
public class CluedoGame {
	
	
	private int numberOfPlayers; // number of players in game
	private List<CharacterToken> activePlayers; // players still active in game
	private static List<WeaponToken> weapons; // all weapons in game
	private static List<Room> rooms; // all rooms in the game
	private static List<Character> characters;
	
	private static List<Card> deck; // represents the deck of all cards
	private static List<Card> unusedCards; // unused cards left after deal
	private static Card[] solution; // random game solution
	
	private static Board gameBoard; // the game board

	/**
	 * Creates a new cluedo game and deals cards to the appropriate players.
	 * @param nPlayers
	 * @param players
	 * @param boardFile
	 */
	public CluedoGame(int nPlayers, List<CharacterToken> players, String boardFile) {
		if(boardFile==null|boardFile.length()<1)
			throw new CluedoError("Invalid board arguments");
		this.numberOfPlayers = nPlayers;
		this.activePlayers = players;
		CluedoGame.characters = getCharacters();
		CluedoGame.weapons = getWeapons();
		CluedoGame.rooms = getRooms();
		CluedoGame.solution = getSolution();
		CluedoGame.gameBoard = new Board(this, boardFile);
		CluedoGame.deck = getDeck();
		dealCards();
		placeWeapons();
	}

	/**
	 * Get the number of players in the game
	 * @return num of players
	 */
	public int numPlayers(){
		return numberOfPlayers;
	}
	
	/**
	 * Get the current active players in the game
	 * @return
	 */
	public List<CharacterToken> players(){
		return activePlayers;
	}
	
	/**
	 * Returns the list of all weapons.
	 * @return
	 */
	public static List<WeaponToken> weapons(){
		return weapons;
	}
	
	/**
	 * Returns the list of all rooms.
	 * @return
	 */
	public static List<Room> rooms(){
		return rooms;
	}
	
	/**
	 * Returns the list of all characters.
	 * @return
	 */
	public static List<Character> characters(){
		return characters;
	}
	
	/**
	 * Returns the list of all cards in the deck.
	 * @return
	 */
	public static List<Card> deck(){
		return deck;
	}
	
	/**
	 * Retur the board.
	 * @return
	 */
	public static Board board(){
		return gameBoard;
	}
	
	/**
	 * Returns the list of extra cards.
	 * @return
	 */
	public List<Card> unusedCards(){
		return unusedCards;
	}
	
	/**
	 * This method gets a random solution to the Cluedo game.
	 *  The solution contains one Character card, one Room card, 
	 *  and one weapon card.
	 * @return solution
	 */
	private static Card[] getSolution() {
		Card[] solution = new Card[3];
		solution[0] = Character.getRandom();
		solution[1] = Room.getRandom();
		solution[2] = Weapon.getRandom();
		return solution;
	}
	
	/**
	 * Returns the game solution
	 * @return game solution
	 */
	public Card[] Solution(){
		return solution;
	}
	
	/**
	 * Checks to see if there are still active players.
	 * @return
	 */
	public boolean activePlayers(){
		for(CharacterToken player: activePlayers){
			if(player.isPlayer())
				return true;
		}
		return false;
	}
	
	/**
	 * Returns a list of all characters.
	 * @return
	 */
	private static List<Character> getCharacters() {
		List<Character> characters = new ArrayList<Character>();
		// iterate over each weapon and add each one to the list
		for(Character c: Character.values()){
			characters.add(c);
		}
		// return the list of all weapons
		return characters;
	}
	
	/**
	 * Returns the character token with the given name.
	 * @param name
	 * @return
	 */
	public CharacterToken getCharacter(String name){
		CharacterToken charToken = null;
		for(CharacterToken character: activePlayers){
			if(character.getName().equalsIgnoreCase(name))
				charToken = character;
		}
		return charToken;
	}

	/** 
	 * This method returns a list of all the weapons in
	 *  the game
	 * @return a list of all weapons
	 */
	private static List<WeaponToken> getWeapons(){
		List<WeaponToken> weapons = new ArrayList<WeaponToken>();
		// iterate over each weapon and add each one to the list
		for(Weapon w: Weapon.values()){
			WeaponToken weapon = new WeaponToken(w);
			weapons.add(weapon);
		}
		// return the list of all weapons
		return weapons;
	}
	
	/**
	 * Returns the weapon token with a given name.
	 * @param name
	 * @return
	 */
	public WeaponToken getWeapon(String name){
		WeaponToken weaponToken = null;
		for(WeaponToken weapon: weapons){
			if(weapon.getName().equalsIgnoreCase(name))
				weaponToken = weapon;
		}
		return weaponToken;
	}
	
	/**
	 * Returns a list of all the rooms in the game
	 * @return a list of all rooms
	 */
	private static List<Room> getRooms() {
		List<Room> rooms = new ArrayList<Room>();
		// iterate over each room and add each one to the list
		for(Room r: Room.values()){
			rooms.add(r);
		}
		// return the list of all rooms
		return rooms;
	}
	
	/**
	 * Returns a room card which name matches a string
	 * 	or null if no match.
	 */
	public static Room getRoom(String name){
		for(Room room : getRooms()){
			if(room.toString().equalsIgnoreCase(name)){
				return room;
			}
		}
		return null; // room not found
	}
	
	/**
	 * Returns a list of all the cards in the game deck
	 * @return list of all cards
	 */
	public List<Card> getDeck() {
		List<Card> deck = new ArrayList<Card>();
		deck.addAll(Arrays.asList(Character.values()));
		deck.addAll(Arrays.asList(Weapon.values()));
		deck.addAll(Arrays.asList(Room.values()));
		deck.removeAll(Arrays.asList(Solution()));
		return deck;
	}
	
	/**
	 * Deals the cards evenly to all player leaving out left over cards
	 */
	private void dealCards() {
		// remove unused cards from deck so remaining cards can be dealt evenly
		int numUnused = deck.size() % numPlayers();
		unusedCards = new ArrayList<Card>();
		for(int i=0; i<numUnused; i++){
			unusedCards.add(getCardFromDeck());
		}
		// deal cards evenly to players
		int numCardsToDeal = deck.size() / numPlayers();
		for(CharacterToken player: players()){
			if(player.isPlayer()){ // makes sure this is a player not just an empty character
				for(int i=0; i<numCardsToDeal; i++){
					player.addCard(getCardFromDeck());
				}
			}
		}
	}
	
	/**
	 * Gets a random card from the remaining deck of cards
	 * @return random card from deck
	 */
	private Card getCardFromDeck() {
		Card result = deck.get(new Random().nextInt(deck.size()));
		deck.remove(result);
		return result;
	}

	/**
	 * Sets weapons in random rooms.
	 */
	private void placeWeapons(){
		List<Room> hasWeapon = new ArrayList<Room>();
		// places each weapon in a room
		for(WeaponToken weapon: getWeapons()){
			for(Room r: getRooms()){
				// finds a room with no weapon in it
				if(!hasWeapon.contains(r))
					// moves the weapon into that room
					board().moveIntoRoom(weapon, r);
					hasWeapon.add(r);
					break;
			}
		}
	}

	/**
	 * Represents the six characters in the game
	 */
	public enum Character implements Card {
		MISS_SCARLETT,
		COLONEL_MUSTARD,
		MRS_WHITE,
		THE_REVEREND_GREEN,
		MRS_PEACOCK,
		PROFESSOR_PLUM;

		/**
		 * Returns a random card.
		 * @return
		 */
		public static Card getRandom() {
			return values()[(int) (Math.random() * values().length)];
		}

		@Override
		public String toString() {
			return this.name().replaceAll("_", " ");
		}
	}

	/**
	 * Represents the six weapons in the game
	 */
	public enum Weapon implements Card {
		CANDLESTICK,
		DAGGER,
		LEAD_PIPE,
		REVOLVER,
		ROPE,
		SPANNER;

		/**
		 * Returns a random weapon.
		 * @return
		 */
		public static Card getRandom() {
			return values()[(int) (Math.random() * values().length)];
		}

		@Override
		public String toString() {
			return this.name().replaceAll("_", " ");
		}
	}

	/**
	 * Represents the nine rooms in the game
	 */
	public enum Room implements Card {
		KITCHEN,
		BALL_ROOM,
		CONSERVATORY,
		BILLIARD_ROOM,
		LIBRARY,
		STUDY,
		HALL,
		LOUNGE,
		DINING_ROOM;

		/**
		 * Returns a random room.
		 * @return
		 */
		public static Card getRandom() {
			return values()[(int) (Math.random() * values().length)];
		}

		@Override
		public String toString() {
			return this.name().replaceAll("_", " ");
		}
	}
}
