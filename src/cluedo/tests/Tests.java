package cluedo.tests;

import static org.junit.Assert.*;
import org.junit.Test;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import cluedo.board.*;
import cluedo.control.*;
import cluedo.control.CluedoGame.Character;
import cluedo.control.CluedoGame.Room;
import cluedo.tokens.*;

/**
 * Tests the Cluedo game
 * @author Patrick Evans and Maria Legaspi
 *
 */
public class Tests {
	
	@Test
	public void testCharactersNotEmpty(){
		CluedoGame game = newGame();
		assertFalse(game.characters().isEmpty());
	}
	
	@Test
	public void testWeaponsNotEmpty(){
		CluedoGame game = newGame();
		assertFalse(game.weapons().isEmpty());
	}
	
	@Test
	public void testRoomsNotEmpty(){
		CluedoGame game = newGame();
		assertFalse(game.rooms().isEmpty());
	}
	
	@Test
	public void testNumCharacters(){
		CluedoGame game = newGame();
		assertEquals(6, game.characters().size());
	}
	
	@Test
	public void testNumWeapons(){
		CluedoGame game = newGame();
		assertEquals(6, game.weapons().size());
	}
	
	@Test
	public void testNumRooms(){
		CluedoGame game = newGame();
		assertEquals(9, game.rooms().size());
	}

	@Test
	public void testCharacterCardName(){
		CharacterToken chara = new CharacterToken("name", CluedoGame.Character.MISS_SCARLETT, true, 0);
		assertTrue(chara.getName().equals("name"));
		assertTrue(chara.getToken().toString().equals("MISS SCARLETT"));
	}
	
	@Test
	public void testPlayerHand(){
		CluedoGame game = newGame();
		assertNotNull(game.players().get(0).getHand());
	}
	
	@Test
	public void testCharacterNames(){
		CluedoGame game = newGame();
		List<CluedoGame.Character> characters = game.characters();
		assertEquals(characters.get(0).toString(), "MISS SCARLETT");
		assertEquals(characters.get(1).toString(), "COLONEL MUSTARD");
		assertEquals(characters.get(2).toString(), "MRS WHITE");
		assertEquals(characters.get(3).toString(), "THE REVEREND GREEN");
		assertEquals(characters.get(4).toString(), "MRS PEACOCK");
		assertEquals(characters.get(5).toString(), "PROFESSOR PLUM");
	}
	
	@Test
	public void testWeaponNames(){
		CluedoGame game = newGame();
		List<WeaponToken> weapons = game.weapons();
		assertEquals(weapons.get(0).toString(), "CANDLESTICK");
		assertEquals(weapons.get(1).toString(), "DAGGER");
		assertEquals(weapons.get(2).toString(), "LEAD PIPE");
		assertEquals(weapons.get(3).toString(), "REVOLVER");
		assertEquals(weapons.get(4).toString(), "ROPE");
		assertEquals(weapons.get(5).toString(), "SPANNER");
	}
	
	@Test
	public void testInvalidCharacterTokenEquals() {
		CharacterToken character = getPlayer();
		CharacterToken other = getOtherPlayer();
		assertFalse(character.equals(other));
	}

	@Test
	public void testValidCharacterEquals(){
		CluedoGame.Character character = getPlayer().getToken();
		CluedoGame.Character other = getPlayer().getToken();
		assertTrue(character.equals(other));
	}
	
	@Test
	public void testInvalidCharacterEquals(){
		CluedoGame.Character character = getPlayer().getToken();
		CluedoGame.Character other = getOtherPlayer().getToken();
		assertFalse(character.equals(other));
	}
	
	@Test
	public void activePlayer(){
		TextClient client = new TextClient();
		CluedoGame game = newGame();
		client.setGame(game);
		client.setBoard(game.board());
		assertEquals(game.players(), game.board().players());
	}
	
	@Test
	public void testValidAccusation(){
		TextClient client = cluedoGame();
		CluedoGame game = client.game();
		Board board = client.board();
		Card[] solution= game.Solution();
		assertTrue(client.checkAccusation(solution));
	}
	
