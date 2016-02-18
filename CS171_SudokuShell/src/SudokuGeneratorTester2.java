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

public class SudokuGeneratorTester2 {

	public static void main(String[] args) {

		
		String exeCmd = "java -version";
		
		if("java".equals(args[0]))
			exeCmd = "java -version";
		else if("python".equals(args[0]))
			exeCmd = "python3 -V";
		else if("gcc".equals(args[0]))
			exeCmd = "gcc --version";
			
		System.out.println("Executed Command : "+exeCmd);
		Process p;
		try {
			p = Runtime.getRuntime().exec(exeCmd);
			p.waitFor();

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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}




