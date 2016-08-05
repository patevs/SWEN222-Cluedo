package cluedo.tokens;

import cluedo.control.CluedoGame.Weapon;

/**
 * Weapon token on the board that can be moved by players.
 */
public class WeaponToken extends GameToken implements Card{
	
	private Weapon name;
	
	/**
	 * Creates a game token representation of the weapon.
	 * @param name
	 */
	public WeaponToken(Weapon name){
		this.name=name;
	}
	
	/**
	 * Returns the weapon name as a string.
	 */
	public String getName(){
		return name.toString();
	}
	
	/**
	 * Returns the weapon card.
	 * @return
	 */
	public Weapon token(){
		return name;
	}
	
	/**
	 * Returns the string representation of the weapon.
	 */
	public String toString(){
		return name.toString();
	}

	@Override
	public char getSymbol() {
		switch(getName()){
			case "CANDLESTICK":
				return '+';
			case "DAGGER":
				return '-';
			case "LEAD PIPE":
				return '/';
			case "REVOLVER":
				return '*';
			case "ROPE":
				return '=';
			case "SPANNER":
				return '?';
			default:
				return (Character) null;
		}
	}
}
