import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sudoku.SudokuBoardReader;
import sudoku.SudokuFile;

public class SudokuGeneratorTester {
	public static final String version = "Tester_v_0_2";

	String exePath;
	String input; 
	String output;
	int timeLimit;
	String types;
	Map<String, Boolean> optMap;

	public static final String total_start = "TOTAL_START";
	public static final String preprocessing_start = "PREPROCESSING_START";
	public static final String preprocessing_done = "PREPROCESSING_DONE";
	public static final String search_start = "SEARCH_START";
	public static final String search_done = "SEARCH_DONE";
	public static final String solution_time = "SOLUTION_TIME";
	public static final String status = "STATUS";
	public static final String solution = "SOLUTION";
	public static final String count_node = "COUNT_NODES";
	public static final String count_deadends = "COUNT_DEADENDS";




	public static void main(String[] args) {


		Map<String, Boolean> optMap = new HashMap<String, Boolean>();

		if(args.length < 4){
			System.err.println("Usage : java -jar SudokuTeneratorTester.jar exeFile inputPath outputPath timeLimit FC AP MAC ...");
			System.exit(0);
		}

		String exe = args[0];//"MySudokuGenerator.jar";//
		String input = args[1];//"testInput";
		String output = args[2];
		int timeLimit = Integer.parseInt(args[3]);
		String types = "";

		if(args.length > 4){
			for(int i = 4 ; i < args.length ; i++){
				types += args[i]+" ";	
				optMap.put(args[i], true);
			}
			types = types.trim();
		}


		SudokuGeneratorTester tester = new SudokuGeneratorTester(exe, input, output, timeLimit, types, optMap);
		tester.runGenerator();
	}

	public SudokuGeneratorTester(String exePath, String input, String output, 
			int timeLimit, String types, Map<String, Boolean> optMap){
		this.exePath = exePath;
		this.input = input;
		this.output = output;
		this.timeLimit = timeLimit;
		this.types = types;
		this.optMap = optMap;
	}

