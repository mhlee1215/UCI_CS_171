import java.io.BufferedReader;
import java.io.FileReader;

public class SudokuPlate{
	int[][] plate;
	int numToken;
	int bHor;
	int bVer;
	boolean isValid = true;

	public SudokuPlate(String fileName){
		
		try{
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			String currentLine;
			int lineCnt = 0;
			while((currentLine = br.readLine()) != null){
		        String[] param = currentLine.trim().split(" ");
		        if(lineCnt == 0){
		        	if(param.length != 3){
		        		System.err.println("Number format mismatched. It must have 4 numbers M N P Q in a row.");
		        		isValid = false;
		        	}else{
		        		
		        	}
		        }else{
		        	
		        }
		        
		        lineCnt++;
			}
			br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		plate = new int[numToken][numToken];
	}
	
	

	boolean isValid(){
		
		if(!isValid) return isValid;
		else{
			
		}
		
		
		
		return false;
	}


}