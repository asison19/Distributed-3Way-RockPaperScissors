import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

/*
 * client rock paper scissors player
 */
public class RPS_Player {
	
	private static int numGames;
	private static int portNumber;
    private static BufferedReader input;
    private static PrintWriter output;
    
	public static void main(String[] args) {
		//connect to server
		try {
			portNumber = Integer.parseInt(args[0]);
			Socket socket = new Socket("localhost", portNumber);
			
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output = new PrintWriter(socket.getOutputStream(), true);
			
			PlayRPS();
			input.close();
			output.close();
			socket.close();
		} catch (ArrayIndexOutOfBoundsException | IOException e) {
			e.printStackTrace();
		} 
		
	}
	
	private static void PlayRPS() {
		
		try {
			numGames = Integer.parseInt(input.readLine());
			int[] hand = new int[numGames];
			System.out.println("Num games: " + numGames);
			for(int i = 0; i < numGames; i++) {
				hand[i] = getRPS();
				output.println(hand[i]);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			System.out.println("My Score is: " + getMyScoreFromServer());
		}
		
	}
	
	private static int getMyScoreFromServer() {
		int myScore = 0;
		try {
			myScore = Integer.parseInt(input.readLine());
		} catch (IOException e) {
			System.err.println("Could not get score.");
			e.printStackTrace();
		}
		return myScore;
	}
	
	private static int getRPS() {
		Random rand = new Random();
		return Math.abs(rand.nextInt() % 3);
	}
}