	public void runGenerator(){

		//Read input
		List<Object> testCases = new ArrayList<Object>();



		if(optMap.get(TestCase.opt_gen) != null){
			try{
				FileReader fr = new FileReader(input);
				BufferedReader br = new BufferedReader(fr);
				String currentLine;
				while((currentLine = br.readLine()) != null){
					String[] param = currentLine.trim().split("[^0-9]");
					if(param.length != 4){
						System.err.println("Number format mismatched. Skipped. It must have 4 numbers M N P Q in a row.");
					}else{
						testCases.add(new TestCase(Integer.parseInt(param[0]), Integer.parseInt(param[1]), Integer.parseInt(param[2]), Integer.parseInt(param[3])));
					}
				}
				br.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			testCases.add(SudokuBoardReader.readFile(input));
		}


		

		for(int i = 0 ; i < testCases.size(); i++){
			//Write test case
			String tmpInput = input+"_"+i;
			String tmpOutput = "out_"+tmpInput;


			//Write Temporal Input
			if(optMap.get(TestCase.opt_gen) != null){
				try {
					PrintWriter pw = new PrintWriter(new FileWriter(tmpInput));
					pw.println(testCases.get(i));
					pw.flush();
					pw.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}else{
				fileCopy(input, tmpInput);
			}


			//Run
			List<String> cmdList = new ArrayList<String>();
			String exeString = exePath+" "+tmpInput+" "+tmpOutput+" "+timeLimit+" "+types;
			if(exePath.endsWith(".class")){
				exeString = "java "+exeString;
				cmdList.add(exeString);
			}else if(exePath.endsWith(".jar")){
				exeString = "java -jar "+exeString;
				cmdList.add(exeString);
			}else if(exePath.endsWith(".py")){
				exeString = "python "+exeString;
				cmdList.add("module load python/3.3.5");
				cmdList.add("alias python=python3");
				cmdList.add(exeString);
			}else{
				exeString = "./"+exeString;
				cmdList.add(exeString);
			}
			
			
			
			for(String exeCmd : cmdList){
				try {
					System.out.println("Executed Command : "+exeCmd);
					Process p =Runtime.getRuntime().exec(exeCmd);
					p.waitFor();

					//Process proc = rt.exec(commands);

					BufferedReader stdInput = new BufferedReader(new 
							InputStreamReader(p.getInputStream()));

					BufferedReader stdError = new BufferedReader(new 
							InputStreamReader(p.getErrorStream()));

					// read the output from the command
					System.out.println(">>Here is the standard output of the command (if any):\n");
					String s = null;
					while ((s = stdInput.readLine()) != null) {
						System.out.println(s);
					}

					// read any errors from the attempted command
					System.out.println(">>Here is the standard error of the command (if any):\n");
					while ((s = stdError.readLine()) != null) {
						System.out.println(s);
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
			if(optMap.get(TestCase.opt_gen)!=null){
				validCheckGEN(i, tmpOutput);	
			}else{
				validCheck(i, testCases.get(i), tmpOutput);
			}


			//If solve, we consider only one case.
			if(optMap.get(TestCase.opt_gen) == null) break;
		}
	}

	public void validCheck(int i, Object testCase, String tmpOutput){
		ResultSet resultSet = new ResultSet(testCase);
		try (Reader reader = new FileReader(tmpOutput)) {
			try(BufferedReader br = new BufferedReader(reader)){

				String line;
				int lineCounter = 0;

				while((line = br.readLine()) != null)
				{
					//System.out.println("LINE = "+line);
					resultSet.feedParse(line);
				}



				ValidityChecker checker = new ValidityChecker(resultSet);
				print(checker.toString());
				System.out.println(checker.toString());
			}
		}catch (IOException e1) {
			// TODO Auto-generated catch block
			print("Invalid file:"+tmpOutput+". Skipping to the next file.\n");
			e1.printStackTrace();
		}
	}

	public void validCheckGEN(int i, String tmpOutput){
		boolean isValid = true; 
		int N, P, Q;
		N = 0;
		P = 0;
		Q = 0;
		int[][] board = null;

		try (Reader reader = new FileReader(tmpOutput)) {
			try(BufferedReader br = new BufferedReader(reader)){

				String line;
				int lineCounter = 0;

				while((line = br.readLine()) != null)
				{	
					String[] lineParts = line.split("\\s+");
					if(lineCounter == 0)
					{
						if(lineParts.length != 3){
							print("Header has wrong format. Must have N P Q.");
						}else{
							N = Integer.parseInt(lineParts[0]);
							P = Integer.parseInt(lineParts[1]);
							Q = Integer.parseInt(lineParts[2]);

							board = new int[N][N];
						}
					}
					else
					{
						if(lineParts.length != N){
							isValid = false;
							print(lineCounter+"-th row is incomplete. Please be advised");
						}else{
							for(int ii = 0 ; ii < N; ii++){
								board[lineCounter-1][ii] = Integer.parseInt(lineParts[ii]);
							}
						}
						//parseLineOfSudokuBoard(sF, lineParts, lineCounter-1);//obo due to parameters taking up first line
					}
					lineCounter++;
				}
				if(lineCounter == 0)
				{
					isValid = false;
					print("Input file \""+tmpOutput+"\" was empty");
				}
				else if(lineCounter < N || board == null)
				{
					isValid = false;
					print("Incomplete or Emtpy board for file " + tmpOutput+". Please be advised");
				}

				if(isValid){
					print((i+1)+"-th sudoku file was correctly generated.");
				}else{
					print((i+1)+"-th sudoku file has problem.");
				}



				//File f = new File(tmpInput);
				//f.delete();
				//f = new File(tmpOutput);
				//f.delete();

				//return sF;
			} 
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			print("Invalid file:"+tmpOutput+". Skipping to the next file.\n");
			e1.printStackTrace();
		} catch (NumberFormatException e2) {
			e2.printStackTrace();
		}
	}

	boolean isAppend = false;
	public void print(String message){
		if(output == null){
			System.out.println(message);
		}else{
			//Write output
			try {
				PrintWriter pw = new PrintWriter(new FileWriter(output, isAppend));
				if(!isAppend) isAppend = true;
				pw.println(message);
				pw.flush();
				pw.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	public static void fileCopy(String inFileName, String outFileName) {
		try {
			FileInputStream fis = new FileInputStream(inFileName);
			FileOutputStream fos = new FileOutputStream(outFileName);

			int data = 0;
			while((data=fis.read())!=-1) {
				fos.write(data);
			}
			fis.close();
			fos.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}




