package cluedo.board;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import cluedo.control.CluedoError;
import cluedo.control.CluedoGame;
import cluedo.control.CluedoGame.Room;
import cluedo.tokens.Card;
import cluedo.tokens.CharacterToken;
import cluedo.tokens.GameToken;

/**
 * Holds the tokens and logic for moving the tokens around the board.
 * 
 * @author Patrick Evans and Maria Legaspi
 *
 */
public class Board {

	private int height;
	private int width;
	
	private int numPlayers;
	private List<CharacterToken> activePlayers;
	private Card[] solution;
	
	private Tile[][] board; // the board is a 2D array of tiles
	
	/**
	 * Creates the board by reading a given file.
	 * @param game
	 * @param boardFile
	 */
	public Board(CluedoGame game, String boardFile) {
		if(game==null||boardFile==null|boardFile.length()<1)
			throw new CluedoError("Invalid board arguments");
		this.numPlayers = game.numPlayers();
		this.activePlayers = game.players();
		this.solution = game.Solution();
		this.board = new Tile[25][25];
		
		// reading the board file
		Scanner scanner = null;
		try{
			scanner = new Scanner(new File(boardFile));
			for(height=0; scanner.hasNextLine(); height++){
				char[] line = scanner.nextLine().toCharArray();
				for(width=0; width < line.length; width++){
						
					// read the text character
					char c = line[width];
					Position pos = new Position(height, width, c);
					
					// digits are starting positions
					if(Character.isDigit(c)){
						// starting tile
						HallwayTile startTile = new HallwayTile(pos, ' ');
						for(CharacterToken player : activePlayers){
							if(player.getUid() == Character.getNumericValue(c)){
								// set player start location
								startTile.setToken(player);
								player.setXPos(width);
								player.setYPos(height);
							}
						}
						board[height][width] = startTile;
					} else {
						board[height][width] = getTile(c, pos);
					}
				}
			}
		} catch(IOException e) {
			System.out.println("Error processing board file");
		}
	}
	
	/**
	 * Returns true if a given token is in any room
	 * @param token
	 * @return
	 */
	public boolean inRoom(GameToken token){
		if(token==null)
			return false;
		int Xpos = token.getXPos();
		int Ypos = token.getYPos();
		Tile t = board[Ypos][Xpos];
		return(t instanceof RoomTile);
	}
	
	/**
	 * Returns true if a given token is in a corner room
	 * @param token
	 * @return
	 */
	public boolean inCornerRoom(GameToken token){
		if(token==null)
			return false;
		int Xpos = token.getXPos();
		int Ypos = token.getYPos();
		Tile t = board[Ypos][Xpos];
		if(t instanceof RoomTile){
			RoomTile r = (RoomTile)t;
			if(r.isCornerRoom())
				return true;
		}
		return false;
	}
	
	/**
	 * Returns true if a given token is in any doorway
	 * @param token
	 * @return
	 */
	public boolean inDoorway(GameToken token){
		if(token==null)
			return false;
		int Xpos = token.getXPos();
		int Ypos = token.getYPos();
		Tile t = board[Ypos][Xpos];
		return(t instanceof DoorwayTile);
	}

	/**
	 * Returns true if a given player can move north on the board
	 * @param token
	 * @return
	 */
	public boolean canMoveNorth(CharacterToken token){	
		if(token==null)
			return false;
		// already in north most square
		if(token.getYPos() - 1 < 0){
			return false;
		}
		
		// cannot move if north is a room, invalid tile, or entrance which is not north
		int xpos = token.getXPos();
		int ypos = token.getYPos();
		Tile t = board[ypos-1][xpos];
		char symbol = t.getSymbol();
		// cannot move if next tile already contains a token
		if(t.getToken()!=null){ 
			return false;
		}
		if(inDoorway(token) || inRoom(token)){
			return Character.isUpperCase(symbol) || symbol == 'n' 
					|| symbol == 'e' || symbol == 's' || symbol == 'w';
		}
		if(Character.isUpperCase(symbol) || symbol == 'x' 
				|| symbol == 'e' || symbol == 's' || symbol == 'w'){
			return false;
		}
		return true;
	}
	
