package visitor;
import java.util.*;
public class GraphNode{
	
	public int stmt_no;
	public Set<String> def;
	public Set<String> use;
	public String exp;
	public String sim_exp;
	public GraphNode()
	{
		stmt_no=0;
		def=new HashSet<String> ();
		use=new HashSet<String> ();		
		
	}

}
