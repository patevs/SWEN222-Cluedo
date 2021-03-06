package cluedo.tokens;

import java.util.ArrayList;
import java.util.List;

import cluedo.control.CluedoGame;
import cluedo.control.CluedoGame.Character;

/**
 * Character tokens on the board that can be moved by players.
 * Represents a player in the game.
 * 
 * @author Patrick Evans and Maria Legaspi
 * 
 */
public class CharacterToken extends GameToken{
	
	private String name;
	private CluedoGame.Character token; // character in game
	private boolean isPlayer;
	private int uid; // unique id of player
	private boolean hasSuggested = false;
	
	private List<Card> hand = new ArrayList<Card>(); // represents the players hand of cards  
	
	// number of remaining steps for a turn
	private int stepsRemaining;
	 
	/**
	 * Creates a character token which holds a set of cards and represents the player on the board.
	 * @param name
	 * @param token
	 * @param uid
	 */
	public CharacterToken(String name, Character token, boolean isPlayer, int uid) {
		this.name = name;
		this.token = token;
		this.isPlayer = isPlayer;
		this.uid = uid;
	}

	@Override
	public String getName(){
		return name.toString();
	}
	
	/**
	 * Returns the character associated with this token.
	 * @return
	 */
	public CluedoGame.Character getToken() {
		return token;
	}
	
	/**
	 * Returns true if this character token represents an active player.
	 * @return
	 */
	public boolean isPlayer(){
		return isPlayer;
	}
	
	/**
	 * Returns false if is a spare character.
	 * @param active
	 */
	public void isPlayer(boolean active){
		this.isPlayer = active;
	}
	
	/**
	 * Returns true if already made a suggestion in given room.
	 * @return
	 */
	public boolean hasSuggested(){
		return hasSuggested;
	}
	
	/**
	 * Sets whether player has made a suggestion.
	 * @param s
	 */
	public void suggested(boolean s){
		hasSuggested = s;
	}
	
	/**
	 * Returns the uid of this character token.
	 * @return
	 */
	public int getUid() {
		return uid;
	}
	
	/**
	 * Returns the player's cards.
	 * @return
	 */
	public List<Card> getHand() {
		return hand;
	}
	
	/**
	 * Adds a card to the player's hand.
	 * @param c
	 */
	public void addCard(Card c){
		hand.add(c);
	}
	
	/**
	 * Sets the amount of steps the player can move.
	 * @param steps
	 */
	public void setRemainingSteps(int steps) {
		this.stepsRemaining = steps;
	}
	
	/**
	 * Returns the amount of steps the player can move.
	 * @return
	 */
	public int getRemainingSteps(){
		return this.stepsRemaining;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((token == null) ? 0 : token.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CharacterToken other = (CharacterToken)obj;
		if (token != other.getToken())
			return false;
		if (name != other.getName())
			return false;
		return true;
	}
	
	@Override
	public String toString(){
		return name.toString();
	}

	@Override
	public char getSymbol() {
		return java.lang.Character.forDigit(getUid(), 10);
	}
}