	/**
	 * Returns true if a given player can move east on the board
	 * @param token
	 * @return
	 */
	public boolean canMoveEast(CharacterToken token){	
		if(token==null)
			return false;
		// already in east most square
		if(token.getXPos() + 1 >= width){
			return false;
		}
		
		// cannot move if east is a room, invalid tile, or entrance which is not north
		int xpos = token.getXPos();
		int ypos = token.getYPos();
		Tile t = board[ypos][xpos + 1];
		char symbol = t.getSymbol();
		// cannot move if next tile already contains a token
		if(t.getToken()!=null){ 
			return false;
		}
		if(inDoorway(token) || inRoom(token)){
			return Character.isUpperCase(symbol) || symbol == 'n' 
					|| symbol == 'e' || symbol == 's' || symbol == 'w';
		}
		if(Character.isUpperCase(symbol) || symbol == 'x' 
				|| symbol == 'n' || symbol == 's' || symbol == 'w'){
			return false;
		}
		return true;
	}
	
	/**
	 * Returns true if a given player can move south on the board
	 * @param token
	 * @return
	 */
	public boolean canMoveSouth(CharacterToken token){
		if(token==null)
			return false;	
		// already in south most square
		if(token.getYPos() + 1 >= height){
			return false;
		}
		
		// cannot move if south is a room, invalid tile, or entrance which is not south
		int xpos = token.getXPos();
		int ypos = token.getYPos();
//		Tile t = board[ypos+1][xpos];
		Tile t = getTile(xpos,ypos+1);
		if(t==null)
			return false;
		char symbol = t.getSymbol();
		// cannot move if next tile already contains a token
		if(t.getToken()!=null){ 
			return false;
		}
		if(inDoorway(token) || inRoom(token)){
			return Character.isUpperCase(symbol) || symbol == 'n' 
					|| symbol == 'e' || symbol == 's' || symbol == 'w';
		}
		if(Character.isUpperCase(symbol) || symbol == 'x' 
				|| symbol == 'n' || symbol == 'e' || symbol == 'w'){
			return false;
		}
		return true;
	}
	
	/**
	 * Returns true if a given player can move west on the board
	 * @param token
	 * @return
	 */
	public boolean canMoveWest(CharacterToken token){	
		if(token==null)
			return false;
		// already in west most square
		if(token.getXPos() - 1 < 0){
			return false;
		}
		
		// cannot move if west is a room, invalid tile, or entrance which is not north
		int xpos = token.getXPos();
		int ypos = token.getYPos();
		Tile t = board[ypos][xpos - 1];
		char symbol = t.getSymbol();
		// cannot move if next tile already contains a token
		if(t.getToken()!=null){ 
			return false;
		}
		if(inDoorway(token) || inRoom(token)){
			return Character.isUpperCase(symbol) || symbol == 'n' 
					|| symbol == 'e' || symbol == 's' || symbol == 'w';
		}
		if(Character.isUpperCase(symbol) || symbol == 'x' 
				|| symbol == 'n' || symbol == 'e' || symbol == 's'){
			return false;
		}
		return true;
	}
	
	/**
	 * Moves player one position up
	 * @param player
	 */
	public void moveNorth(CharacterToken player) {
		if(player==null)
			return;
		Point newPos = new Point(player.getXPos(), player.getYPos()-1);
		move(newPos, player);
	}	

	/**
	 * Moves player one position to the right
	 * @param player
	 */
	public void moveEast(CharacterToken player){
		if(player==null)
			return;
		Point newPos = new Point(player.getXPos()+1, player.getYPos());
		move(newPos, player);
	}
	
	/**
	 * Moves player one position down
	 * @param player
	 */
	public void moveSouth(CharacterToken player) {
		if(player==null)
			return;
		Point newPos = new Point(player.getXPos(), player.getYPos()+1);
		move(newPos, player);
	}
	
	/**
	 * Moves player one position left
	 * @param player
	 */
	public void moveWest(CharacterToken player) {
		if(player==null)
			return;
		Point newPos = new Point(player.getXPos()-1, player.getYPos());
		move(newPos, player);
	}
	
	/**
	 * Moves player to opposite room.
	 * @param player
	 */
	public void useStairs(CharacterToken player){
		if(player==null)
			return;
		// checks player is in a corner room
		Tile tile = getTile(player.getXPos(), player.getYPos());
		if(!(tile instanceof RoomTile))
			throw new CluedoError("No stairs in this area: " + tile.toString());
		if(!(((RoomTile)tile).isCornerRoom()))
			throw new CluedoError("No stairs in this area: " + tile.toString());
		RoomTile rTile = (RoomTile)tile;
		// finds the opposite room and moves player to that room
		CluedoGame.Room opposite = rTile.oppositeRoomPos();
		moveIntoRoom(player, opposite);
	}
	
