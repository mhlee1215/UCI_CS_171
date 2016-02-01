import java.util.HashMap;
import java.util.Map;

public class ResultSet {
	int N, P, Q;
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
	
	public static final String initString = "INIT";
	public static final int initValue = -1;
	public static final int errorValue = -999;
	public static final String unrecognizeString = "UNRECOGNIZED";
	
	public static final String STATE_SUCCESS = "success";
	public static final String STATE_TIMEOUT = "timeout";
	public static final String STATE_ERROR = "error";
	
	public static final int parseErrorValue = -998;
	public static final String parseErrorString = "NUMBER_PARSE_ERROR";
	
	Map<String, Object> resultMap = null;
	public ResultSet(TestCase testCase){
		this.N = testCase.N;
		this.P = testCase.P;
		this.Q = testCase.Q;
		
		resultMap = new HashMap<String, Object>();
		
		resultMap.put(ResultSet.total_start, initValue);
		resultMap.put(ResultSet.preprocessing_start, initValue);
		resultMap.put(ResultSet.preprocessing_done, initValue);
		resultMap.put(ResultSet.search_start, initValue);
		resultMap.put(ResultSet.search_done, initValue);
		resultMap.put(ResultSet.solution_time, initValue);
		resultMap.put(ResultSet.status, initString);
		resultMap.put(ResultSet.solution, new int[N][N]);
		resultMap.put(ResultSet.count_node, -1);
		resultMap.put(ResultSet.count_deadends, initValue);
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
				if(idx == 1) resultMap.put(key, STATE_SUCCESS);
				else if(idx == 2) resultMap.put(key, STATE_TIMEOUT);
				else if(idx == 3) resultMap.put(key, STATE_ERROR);
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
		String[] parts = line.split("=");
		//System.out.println(parts.length);
		if(parts.length < 2) return;
		//if(!parts[1].equals("=")) return;
		
		String feedKey = parts[0].trim();
		String feedValue = parts[1].trim();
		//System.out.println("key :"+feedKey+", value:"+feedValue);
		if(resultMap.keySet().contains(feedKey)){
			if(ResultSet.total_start.equals(feedKey.toUpperCase()) ||
				ResultSet.preprocessing_start.equals(feedKey.toUpperCase()) || 
				ResultSet.preprocessing_done.equals(feedKey.toUpperCase()) ||
				ResultSet.search_start.equals(feedKey.toUpperCase()) ||
				ResultSet.search_done.equals(feedKey.toUpperCase()) ||
				ResultSet.solution_time.equals(feedKey.toUpperCase()) ||
				ResultSet.count_node.equals(feedKey.toUpperCase()) ||
				ResultSet.count_deadends.equals(feedKey.toUpperCase())){
				try{
					resultMap.put(feedKey.toUpperCase(), Integer.parseInt(feedValue));
				}catch(Exception e){
					resultMap.put(feedKey.toUpperCase(), ResultSet.parseErrorValue);
				}
			}else if(ResultSet.status.equals(feedKey.toUpperCase())){
				if(feedValue.toUpperCase().equals(ResultSet.STATE_SUCCESS) ||
						feedValue.toUpperCase().equals(ResultSet.STATE_TIMEOUT) ||
						feedValue.toUpperCase().equals(ResultSet.STATE_ERROR) ){
					resultMap.put(feedKey.toUpperCase(), feedValue.toUpperCase());
				}
				else{
					resultMap.put(feedKey.toUpperCase(), ResultSet.unrecognizeString);
				}
					
					
			}else if(ResultSet.solution.equals(feedKey.toUpperCase())){
				String[] solParts = feedValue.split(",");
				//System.out.println("solParts : "+solParts.length+", N:"+N);
				if(solParts.length == N*N){
					int[][] result = (int[][]) resultMap.get(solution);
					for(int i = 0 ; i < solParts.length ; i++){
						try{
							result[(i)/(N)][(i)%(N)] = Integer.parseInt(solParts[i].trim());
						}catch(Exception e){
							result[(i)/(N)][(i)%(N)] = parseErrorValue;
						}
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
				if(resultMap.get(key) instanceof Integer){
					if(((Integer)(resultMap.get(key))) == ResultSet.parseErrorValue){
						resultString += (key+"="+ parseErrorString +"\n");
					}else{
						resultString += (key+"="+resultMap.get(key) +"\n");
					}
				}else{
					resultString += (key+"="+resultMap.get(key) +"\n");
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
				resultString += "\n";
			}
		}
		return resultString;
	}
	
	public void printData(){
		System.out.println(this.toString());
	}

	public int getN() {
		return N;
	}

	public int getP() {
		return P;
	}

	public int getQ() {
		return Q;
	}
	
	
}
