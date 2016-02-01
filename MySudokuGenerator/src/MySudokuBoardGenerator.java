
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import sudoku.SudokuBoardGenerator;
import sudoku.SudokuFile;


public class MySudokuBoardGenerator {

//	public static SudokuFile generateBoard(int N, int p, int q, int numAssignments)
//	{	
//		return generateBoard(N, p, q, numAssignments, 5000);
//	}
//	
//	public static SudokuFile generateBoard(int N, int p, int q, int numAssignments, long timeout)
//	{
//		//given a SudokuFile with N, P, Q, creates a board with the given params
//		//and assigns it to the board of the SudokuFile.
//		//timeout represents the time in ms allowed to created the SudokuFile
//		SudokuFile sf = new SudokuFile(N, p, q);
//		int[][] tempBoard = new int[sf.getN()][sf.getN()];
//		if(numAssignments > sf.getN()*sf.getN())
//		{
//			System.out.println("Number of assignments exceeds available spaces in board. Returning SudokuFile with an empty board");
//			return sf;
//		}
//		Random integerPicker = new Random();
//		long startTime = System.currentTimeMillis();
//		long currentTime;
//		for(int i = 0; i < numAssignments; i++)
//		{
//			int randomRow = integerPicker.nextInt(sf.getN()); //0 to N-1
//			int randomColumn = integerPicker.nextInt(sf.getN()); //0 to N-1
//			int randomAssignment = integerPicker.nextInt(sf.getN())+1; //1 to N
//			if(tempBoard [randomRow][randomColumn] == 0 && checkConstraints(randomRow, randomColumn, randomAssignment, sf, tempBoard) )
//			{
//				tempBoard[randomRow][randomColumn] = randomAssignment;
//			}
//			else
//			{
//				i--;
//				currentTime = System.currentTimeMillis();
//				if(currentTime - startTime > timeout)
//				{
//					System.out.println("Timeout at "+i+" elements");
//					tempBoard = new int[sf.getN()][sf.getN()];
//					break;
//				}
//			}
//		}
//		sf.setBoard(tempBoard);
//		return sf;
//	}
//
//
//	private static boolean checkConstraints(int row, int col, int value, SudokuFile sf, int[][] board)
//	{
//		if(checkRow(row, value, sf.getN(), board) 
//				&& checkColumn(col, value, sf.getN(), board) 
//				&& checkBox(row, col, value, sf.getP(), sf.getQ(), board))
//		{
//			return true;
//		}
//		
//		return false; 
//	}
//
//	public static boolean checkRow(int row, int value, int N, int[][] board)
//	{
//		for(int i = 0; i < N; i++)
//		{
//			if(board[row][i] == value)
//			{
//				return false;
//			}
//		}
//		return true;
//	}
//
//	public static boolean checkColumn(int column, int value, int N, int[][] board)
//	{
//		for (int i = 0; i < N; i++)
//		{
//			if(board[i][column] == value)
//			{
//				return false;
//			}
//		}
//
//		return true;
//	}
//
//	public static boolean checkBox(int row, int column, int value, int p, int q, int[][] board)
//	{
//		int rDiv = row/p;
//		int cDiv = column/q;
//		for(int i = rDiv * p; i < (rDiv + 1) * p; i++)
//		{
//			for(int j = cDiv * q; j < (cDiv + 1) * q; j++)
//			{
//				try{
//				if(board[i][j] == value)
//				{
//					return false;
//				}
//				}catch(ArrayIndexOutOfBoundsException e)
//				{
//					System.out.println(p + " "+ q);
//				}
//			}
//		}
//		return true;
//	}
	public static void writeFile(String filePath, SudokuFile sf)
	{
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(filePath));
			pw.println(sf.getN()+" "+sf.getP()+" "+sf.getQ());
			int[][] board = sf.getBoard();
			for(int i = 0 ; i < board.length ; i++){
				for(int j = 0 ; j < board[i].length ; j++){
					if(j > 0)
						pw.print(" ");
					pw.print(board[i][j]);
				}
				pw.println("");
			}
			pw.flush();
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public static void writeFile(String filePath, ResultSet resultSet){
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(filePath), true);
			pw.print(resultSet.toString());
			pw.flush();
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public static final String opt_none = "NONE";
	public static final String opt_gen = "GEN";
	public static final String opt_fc = "FC";
	public static final String opt_acp = "ACP";
	public static final String opt_mac = "MAC";
	public static final String opt_mrv = "MRV";
	public static final String opt_dh = "DH";
	public static final String opt_lcv = "LCV";
	
	
	
	public static void main(String[] args){
		
//		String a = "GEN";
//		String[] aa = a.split("\\|");
//		System.out.println(aa[0]);
//		
//		if(1==1) return;
		int N, p, q, numAssignments;
		Map<String, Boolean> optMap = new HashMap<String, Boolean>();
		if(args.length < 3){
			System.err.println("Usage : java -jar exeFile inputPath outputPath timeLimit <FC|ACP|MAC|MRV|DH|LCV>");
			System.exit(0);
		}
		
		String input = args[0];//"testinput";
		String output = args[1];//"testoutput";
		int timeLimit = Integer.parseInt(args[2]);
		
		if(args.length == 4){
			String types = args[3];
			String[] typesParts = types.split("\\|");
			for(int i = 0 ; i < typesParts.length ; i++){
				//System.out.println(typesParts[i]+"=SET!");
				optMap.put(typesParts[i], true);
			}
		}
		
		try{
			FileReader fr = new FileReader(input);
			BufferedReader br = new BufferedReader(fr);
			String currentLine;
			while((currentLine = br.readLine()) != null){
				String[] param = currentLine.trim().split(" ");
				if(param.length != 4){
					System.err.println("Number format mismatched. Skipped. It must have 4 numbers M N P Q in a row.");
					System.exit(0);
				}else{
					numAssignments = Integer.parseInt(param[0]);
					N = Integer.parseInt(param[1]);
					p = Integer.parseInt(param[2]);
					q = Integer.parseInt(param[3]);
					
					if(optMap.get(opt_gen) != null){
						SudokuFile sFile = SudokuBoardGenerator.generateBoard(N, p, q, numAssignments);
						System.out.println(sFile);
						writeFile(output, sFile);
					}else {
						ResultSet resultSet = new ResultSet(N);
						resultSet.setRandData();
						System.out.println(resultSet);
						writeFile(output, resultSet);
					}
				}
			}
			br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