	/**
	 * Sets player's position within the token and the board.
	 * @param newPos
	 * @param player
	 */
	public void move(Point newPos, GameToken player){
		if(newPos == null || newPos.x < 0 || newPos.x > 24 ||
				newPos.y < 0 || newPos.y > 24 || player==null)
			return;
		// set original pos to null
		board[player.getYPos()][player.getXPos()].setToken(null);	
		// change player position
		player.setXPos(newPos.x);
		player.setYPos(newPos.y);
		// set player in new position on board
		board[newPos.y][newPos.x].setToken(player);	
	}
	
	/**
	 * Returns the tile at the specified position
	 * @param x
	 * @param y
	 * @return
	 */
	public Tile getTile(int x, int y){
		if(x<0 || x>24||y<0||y>24)
			return null;
		return board[y][x];
	}
	
	public void moveIntoRoom(GameToken token, Room r) {
		if(r == null || token == null)
			throw new CluedoError("Null parameters: moveIntoRoom()");
		// finds a tile in the given room
		for(int i=0; i<board[0].length; i++){
			for(int j=0; j<board.length; j++){
				Tile t = board[i][j];
				if(t instanceof RoomTile){
					RoomTile rTile = (RoomTile)t;
					// if this tile matches the room we're looking for
					if(rTile.name() == r && t.getToken() == null){
						// move the token into this tile
						move(new Point(j, i), token);
						return; // done
					}
				}		
			}
		}
	}

	/**
	 * Returns the symbol associated with a given room.
	 * @param r
	 * @return
	 */
	public char getRoomSymbol(Room r) {
		switch(r.toString()){
			case "KITCHEN" :
				return 'K';
			case "BALL ROOM":
				return 'B';
			case "CONSERVATORY":
				return 'C';
			case "DINING ROOM":
				return 'N';
			case "BILLIARD ROOM":
				return 'I';
			case "LIBRARY":
				return 'L';
			case "LOUNGE":
				return 'O';
			case "HALL":
				return 'H';
			case "STUDY":
				return 'S';
			default:
				return ' ';
		}
	}
	
	/**
	 * Return a new tile corresponding to a specified character.
	 * @param c tile character
	 * @param p position on board
	 * @return tile
	 */
	private Tile getTile(char c, Position p) {
		switch(c){
			case 'x':
				return new WallTile(p);
			case 'n':
			case 'e':
			case 's':
			case 'w':
				return new DoorwayTile(p, c);
			case ' ':
				return new HallwayTile(p, c);
			case 'K':
				return new RoomTile(p, Room.KITCHEN, 'K');
			case 'B':
				return new RoomTile(p, Room.BALL_ROOM, 'B');
			case 'C':
				return new RoomTile(p, Room.CONSERVATORY, 'C');
			case 'N':
				return new RoomTile(p, Room.DINING_ROOM, 'N');
			case 'I':
				return new RoomTile(p, Room.BILLIARD_ROOM, 'I');
			case 'L':
				return new RoomTile(p, Room.LIBRARY, 'L');
			case 'O':
				return new RoomTile(p, Room.LOUNGE, 'O');
			case 'H':
				return new RoomTile(p, Room.HALL, 'H');
			case 'S':
				return new RoomTile(p, Room.STUDY, 'S');
			default:
				throw new CluedoError("Error: tile character not recognised");
		}
	}
	
	/**
	 * Prints the state of the board from the 2D board array
	 */
	public void printBoard(){
		for(int i=0; i<board[0].length; i++){
			for(int j=0; j<board.length; j++){
				if(board[i][j] != null){
					System.out.print(board[i][j].getSymbol());
				} else {
					System.out.print(" ");
				}		
			}
			System.out.print("\n");
		}
	}
	
	/**
	 * Prints the board and returns an empty string.
	 */
	public String toString(){
		this.printBoard();
		return " ";
	}

	/**
	 * Returns a list of all character tokens.
	 * @return
	 */
	public List<CharacterToken> players() {
		return activePlayers;
	}

}
