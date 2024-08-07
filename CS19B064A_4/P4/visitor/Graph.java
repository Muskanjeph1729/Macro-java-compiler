package visitor;
import java.util.*;
public class Graph{
	
	public int N;
	public int N2;
	public int curr;
	public boolean called;
	public int args;
	public String str;
	public int max_args;
	public int spilled;
	public int fin;
	public int gone;
	public HashMap<Integer, GraphNode> Nodes;
	public Set<String> used; 
	public HashMap<String, Integer> start;
	public HashMap<String, Integer> end;
	public Set<Integer> active;
	public Set<Integer> inactive;
	public Set<String> spill;
	public HashMap<String,Integer> registor;
	public Vector<Pair> intervals;
	public HashMap<Integer, Pair> curr_association;
	public HashMap<Integer, String> reg_map;
	public HashMap<String, Integer> reg_size;
	public HashMap<String, Integer> spill_map;
	public int last_spilled;
	public HashMap<String, Integer> labels;
	
	public Graph()
	{
		N=0;
		N2=0;
		fin=0;
		curr=0;
		str="";
		max_args=0;
		args=0;
		called=false;
		gone=0;
		Nodes=new HashMap<Integer, GraphNode> ();
		used=new HashSet<String> ();
		start=new HashMap<String, Integer>();
		end=new HashMap<String, Integer>();
		active=new HashSet<Integer> ();
		inactive=new HashSet<Integer> ();
		for(int i=0;i<18;i++)
		{
			inactive.add(i);
		}
		spill=new HashSet<String> ();
		registor=new HashMap<String,Integer> ();
		
		intervals=new Vector<Pair>();
		curr_association=new HashMap<Integer, Pair>();
		reg_map=new HashMap<Integer, String>();
		reg_size=new HashMap<String, Integer>();
		spill_map=new HashMap<String,Integer>();
		last_spilled=0;
		reg_map.put(0,"s0");
		reg_map.put(1,"s1");
		reg_map.put(2,"s2");
		reg_map.put(3,"s3");
		reg_map.put(4,"s4");
		reg_map.put(5,"s5");
		reg_map.put(6,"s6");
		reg_map.put(7,"s7");
		reg_map.put(8,"t0");
		reg_map.put(9,"t1");
		reg_map.put(10,"t2");
		reg_map.put(11,"t3");
		reg_map.put(12,"t4");
		reg_map.put(13,"t5");
		reg_map.put(14,"t6");
		reg_map.put(15,"t7");
		reg_map.put(16,"t8");
		reg_map.put(17,"t9");
		labels=new HashMap<String, Integer>();
		
	}
	public void find_ranges()
	{
		for(int i=0;i<=N;i++)
		{	
			for(String s:Nodes.get(i).use){
				if(!spill.contains(s))
				used.add(s);
			}
		}
		
		
		for(int i=0;i<=N;i++)
		{
			GraphNode GN=Nodes.get(i);
			for(String s:GN.def)
			{
				if(used.contains(s))
				{
					if(!start.containsKey(s))
					{
						start.put(s,i);
					}
				}
			}
			
			for(String s: GN.use)
			{	
				if(used.contains(s)){
				if(!start.containsKey(s))
					{start.put(s,i);}
				
					end.put(s,i);
				}
			}
		}
		
		
	}
	
	public void linear_scan()
	{
		for(String a:used)
		{
			Pair2 temp=new Pair2();
			temp.x=start.get(a);
			temp.y=end.get(a);
			
			//if(a.compareTo("TEMP 5")==0){System.out.println(a);}
			
			Pair pp=new Pair();
			pp.s=a;
			pp.p=temp;
			
			intervals.add(pp);
		}	
		Comparator<Pair> compare=new ComparePairs();
		Collections.sort(intervals,compare);
		
		for(int i=0;i<intervals.size();i++)
		{
			expire_old(i);
			//if(intervals.get(i).s.compareTo("TEMP 26")==0)
			//{
				//System.out.println(active.size());		
			//}
			if(active.size()==18)
			{
				//for(Integer j:active)
				//{
				//	System.out.print(curr_association.get(j).s+" ");
				//}System.out.println();
				spill(i);
			}
			else
			{
				Set<Integer> temp2=new HashSet<Integer> ();
		 		temp2.addAll(inactive);
				for(Integer j:temp2)
				{
					active.add(j);
					inactive.remove(j);//if(intervals.get(i).s.compareTo("TEMP 26")==0){System.out.println("as "+j);}
					registor.put(intervals.get(i).s,j);
					reg_size.put(intervals.get(i).s,active.size());
					curr_association.put(j,intervals.get(i));
					break;
				}
			}
		}
		spilled=spill.size();
	}
	
	public void expire_old(int i)
	{
		Set<Integer> temp2=new HashSet<Integer> ();
		temp2.addAll(active);
		for(Integer  j: temp2)
		{
			Pair temp=curr_association.get(j);
			if(temp.p.y<=intervals.get(i).p.x)
			{//System.out.println("ahe ahe");
				active.remove(j);
				inactive.add(j);
			}
		}	
	
	}
	
	public void spill(int i)
	{
		int last=-1;int k=-1;
		Set<Integer> temp2=new HashSet<Integer> ();
		temp2.addAll(active);
		for(Integer j: temp2)
		{
			Pair temp=curr_association.get(j);
			if(temp.p.y>last)
			{
				last=temp.p.y;
				k=j;
			}
		}
		Pair at=intervals.get(i);
		if(at.p.y> last)
		{
			spill.add(at.s);
		}
		else
		{
			registor.remove(curr_association.get(k).s);
			spill.add(curr_association.get(k).s);
			curr_association.put(k,at);
			registor.put(at.s,k);
			
		}
	}
	
}

class ComparePairs implements Comparator<Pair>
{
	public int compare(Pair o1, Pair o2)
	{
		if(o1.p.x!=o2.p.x)
		return o1.p.x-o2.p.x;
		else
		return o1.p.y-o2.p.y;
	}


}