	@Test
	public void testInvalidAccusation(){
		TextClient client = cluedoGame();
		CluedoGame game = client.game();
		Board board = client.board();
		Card[] solution= game.Solution();
		Card[] guess = solution.clone();
		// change the room so the guess is wrong
		List<Room> rooms = getAllRooms();
		for(Room r: rooms){
			if(!(r.name().equals(solution[1].toString()))){
					guess[1] = r;
					break;
			}
		}
		assertFalse(client.checkAccusation(guess));
	}
	
	@Test
	public void testGameEquipment(){
		CluedoGame game = newGame();
		assertEquals(game.weapons().size(), 6);
		assertEquals(game.rooms().size(), 9);
	}
	
	@Test
	public void testRoomSymbols(){
		Board board = newBoard(newGame());
		assertEquals('K', board.getRoomSymbol(Room.KITCHEN));
		assertEquals('B', board.getRoomSymbol(Room.BALL_ROOM));
		assertEquals('C', board.getRoomSymbol(Room.CONSERVATORY));
		assertEquals('I', board.getRoomSymbol(Room.BILLIARD_ROOM));
		assertEquals('L', board.getRoomSymbol(Room.LIBRARY));
		assertEquals('S', board.getRoomSymbol(Room.STUDY));
		assertEquals('H', board.getRoomSymbol(Room.HALL));
		assertEquals('O', board.getRoomSymbol(Room.LOUNGE));
		assertEquals('N', board.getRoomSymbol(Room.DINING_ROOM));
	}
	
	@Test
	public void testRoomNames(){
		List<Room> all = getAllRooms();
		assertTrue(all.contains(CluedoGame.Room.KITCHEN));
		assertTrue(all.contains(CluedoGame.Room.BALL_ROOM));
		assertTrue(all.contains(CluedoGame.Room.CONSERVATORY));
		assertTrue(all.contains(CluedoGame.Room.BILLIARD_ROOM));
		assertTrue(all.contains(CluedoGame.Room.LIBRARY));
		assertTrue(all.contains(CluedoGame.Room.STUDY));
		assertTrue(all.contains(CluedoGame.Room.HALL));
		assertTrue(all.contains(CluedoGame.Room.LOUNGE));
		assertTrue(all.contains(CluedoGame.Room.DINING_ROOM));
	}

	@Test
	public void testPlayerPosition(){
		TextClient client = cluedoGame();
		CluedoGame game = client.game();
		Board board = client.board();
		CharacterToken player = getPlayer();
		//System.out.println(player.getXPos() + ", " + player.getYPos());
		CharacterToken othPlayer = getOtherPlayer();
		board.move(getRoomPos(), player);
		//System.out.println(player.getXPos() + ", " + player.getYPos());
		board.moveIntoRoom(othPlayer, CluedoGame.Room.BALL_ROOM);
		System.out.println(player.getXPos() + ", " + player.getYPos());
		System.out.println(othPlayer.getXPos() + ", " + othPlayer.getYPos());
		RoomTile pOneTile = (RoomTile)(board.getTile(player.getXPos(), player.getYPos()));
		RoomTile pTwoTile = (RoomTile)(board.getTile(othPlayer.getXPos(), othPlayer.getYPos()));
		assertEquals(pOneTile.name().toString(), pTwoTile.name().toString());
	}
	
	//================//
	// HELPER METHODS //
	//================//
	
	/**
	 * Returns a solution with Colonel Mustard, Ballroom, Rope.
	 */
	private Card[] getSolution(){
		Card[] solution = new Card[3];
		solution[0] = getPlayer().getToken();
		solution[1] = getRoom();
		solution[2] = getWeaponToken();
		return solution;
	}
	
	/**
	 * Returns a test character.
	 * @return
	 */
	private CharacterToken getPlayer(){
		return new CharacterToken("test1", CluedoGame.Character.COLONEL_MUSTARD, true, 0);
	}
	
	/**
	 * Returns a second test character.
	 * @return
	 */
	private CharacterToken getOtherPlayer(){
		return new CharacterToken("test2", CluedoGame.Character.MISS_SCARLETT, true, 0);
	}
	
	/**
	 * Returns a random test character.
	 * @return
	 */
	private CharacterToken getRandomPlayer(){
		int index = (int)(Math.random()*6);
		return getAllCharacters().get(index);
	}
	
	/**
	 * Returns a non-player character.
	 */
	private CharacterToken getNonPlayer(){
		return new CharacterToken("test1", CluedoGame.Character.MRS_PEACOCK, false, 0);
	}
	
