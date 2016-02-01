import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidityChecker {
	
	
	
	HashMap<String, String> validMap = null;
	ResultSet resultSet;
	public static final String passString = "[pass]";
	public static final String failString = "[fail]";
	public static final String initString = "[init]";
	
	public ValidityChecker(ResultSet resultSet){
		this.resultSet = resultSet;
		
		int[][] board = (int[][]) resultSet.resultMap.get(ResultSet.solution);
		int N = resultSet.getN();
		validMap = new HashMap<String, String>();
		
		validMap.put(ResultSet.total_start, initString);
		validMap.put(ResultSet.preprocessing_start, initString);
		validMap.put(ResultSet.preprocessing_done, initString);
		validMap.put(ResultSet.search_start, initString);
		validMap.put(ResultSet.search_done, initString);
		validMap.put(ResultSet.solution_time, initString);
		validMap.put(ResultSet.status, initString);
		validMap.put(ResultSet.solution, initString);
		validMap.put(ResultSet.count_node, initString);
		validMap.put(ResultSet.count_deadends, initString);
		
		if((int)(resultSet.resultMap.get(ResultSet.total_start)) >= 0)
			validMap.put(ResultSet.total_start, passString);
		else
			validMap.put(ResultSet.total_start, failString);
		
		if((int)(resultSet.resultMap.get(ResultSet.preprocessing_start)) >= 0)
			validMap.put(ResultSet.preprocessing_start, passString);
		else
			validMap.put(ResultSet.preprocessing_start, failString);
			
		if((int)(resultSet.resultMap.get(ResultSet.preprocessing_done)) >= 0 &&
				((int)(resultSet.resultMap.get(ResultSet.preprocessing_done)) >= 
				 (int)(resultSet.resultMap.get(ResultSet.preprocessing_start))
				)
			)
			validMap.put(ResultSet.preprocessing_done, passString);
		else
			validMap.put(ResultSet.preprocessing_done, failString);
			
		if((int)(resultSet.resultMap.get(ResultSet.search_start)) >= 0)
			validMap.put(ResultSet.search_start, passString);
		else
			validMap.put(ResultSet.search_start, failString);
			
		if((int)(resultSet.resultMap.get(ResultSet.search_done)) >= 0 &&
				((int)(resultSet.resultMap.get(ResultSet.search_done)) >= 
				 (int)(resultSet.resultMap.get(ResultSet.search_start))
				)
			)
			validMap.put(ResultSet.search_done, passString);
		else
			validMap.put(ResultSet.search_done, failString);
			
		if((int)(resultSet.resultMap.get(ResultSet.solution_time)) >= 0)
			validMap.put(ResultSet.solution_time, passString);
		else
			validMap.put(ResultSet.solution_time, failString);
		
		if(((String)(resultSet.resultMap.get(ResultSet.status))).toLowerCase().equals("success")){
			validMap.put(ResultSet.status, passString);
		}else{
			validMap.put(ResultSet.status, failString);
		}
		
		//if((int)(resultSet.resultMap.get(ResultSet.solution)) >= 0){
			//int[][] board = resultSet.resultMap.get(ResultSet.solution);
		boolean isValid = true;
		int sum = 0;
		for(int i = 1 ; i <= N ; i++) sum+=i;
		
		//Check whether it was correctly parsed.
		for(int i = 0 ; i < N ; i++){
			for(int j = 0 ; j < N ; j++){
				if(board[i][j] == ResultSet.parseErrorValue){
					isValid = false;
					break;
				}
			}
		}
		
		if(isValid){
			//row-wise
			for(int i = 0 ; i < N ; i++){
				boolean curIsValid = true;
				List<Integer> row = new ArrayList<Integer>(); 
				for(int j = 0 ; j < N ; j++){
					if(row.contains(board[i][j])){
						curIsValid = false;
						break;
					}
				}	
				if(!curIsValid || (sum(row) != sum)){
					isValid = false;
					break;
				}
			}
			
			//column-wise
			for(int i = 0 ; i < N ; i++){
				boolean curIsValid = true;
				List<Integer> row = new ArrayList<Integer>(); 
				for(int j = 0 ; j < N ; j++){
					if(row.contains(board[j][i])){
						curIsValid = false;
						break;
					}
				}	
				if(!curIsValid || (sum(row) != sum)){
					isValid = false;
					break;
				}
			}	
		}
			
		if(isValid){	
			validMap.put(ResultSet.solution, passString);
		}else{
			validMap.put(ResultSet.solution, failString);
		}
		if((int)(resultSet.resultMap.get(ResultSet.count_node)) >= 0)
			validMap.put(ResultSet.count_node, passString);
		else
			validMap.put(ResultSet.count_node, failString);
		
		if((int)(resultSet.resultMap.get(ResultSet.count_deadends)) >= 0)
			validMap.put(ResultSet.count_deadends, passString);
		else
			validMap.put(ResultSet.count_deadends, failString);
		
	}
	
	public int sum(List<Integer> list){
		int sum = 0;
		for(Integer integer : list)
			sum+=integer;
		return sum;
	}
	
	@Override
	public String toString() {
		Map<String, Object> resultMap = resultSet.resultMap;
		String resultString = "";

		for(String key : resultMap.keySet()){
			if(ResultSet.total_start.equals(key.toUpperCase()) ||
				ResultSet.preprocessing_start.equals(key.toUpperCase()) || 
				ResultSet.preprocessing_done.equals(key.toUpperCase()) ||
				ResultSet.search_start.equals(key.toUpperCase()) ||
				ResultSet.search_done.equals(key.toUpperCase()) ||
				ResultSet.solution_time.equals(key.toUpperCase()) ||
				ResultSet.count_node.equals(key.toUpperCase()) ||
				ResultSet.count_deadends.equals(key.toUpperCase()) ||
				ResultSet.status.equals(key.toUpperCase())){
				
				if(resultMap.get(key) instanceof Integer){
					if(((Integer)(resultMap.get(key))) == ResultSet.parseErrorValue){
						resultString += (key+"="+ ResultSet.parseErrorString  +" - "+validMap.get(key)+"\n");
					}else{
						resultString += (key+"="+resultMap.get(key)  +" - "+validMap.get(key)+"\n");
					}
				}else{
					resultString += (key+"="+resultMap.get(key)  +" - "+validMap.get(key)+"\n");
				}
				
			}else if(ResultSet.solution.equals(key.toUpperCase())){
				resultString += key+"=";
				int[][] result = (int[][]) resultMap.get(key);
				for(int i = 0 ; i < result.length ; i++){
					for(int j = 0 ; j < result[i].length ; j++){
						resultString += (result[i][j]+",");
					}
				}
				resultString = resultString.substring(0, resultString.length()-1);
				resultString += " - "+validMap.get(key)+ "\n";
			}
		}
		return resultString;
	}
	
	public void printData(){
		System.out.println(this.toString());
	}
}
