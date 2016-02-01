public class TestCase{
	public int M,N,P,Q;
	public String type;
	
	public static final String opt_none = "NONE";
	public static final String opt_gen = "GEN";
	public static final String opt_fc = "FC";
	public static final String opt_acp = "ACP";
	public static final String opt_mac = "MAC";
	public static final String opt_mrv = "MRV";
	public static final String opt_dh = "DH";
	public static final String opt_lcv = "LCV";
	
	
	public TestCase(int M, int N, int P, int Q){
		this.M = M;
		this.N = N;
		this.P = P;
		this.Q = Q;
		this.type = TestCase.opt_none;
	}
	
	public TestCase(int M, int N, int P, int Q, String type){
		this(M, N, P, Q);
		this.type = type;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return M+" "+N+" "+P+" "+Q;
	}
} 