	/**
	 * Returns a list of all character tokens.
	 * @return
	 */
	private List<CharacterToken> getAllCharacters(){
		List<CharacterToken> characters = new ArrayList<CharacterToken>();
		characters.add(new CharacterToken("test1", CluedoGame.Character.COLONEL_MUSTARD, true, 0));
		characters.add(new CharacterToken("test2", CluedoGame.Character.MISS_SCARLETT, true, 0));
		characters.add(new CharacterToken("test3", CluedoGame.Character.MRS_PEACOCK, true, 0));
		characters.add(new CharacterToken("test4", CluedoGame.Character.MRS_WHITE, true, 0));
		characters.add(new CharacterToken("test5", CluedoGame.Character.PROFESSOR_PLUM, true, 0));
		characters.add(new CharacterToken("test6", CluedoGame.Character.THE_REVEREND_GREEN, true, 0));
		return characters;
	}
	
	/**
	 * Returns a position in the ballroom.
	 */
	private Point getRoomPos(){
		return new Point(12,4);
	}
	
	/**
	 * Returns a second position in the billiard room.
	 * @return
	 */
	private Point getOthRoomPos(){
		return new Point(21,11);
	}
	
	/**
	 * Returns a test room.
	 */
	private Room getRoom(){
		return CluedoGame.Room.BALL_ROOM;
	}
	
	/**
	 * Returns a second test room.
	 */
	private Room getOtherRoom(){
		return CluedoGame.Room.BILLIARD_ROOM;
	}
	
	/**
	 * Returns a random test room.
	 */
	private Room getRandomRoom(){
		int index = (int)(Math.random()*6);
		return getAllRooms().get(index);
	}
	
	private List<Room> getAllRooms(){
		List<Room> rooms = new ArrayList<Room>();
		rooms.add(CluedoGame.Room.KITCHEN);
		rooms.add(CluedoGame.Room.BALL_ROOM);
		rooms.add(CluedoGame.Room.CONSERVATORY);
		rooms.add(CluedoGame.Room.BILLIARD_ROOM);
		rooms.add(CluedoGame.Room.LIBRARY);
		rooms.add(CluedoGame.Room.STUDY);
		rooms.add(CluedoGame.Room.HALL);
		rooms.add(CluedoGame.Room.LOUNGE);
		rooms.add(CluedoGame.Room.DINING_ROOM);
		return rooms;
	}
	
	/**
	 * Returns a test weapon.
	 * @return
	 */
	private WeaponToken getWeaponToken(){
		return new WeaponToken(CluedoGame.Weapon.ROPE);
	}
	
	/**
	 * Returns a second test weapon.
	 * @return
	 */
	private WeaponToken getOtherWeaponToken(){
		return new WeaponToken(CluedoGame.Weapon.CANDLESTICK);
	}
	
	/**
	 * Returns a random weapon.
	 * @return
	 */
	private WeaponToken getRandomWeapon(){
		int index = (int)(Math.random()*6);
		return getAllWeapons().get(index);
	}
	
	/**
	 * Returns the list of all weapons.
	 * @return
	 */
	private List<WeaponToken> getAllWeapons(){
		List<WeaponToken> weapons = new ArrayList<WeaponToken>();
		weapons.add(new WeaponToken(CluedoGame.Weapon.CANDLESTICK));
		weapons.add(new WeaponToken(CluedoGame.Weapon.DAGGER));
		weapons.add(new WeaponToken(CluedoGame.Weapon.LEAD_PIPE));
		weapons.add(new WeaponToken(CluedoGame.Weapon.REVOLVER));
		weapons.add(new WeaponToken(CluedoGame.Weapon.ROPE));
		weapons.add(new WeaponToken(CluedoGame.Weapon.SPANNER));
		return weapons;
	}
	
	/**
	 * Creates a game.
	 * @return
	 */
	private CluedoGame newGame(){
		return new CluedoGame(6, getAllCharacters(), "gameBoard.txt");
	}
	
	/**
	 * Creates a board.
	 * @return
	 */
	private Board newBoard(CluedoGame game){
		return new Board(game, "gameBoard.txt");
	}
	
	private TextClient cluedoGame(){
		TextClient client = new TextClient();
		CluedoGame game = newGame();
		client.setGame(game);
		client.setBoard(game.board());
		return client;
	}
}

