/*
 * @author Andrew Sison
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

/*
 * Server rock paper scissors player
 * Is also the first player
 */
public class RockPaperScissors {
	
	//private static int rock = 0;
	//private static int paper = 1;
	//private static int scissors = 2;
	private static int portNumber0;
	private static int numGames;
	private static ServerSocket listener;
	private static Socket socket1;
	private static Socket socket2;
	private static BufferedReader input1;
	private static BufferedReader input2;
	private static PrintWriter output1;
	private static PrintWriter output2;
	
	/*
	 * @param 
	 * args[0] is the number of games to play
	 * args[1] portNumber
	 */
	public static void main(String args[]) {
		
		try {
			//get the number of games to play
			numGames = Integer.valueOf(args[0]);
			
			//get the port number
			portNumber0 = Integer.valueOf(args[1]);
			
			connectSocket();
			playRPS();
		} catch (ArrayIndexOutOfBoundsException | IOException e) {
			//System.out.println("Number of games not specified");
			e.printStackTrace();
		} finally {
			try {
				input1.close();
				input2.close();
				output1.close();
				output2.close();
				socket1.close();
				socket2.close();
				//closeSockets();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private static void closeSockets() throws IOException {
		boolean isSocket1Closed = false;
		boolean isSocket2Closed = false;
		do {
			if(socket1.isInputShutdown()) {
				socket1.close();
				isSocket1Closed = true;
			}
		} while(!isSocket1Closed);
		
		do {
			if(socket2.isInputShutdown()) {
				socket2.close();
				isSocket2Closed = true;
			}
		} while(!isSocket2Closed);
		
	}

	private static void connectSocket() throws IOException {
		try {
			
			listener = new ServerSocket(portNumber0);
			
			boolean isSocket1Connected = false;
			boolean isSocket2Connected = false;
			do { 
				try {
					System.out.println("Waiting for 2nd player");
					socket1 = listener.accept();
					input1 = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
					isSocket1Connected = true;
				} catch (ConnectException e) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			} while (!isSocket1Connected);
			do { 
				try {
					System.out.println("Waiting for 3rd player");
					socket2 = listener.accept();
					input2 = new BufferedReader(new InputStreamReader(socket2.getInputStream()));
					isSocket2Connected = true;
				} catch (ConnectException e) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			} while (!isSocket2Connected);
			
			//setup Output
			output1 = new PrintWriter(socket1.getOutputStream(), true);
			output2 = new PrintWriter(socket2.getOutputStream(), true);
			
			//tell clients number of games to play, ie number of hands they must make
			System.out.println("The number of games we're playing is: " + numGames);
			output1.println(Integer.toString(numGames));
			output2.println(Integer.toString(numGames));
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
	}

	private static void playRPS() throws IOException {
		
		int[] hand0 = new int[numGames];
		int[] hand1 = new int[numGames];
		int[] hand2 = new int[numGames];
		
		//create my hands
		for(int i = 0; i < numGames; i++) {
			hand0[i] = getRPS();
		}
		
		//get other player's hands
		for(int i = 0; i < numGames; i++) {
			hand1[i] = Integer.parseInt(input1.readLine()); 
		}
		for(int i = 0; i < numGames; i++) {
			hand2[i] = Integer.parseInt(input2.readLine());
		}
		
		//print out hands
		for(int i = 0; i < numGames; i++) {
			System.out.println("Round: " + (i+1) 
					+ "\t\n Player One got: " + translateHand(hand0[i])
					+ "\t\n Player Two got: " + translateHand(hand1[i])
					+ "\t\n Player Three got: " + translateHand(hand2[i]));
		}
		System.out.println(); //end line
		//tell players their score
		int player1_Score = judgeHand(hand1, hand0, hand2, "Two");
		System.out.println("Player 2's Score is: " + player1_Score);
		output1.println(player1_Score);
		int player2_Score = judgeHand(hand2, hand0, hand1, "Three");
		System.out.println("Player 3's Score is: " + player2_Score);
		output2.println(player2_Score);
		
		//get my score
		int myScore = judgeHand(hand0, hand1, hand2, "One");
		System.out.println("My Score, Player One, is: " + myScore);
	}
	
	//get random number from 0 to 2, which are stand ins for rock, paper, and scissors respectively.
	private static int getRPS() {
		Random rand = new Random();
		return Math.abs(rand.nextInt() % 3);
	}
	
	private static int judgeHand(int[] h0, int[] h1, int[] h2, String player) {
		int points = 0;
		for(int i = 0; i < numGames; i++) {
			if(h0[i] != h1[i] && h0[i] != h2[i] && h1[i] != h2[i]) {
				//no points awarded if they all got different choices
			} else if(h0[i] == 0) {
				if(h1[i] == 2)
					points++;
				if(h2[i] == 2)
					points++;		
			} else if(h0[i] == 1) {
				if(h1[i] == 0)
					points++;
				if(h2[i] == 0)
					points++;
			}else if(h0[i] == 2) {
				if(h1[i] == 1)
					points++;
				if(h2[i] == 1)
					points++;
			}
		}
		return points;
	}
	
	//0 for rock, 1 for paper, and 2 for scissors
	private static String translateHand(int hand) {
		String str = null;
		switch(hand) {
			case 0:
				str = "rock";
				break;
			case 1:
				str = "paper";
				break;
			case 2:
				str = "scissors";
				break;
			
		}
		
		return str;
	}
}

