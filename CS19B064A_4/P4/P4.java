import syntaxtree.*;
import visitor.*;
@SuppressWarnings("unchecked")

public class P4 {
   public static void main(String [] args) {
      try {
         Node root = new microIRParser(System.in).Goal();
        // System.out.println("Program parsed successfully");
         Arg A=new Arg();
         root.accept(new GJDepthFirst(),A);
         for(String s:A.CFGs.keySet())
         {
         	Graph G=A.CFGs.get(s);
         	G.find_ranges();
         	
         	//System.out.println(s);
         	//for(String c:G.spill)
         	//{System.out.println("spill "+c);}
         	
         	G.linear_scan();/*
         	for(int i=0;i<G.intervals.size();i++)
         	{Pair t=G.intervals.get(i);
         		System.out.println(t.s+" "+G.start.get(t.s)+" "+G.end.get(t.s)+" "+G.reg_size(t.s));
         	}
         	for(String c:G.spill)
         	{System.out.println("spill "+c);}
         	System.out.println();*/
         	/*
         	for(String a: G.used)
         	{
         		System.out.println(a+" "+G.registor.get(a));
         	}System.out.println();
         	System.out.println();*/
         }
         A.pass++;
         root.accept(new GJDepthFirst(),A);
          // Your assignment part is invoked here.
      }
      catch (ParseException e) {
         System.out.println(e.toString());
      }
   }
} 

