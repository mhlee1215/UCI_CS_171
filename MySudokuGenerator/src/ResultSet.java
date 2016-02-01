import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultSet {
	int N;
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
	
	Map<String, Object> resultMap = null;
	public ResultSet(int N){
		this.N = N;
		
		resultMap = new HashMap<String, Object>();
		
		resultMap.put(ResultSet.total_start, -1);
		resultMap.put(ResultSet.preprocessing_start, -1);
		resultMap.put(ResultSet.preprocessing_done, -1);
		resultMap.put(ResultSet.search_start, -1);
		resultMap.put(ResultSet.search_done, -1);
		resultMap.put(ResultSet.solution_time, -1);
		resultMap.put(ResultSet.status, "INIT");
		resultMap.put(ResultSet.solution, new int[N][N]);
		resultMap.put(ResultSet.count_node, -1);
		resultMap.put(ResultSet.count_deadends, -1);
	}
	
	public void setRandData(){
		for(String key : resultMap.keySet()){
			if(ResultSet.total_start.equals(key.toUpperCase()) ||
				ResultSet.preprocessing_start.equals(key.toUpperCase()) || 
				ResultSet.preprocessing_done.equals(key.toUpperCase()) ||
				ResultSet.search_start.equals(key.toUpperCase()) ||
				ResultSet.search_done.equals(key.toUpperCase()) ||
				ResultSet.solution_time.equals(key.toUpperCase()) ||
				ResultSet.count_node.equals(key.toUpperCase()) ||
				ResultSet.count_deadends.equals(key.toUpperCase())
				){
				resultMap.put(key, (int)(Math.random()*10));
			}else if(ResultSet.status.equals(key.toUpperCase())){
				int idx = (int)(Math.random()*2)+1;
				if(idx == 1) resultMap.put(key, "success");
				else if(idx == 2) resultMap.put(key, "timeout");
				else if(idx == 3) resultMap.put(key, "error");
			}
			else if(ResultSet.solution.equals(key.toUpperCase())){

				int[][] result = (int[][]) resultMap.get(key);
				for(int i = 0 ; i < result.length ; i++){
					for(int j = 0 ; j < result[i].length ; j++){
						result[i][j]=(int) (Math.random()*N)+1;
					}
				}
				resultMap.put(key, result);
			}
		}
	}
	
	public void feedParse(String line){
		String[] parts = line.split("[^a-zA-Z0-9]");
		if(parts.length < 3) return;
		if(!parts[1].equals("=")) return;
		
		String feedKey = parts[0];
		String feedValue = parts[2];
		if(resultMap.keySet().contains(feedKey)){
			if(ResultSet.total_start.equals(feedKey.toUpperCase()) ||
				ResultSet.preprocessing_start.equals(feedKey.toUpperCase()) || 
				ResultSet.preprocessing_done.equals(feedKey.toUpperCase()) ||
				ResultSet.search_start.equals(feedKey.toUpperCase()) ||
				ResultSet.search_done.equals(feedKey.toUpperCase()) ||
				ResultSet.solution_time.equals(feedKey.toUpperCase()) ||
				ResultSet.count_node.equals(feedKey.toUpperCase()) ||
				ResultSet.count_deadends.equals(feedKey.toUpperCase())){
				resultMap.put(feedKey.toUpperCase(), Integer.parseInt(feedValue));
			}else if(ResultSet.status.equals(feedKey.toUpperCase())){
				resultMap.put(feedKey.toUpperCase(), feedValue.toUpperCase());
			}else if(ResultSet.solution.equals(feedKey.toUpperCase())){
				if(parts.length == N*N+2){
					int[][] result = (int[][]) resultMap.get(solution);
					for(int i = 2 ; i < parts.length ; i++){
						result[(i-2)/(N)][(i-2)%(N)] = Integer.parseInt(parts[i]);
					}
					
					resultMap.put(feedKey.toUpperCase(), result);
				}
			}
		}
	}
	
	@Override
	public String toString() {
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
				resultString += (key+"="+resultMap.get(key) +"\n");
			}else if(ResultSet.solution.equals(key.toUpperCase())){
				resultString += key+"=";
				int[][] result = (int[][]) resultMap.get(key);
				for(int i = 0 ; i < result.length ; i++){
					for(int j = 0 ; j < result[i].length ; j++){
						resultString += (result[i][j]+",");
					}
				}
				resultString = resultString.substring(0, resultString.length()-1);
				resultString += "\n";
			}
		}
		return resultString;
	}
	
	public void printData(){
		System.out.println(this.toString());
	}
}
