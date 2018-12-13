package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Contains the data for a player
 * @author Alex Pickering
 */
public class PlayerData {
	int wins, losses, draws, plays;
	String name;
	File saveFile;
	
	/**
	 * Default constructor. Creates new data for the player
	 * @param name The player's name
	 */
	public PlayerData(String name) {
		this.name = name;
		
		saveFile = new File("players/" + name + ".pd");
		
		wins = losses = draws = plays = 0;
	}
	
	/**
	 * Load constructor. Loads player data from the file specified
	 * @param f The file to read from
	 * @throws IOException 
	 */
	public PlayerData(File f) throws IOException {
		saveFile = f;
		
		String[] dat = readData().split(" ");
		
		name = dat[0];
		wins = Integer.parseInt(dat[1]);
		losses = Integer.parseInt(dat[2]);
		draws = Integer.parseInt(dat[3]);
		plays = Integer.parseInt(dat[4]);
	}
	
	public PlayerData(String name, int wins, int losses, int draws, int plays) {
		this.name = name;
		this.wins = wins;
		this.losses = losses;
		this.draws = draws;
		this.plays = plays;
		
		saveFile = new File("players/" + name + ".pd");
	}
	
	/**
	 * Increments wins and plays
	 */
	public void incrementWins() {
		wins++;
		plays++;
	}
	
	/**
	 * Increments losses and plays
	 */
	public void incrementLosses() {
		losses++;
		plays++;
	}
	
	/**
	 * Increments draws and plays
	 */
	public void incrementDraws() {
		draws++;
		plays++;
	}
	
	/**
	 * Reads the player's data to a string
	 * @param f The file to read from
	 * @return The player's data, read from the file
	 * @throws IOException 
	 */
	String readData() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(saveFile));
		String r = "";
		
		for(int i = 0; i < 5; i++) {
			String s = br.readLine();
			
			if(s.equals(null)) {
				br.close();
				throw new IOException("File format incorrect - Not enough data");
			}
			
			r += " " + s;
		}
		
		br.close();
		
		return r;
	}
	
	/**
	 * Saves the player's data
	 * @param f The file to write to
	 * @throws IOException
	 */
	public void save() throws IOException {
		if(!saveFile.exists()) saveFile.createNewFile();
		
		PrintWriter pr = new PrintWriter(new FileWriter(saveFile, false), true);
		
		pr.println(name);
		pr.println(wins);
		pr.println(losses);
		pr.println(draws);
		pr.println(plays);
		
		pr.close();
	}
	
	/**
	 * Gets the player's win count
	 * @return The player's wins
	 */
	public int getWins() {
		return wins;
	}
	
	/**
	 * Gets the player's loss count
	 * @return The player's losses
	 */
	public int getLosses() {
		return losses;
	}
	
	/**
	 * Gets the player's draw count
	 * @return The player's draws
	 */
	public int getDraws() {
		return draws;
	}
	
	/**
	 * Gets the player's play count
	 * @return The player's plays
	 */
	public int getPlays() {
		return plays;
	}
	
	/**
	 * Gets the player's name
	 * @return The player's name
	 */
	public String getName() {
		return name;
	}
}
