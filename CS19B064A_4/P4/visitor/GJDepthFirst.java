//
// Generated by JTB 1.3.2
//

package visitor;
import syntaxtree.*;
import java.util.*;

/**
 * Provides default methods which visit each node in the tree in depth-first
 * order.  Your visitors may extend this class.
 */
public class GJDepthFirst<R,A> implements GJVisitor<R,A> {
   //
   // Auto class visitors--probably don't need to be overridden.
   //
   public Graph G;
   public GraphNode GN;
   public boolean in_exp;
   public boolean in_def;
   public int count;
   public boolean in_call;
   public boolean in_sim_exp;
   public boolean label;
   public String str;
   public String str1;
   
   public R visit(NodeList n, A argu) {
      R _ret=null;
      int _count=0;
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
         e.nextElement().accept(this,argu);
         _count++;
      }
      return _ret;
   }

   public R visit(NodeListOptional n, A argu) {
      if ( n.present() ) {
         R _ret=null;
         int _count=0;
         for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
            e.nextElement().accept(this,argu);
            _count++;
         }
         return _ret;
      }
      else
         return null;
   }

   public R visit(NodeOptional n, A argu) {
      if ( n.present() )
         return n.node.accept(this,argu);
      else
         return null;
   }

   public R visit(NodeSequence n, A argu) {
      R _ret=null;
      int _count=0;
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
         e.nextElement().accept(this,argu);
         _count++;
      }
      return _ret;
   }

   public R visit(NodeToken n, A argu) { return (R) n.tokenImage; }

   //
   // User-generated visitor methods below
   //

   /**
    * f0 -> "MAIN"
    * f1 -> StmtList()
    * f2 -> "END"
    * f3 -> ( Procedure() )*
    * f4 -> <EOF>
    */
   public R visit(Goal n, A argu) {
      R _ret=null;
      
      Arg T=(Arg) argu;
      
      if(T.pass==0)
      {
      G=new Graph();
      T.CFGs.put("MAIN",G);
      in_exp=false;
      in_def=false;
      in_call=false;
      count=0;
      GN=new GraphNode();
      G.Nodes.put(0,GN);
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      return _ret;
      }
      else
      {
      G=T.CFGs.get("MAIN");
      int x=G.spilled;
      if(G.called)
      x=x+10;
      int y=G.max_args;
      in_call=false;
      label=false;
      count=0;
      in_sim_exp=false;
      str="";
      str1="";
      
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      
      G.str=str;
     
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      G=T.CFGs.get("MAIN");
      System.out.println("MAIN [0] ["+Math.max(G.last_spilled,G.fin)+"] ["+(G.max_args)+"]");
      System.out.print(G.str);
      System.out.println("END");
      System.out.println();
      System.out.println(str1);
      return _ret;
      }
      
      
   }
   

   /**
    * f0 -> ( ( Label() )? Stmt() )*
    */
   public R visit(StmtList n, A argu) {
      R _ret=null;
      Arg T=(Arg) argu;
      
      if(T.pass==0)
      {
      n.f0.accept(this, argu);
      return _ret;
      }
      else
      {label=true;
      n.f0.accept(this, argu);
      return _ret;
      }
   }

   /**
    * f0 -> Label()
    * f1 -> "["
    * f2 -> IntegerLiteral()
    * f3 -> "]"
    * f4 -> StmtExp()
    */
   public R visit(Procedure n, A argu) {
      R _ret=null;
      Arg T=(Arg) argu;
      
      if(T.pass==0)
      {
      String a=(String) n.f0.accept(this, argu);
      G=new Graph();
      T.CFGs.put(a,G);
      n.f1.accept(this, argu);
      String b=(String) n.f2.accept(this, argu);
      GN=new GraphNode();
      G.Nodes.put(0,GN);
      G.args=Integer.parseInt(b);
          
      for(int i=0;i<Integer.parseInt(b) && i<4;i++)
      {
      	  GN.def.add("TEMP "+String.valueOf(i));    
      }
      for(int i=4;i<Integer.parseInt(b);i++)
      {
      	  G.spill.add("TEMP "+String.valueOf(i));
      	  G.spill_map.put("TEMP "+String.valueOf(i),i-4 );
      	  G.last_spilled++;
      }
      
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      
      
      return _ret;
      }
      else
      {
      label=false;
      str="";
      String a=(String)n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      String b=(String) n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      G=T.CFGs.get(a);
      
      int x=8+G.spilled;
      if(G.called)
      x=x+10;
      //System.out.println(G.spilled+" "+G.args);
      
      int y=0;
      if(G.args>4)y=G.args-4;
      str=str+"ASTORE SPILLEDARG "+y+" s0"+'\n';
      str=str+"ASTORE SPILLEDARG "+(y+1)+" s1"+'\n';
      str=str+"ASTORE SPILLEDARG "+(y+2)+" s2"+'\n';
      str=str+"ASTORE SPILLEDARG "+(y+3)+" s3"+'\n';
      str=str+"ASTORE SPILLEDARG "+(y+4)+" s4"+'\n';
      str=str+"ASTORE SPILLEDARG "+(y+5)+" s5"+'\n';
      str=str+"ASTORE SPILLEDARG "+(y+6)+" s6"+'\n';
      str=str+"ASTORE SPILLEDARG "+(y+7)+" s7"+'\n';
      G.last_spilled=G.last_spilled+8;
      for(int i=0;i<G.args && i<4;i++)
      {   
      	  if(G.registor.containsKey(("TEMP "+Integer.toString(i))))
      	  {
      	  	str=str+"MOVE "+G.reg_map.get(G.registor.get("TEMP "+i))+" a"+i+'\n';	
      	  }
      	 // if(G.spill.contains("TEMP 0"))System.out.println("sdfsdfsdfesdfs");
      	  //if (G.registor.containsKey(("TEMP "+Integer.toString(i)))){System.out.println("ukjkjkjk");}
      	  if(G.spill.contains(("TEMP "+Integer.toString(i))))
      	  {
      	  	if(G.spill_map.containsKey(("TEMP "+Integer.toString(i))))
      	  	{
      	  		str=str+"MOVE v0 a"+i+'\n';
      	  		str=str+"ASTORE SPILLEDARG "+G.spill_map.get("TEMP "+i)+" v0"+'\n';
      	  	}
      	  	else
      	  	{
      	  		G.spill_map.put("TEMP "+i,G.last_spilled);G.last_spilled++;
      	  		str=str+"MOVE v0 a"+i+'\n';
      	  		str=str+"ASTORE SPILLEDARG "+G.spill_map.get("TEMP "+i)+" v0"+'\n';
      	  	}
      	  }
      }
      
      n.f4.accept(this, argu);
      str1=str1+a+" ["+b+"] ["+Math.max(G.last_spilled,G.fin)+"] ["+G.max_args+"]"+'\n';
      str1=str1+str+'\n';
      
      return _ret;
      }
   }

   /**
    * f0 -> NoOpStmt()
    *       | ErrorStmt()
    *       | CJumpStmt()
    *       | JumpStmt()
    *       | HStoreStmt()
    *       | HLoadStmt()
    *       | MoveStmt()
    *       | PrintStmt()
    */
   public R visit(Stmt n, A argu) {
      R _ret=null;
      Arg T=(Arg) argu;
      if(T.pass==0)
      {
      	G.N++;
      	GN=new GraphNode();
      	GN.stmt_no=G.N;
      	G.Nodes.put(G.N,GN);
      	n.f0.accept(this, argu);
      	return _ret;
      }
      else
      {label=false;
      G.N2++;
      GN=G.Nodes.get(G.N2);
      	n.f0.accept(this, argu);
      	label=true;
      	return _ret;
      }
   }

   /**
    * f0 -> "NOOP"
    */
   public R visit(NoOpStmt n, A argu) {
      R _ret=null;
      Arg T=(Arg) argu;
      if(T.pass==0){
      n.f0.accept(this, argu);
      return _ret;}
      else
      {
      str=str+"NOOP"+'\n';
      n.f0.accept(this, argu);
      return _ret;
      }
   }

   /**
    * f0 -> "ERROR"
    */
   public R visit(ErrorStmt n, A argu) {
      R _ret=null;
      Arg T=(Arg) argu;
      if(T.pass==0){
      n.f0.accept(this, argu);
      return _ret;}
      else
      {
      str=str+"ERROR"+'\n';
      n.f0.accept(this, argu);
      return _ret;
      }
   }

   /**
    * f0 -> "CJUMP"
    * f1 -> Temp()
    * f2 -> Label()
    */
   public R visit(CJumpStmt n, A argu) {
      R _ret=null;
      Arg T=(Arg) argu;
      
      if(T.pass==0)
      {
      
      	n.f0.accept(this, argu);
      	in_exp=true;
      	n.f1.accept(this, argu);
      	in_exp=false;
      	String a=(String) n.f2.accept(this, argu);
        
        if(G.labels.containsKey(a))
        {
        	int x=G.labels.get(a);
        	int y=G.N;
        	Set<String> defined=new HashSet<String>();
        	//System.out.println(a+" "+x+" "+y);
        	for(int i=x;i<y;i++)
        	{
        		for(String s:G.Nodes.get(i).use)
        		{
        			if(!defined.contains(s))
        			{
        				GN.use.add(s);
        			}
        			//if(defined.contains(s))System.out.println(s);
        		}
        		defined.addAll(G.Nodes.get(i).def);
        		//System.out.println(i+" "+y);
        		//for(String s:G.Nodes.get(i).def)System.out.print(s+" ");System.out.println();
        	//GN.use.addAll(G.Nodes.get(i).use);
        	}
        	
        }
      	
      	return _ret;
      }
      else
      {
      	n.f0.accept(this, argu);
      	String a=(String)n.f1.accept(this, argu);
      	String b=(String)n.f2.accept(this, argu);
      	
      	  if(G.registor.containsKey(a))
      	  {
      	  	str=str+"CJUMP "+G.reg_map.get(G.registor.get(a))+" "+b+'\n';	
      	  }
      	  if(G.spill.contains(a))
      	  {
      	  	if(G.spill_map.containsKey(a))
      	  	{
      	  		
      	  		str=str+"ALOAD v0 SPILLEDARG "+G.spill_map.get(a)+'\n';
      	  		str=str+"CJUMP v0 "+b+'\n';
      	  	}
      	  	else
      	  	{
      	  		G.spill_map.put(a,G.last_spilled);G.last_spilled++;
      	  		str=str+"ALOAD v0 SPILLEDARG "+G.spill_map.get(a)+'\n';
      	  		str=str+"CJUMP v0 "+b+'\n';
      	  	}
      	  }
      	
      	
      	
      	return _ret;
      }
   }

   /**
    * f0 -> "JUMP"
    * f1 -> Label()
    */
   public R visit(JumpStmt n, A argu) {
      R _ret=null;
      Arg T=(Arg) argu;
      
      if(T.pass==0)
      {
      	n.f0.accept(this, argu);
      	String a=(String)n.f1.accept(this, argu);
      	if(G.labels.containsKey(a))
        {
        	int x=G.labels.get(a);
        	int y=G.N;
        	Set<String> defined=new HashSet<String>();
        	//System.out.println(a+" "+x+" "+y);
        	for(int i=x;i<y;i++)
        	{
        		for(String s:G.Nodes.get(i).use)
        		{
        			if(!defined.contains(s))
        			{
        				GN.use.add(s);
        			}
        			//if(defined.contains(s))System.out.println(s);
        		}
        		defined.addAll(G.Nodes.get(i).def);
        		//System.out.println(i+" "+y);
        		//for(String s:G.Nodes.get(i).def)System.out.print(s+" ");System.out.println();
        	//GN.use.addAll(G.Nodes.get(i).use);
        	}
        	
        }	
      }
      else
      {
      	n.f0.accept(this, argu);
      	String b=(String)n.f1.accept(this, argu);
      	str=str+"JUMP "+b+'\n';
      	
      	
      }
      return _ret;
   }

   /**
    * f0 -> "HSTORE"
    * f1 -> Temp()
    * f2 -> IntegerLiteral()
    * f3 -> Temp()
    */
   public R visit(HStoreStmt n, A argu) {
      R _ret=null;
      Arg T=(Arg) argu;
      if(T.pass==0)
      {
      n.f0.accept(this, argu);
      in_exp=true;
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      in_exp=false;
      return _ret;
      
      }
      else{
      n.f0.accept(this, argu);
      String a=(String)n.f1.accept(this, argu);
      String b=(String)n.f2.accept(this, argu);
      String c=(String)n.f3.accept(this, argu);
      
      String a1="";
      if(G.registor.containsKey(a))
      	  {
      	  	a1=G.reg_map.get(G.registor.get(a));	
      	  }
      	  if(G.spill.contains(a))
      	  {
      	  	if(G.spill_map.containsKey(a))
      	  	{
      	  		
      	  		str=str+"ALOAD v0 SPILLEDARG "+G.spill_map.get(a)+'\n';
      	  		a1="v0";
      	  	}
      	  	else
      	  	{
      	  		G.spill_map.put(a,G.last_spilled);G.last_spilled++;
      	  		str=str+"ALOAD v0 SPILLEDARG "+G.spill_map.get(a)+'\n';
      	  		a1="v0";
      	  	}
      	  }
      String c1="";
      if(G.registor.containsKey(c))
      	  {
      	  	c1=G.reg_map.get(G.registor.get(c));	
      	  }
      	  if(G.spill.contains(c))
      	  {
      	  	if(G.spill_map.containsKey(c))
      	  	{
      	  		
      	  		str=str+"ALOAD v1 SPILLEDARG "+G.spill_map.get(c)+'\n';
      	  		c1="v1";
      	  	}
      	  	else
      	  	{
      	  		G.spill_map.put(c,G.last_spilled);G.last_spilled++;
      	  		str=str+"ALOAD v1 SPILLEDARG "+G.spill_map.get(c)+'\n';
      	  		c1="v1";
      	  	}
      	  }
      	  if(a1.compareTo("")!=0 && c1.compareTo("")!=0)
      	  str=str+"HSTORE "+a1+" "+b+" "+c1+'\n';
      return _ret;
      }
   }

   /**
    * f0 -> "HLOAD"
    * f1 -> Temp()
    * f2 -> Temp()
    * f3 -> IntegerLiteral()
    */
   public R visit(HLoadStmt n, A argu) {
      R _ret=null;
      Arg T=(Arg) argu;
      
      if(T.pass==0){
      n.f0.accept(this, argu);
      in_def=true;
      n.f1.accept(this, argu);
      in_def=false;
      in_exp=true;
      n.f2.accept(this, argu);
      in_exp=false;
      n.f3.accept(this, argu);
      return _ret;
      }
      else
      {
      n.f0.accept(this, argu);
      String a=(String)n.f1.accept(this, argu);
      String c=(String)n.f2.accept(this, argu);
      String b=(String)n.f3.accept(this, argu);
      
      String a1="";
      if(G.registor.containsKey(a))
      	  {
      	  	a1=G.reg_map.get(G.registor.get(a));	
      	  }
      	  if(G.spill.contains(a))
      	  {
      	  	if(G.spill_map.containsKey(a))
      	  	{
      	  		
      	  		a1="v0";
      	  	}
      	  	else
      	  	{
      	  		G.spill_map.put(a,G.last_spilled);G.last_spilled++;
      	  		a1="v0";
      	  	}
      	  }
      String c1="";
      if(G.registor.containsKey(c))
      	  {
      	  	c1=G.reg_map.get(G.registor.get(c));	
      	  }
      	  if(G.spill.contains(c))
      	  {
      	  	if(G.spill_map.containsKey(c))
      	  	{
      	  		
      	  		str=str+"ALOAD v1 SPILLEDARG "+G.spill_map.get(c)+'\n';
      	  		c1="v1";
      	  	}
      	  	else
      	  	{
      	  		G.spill_map.put(c,G.last_spilled);G.last_spilled++;
      	  		str=str+"ALOAD v1 SPILLEDARG "+G.spill_map.get(c)+'\n';
      	  		c1="v1";
      	  	}
      	  }
      	  if(a1.compareTo("")!=0 && c1.compareTo("")!=0)
      	  {
      	  	str=str+"HLOAD "+a1+" "+c1+" "+b+'\n';
      	  }
      	  if(a1.compareTo("v0")==0)
      	  {
      	  	str=str+"ASTORE SPILLEDARG "+G.spill_map.get(a)+" v0"+'\n';
      	  }
      return _ret;
      }
   }

   /**
    * f0 -> "MOVE"
    * f1 -> Temp()
    * f2 -> Exp()
    */
   public R visit(MoveStmt n, A argu) {
      R _ret=null;
      Arg T=(Arg) argu;
      
      if(T.pass==0){
      n.f0.accept(this, argu);
      in_def=true;
      n.f1.accept(this, argu);
      in_def=false;
      n.f2.accept(this, argu);
      
      return _ret;
      }
      else
      {
      n.f0.accept(this, argu);
      String a=(String)n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      String a1="";
      if(G.registor.containsKey(a))
      	  {
      	  	a1=G.reg_map.get(G.registor.get(a));	
      	  }
      	  if(G.spill.contains(a))
      	  {
      	  	if(G.spill_map.containsKey(a))
      	  	{
      	  		
      	  		a1="v0";
      	  	}
      	  	else
      	  	{
      	  		G.spill_map.put(a,G.last_spilled);G.last_spilled++;
      	  		a1="v0";
      	  	}
      	  }
      if(a1.compareTo("")!=0)
      {
      	 str=str+"MOVE "+a1+" "+GN.exp+'\n';
      }
      if(a1.compareTo("v0")==0)
      	  {
      	  	str=str+"ASTORE SPILLEDARG "+G.spill_map.get(a)+" v0"+'\n';
      	  }
      
      return _ret;
      }
   }

   /**
    * f0 -> "PRINT"
    * f1 -> SimpleExp()
    */
   public R visit(PrintStmt n, A argu) {
      R _ret=null;
      Arg T=(Arg) argu;
      
      if(T.pass==0){
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      return _ret;
      }
      else
      {
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      str=str+"PRINT "+GN.sim_exp+'\n';
      
      return _ret;
      
      }
   }

   /**
    * f0 -> Call()
    *       | HAllocate()
    *       | BinOp()
    *       | SimpleExp()
    */
   public R visit(Exp n, A argu) {
      R _ret=null;
      Arg T=(Arg) argu;
      if(T.pass==0){
      n.f0.accept(this, argu);
      return _ret;
      }
      else
      {GN.exp="";
      n.f0.accept(this, argu);
      return _ret;
      
      }
   }

   /**
    * f0 -> "BEGIN"
    * f1 -> StmtList()
    * f2 -> "RETURN"
    * f3 -> SimpleExp()
    * f4 -> "END"
    */
   public R visit(StmtExp n, A argu) {
      R _ret=null;
      Arg T=(Arg) argu;
      
      if(T.pass==0){
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        G.N++;
      	GN=new GraphNode();
      	GN.stmt_no=G.N;
      	G.Nodes.put(G.N,GN);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        return _ret;
      }
      else
      {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        
        str=str+"MOVE v0 "+GN.sim_exp+'\n';
        int y=0;
      if(G.args>4)y=G.args-4;
      str=str+"ALOAD s0 SPILLEDARG "+y+'\n';
      str=str+"ALOAD s1 SPILLEDARG "+(y+1)+'\n';
      str=str+"ALOAD s2 SPILLEDARG "+(y+2)+'\n';
      str=str+"ALOAD s3 SPILLEDARG "+(y+3)+'\n';
      str=str+"ALOAD s4 SPILLEDARG "+(y+4)+'\n';
      str=str+"ALOAD s5 SPILLEDARG "+(y+5)+'\n';
      str=str+"ALOAD s6 SPILLEDARG "+(y+6)+'\n';
      str=str+"ALOAD s7 SPILLEDARG "+(y+7)+'\n';
      str=str+"END"+'\n';
      
        return _ret;
      }
   }

   /**
    * f0 -> "CALL"
    * f1 -> SimpleExp()
    * f2 -> "("
    * f3 -> ( Temp() )*
    * f4 -> ")"
    */
   public R visit(Call n, A argu) {
      R _ret=null;
      Arg T=(Arg) argu;
      
      if(T.pass==0)
      {G.called=true;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      in_exp=true;
      in_call=true;
      n.f3.accept(this, argu);
      in_call=false;
      in_exp=false;
      if(G.max_args<count)
      {G.max_args=count;}
      count=0;
      n.f4.accept(this, argu);
      return _ret;
      }
      else
      {
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      String b=GN.sim_exp;
      n.f2.accept(this, argu);
      
      
      
      str=str+"ASTORE SPILLEDARG "+G.last_spilled+" t0"+'\n';
      str=str+"ASTORE SPILLEDARG "+(G.last_spilled+1)+" t1"+'\n';
      str=str+"ASTORE SPILLEDARG "+(G.last_spilled+2)+" t2"+'\n';
      str=str+"ASTORE SPILLEDARG "+(G.last_spilled+3)+" t3"+'\n';
      str=str+"ASTORE SPILLEDARG "+(G.last_spilled+4)+" t4"+'\n';
      str=str+"ASTORE SPILLEDARG "+(G.last_spilled+5)+" t5"+'\n';
      str=str+"ASTORE SPILLEDARG "+(G.last_spilled+6)+" t6"+'\n';
      str=str+"ASTORE SPILLEDARG "+(G.last_spilled+7)+" t7"+'\n';
      str=str+"ASTORE SPILLEDARG "+(G.last_spilled+8)+" t8"+'\n';
      str=str+"ASTORE SPILLEDARG "+(G.last_spilled+9)+" t9"+'\n';
      G.fin=Math.max(G.last_spilled+10,G.fin);
      in_call=true;
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      in_call=false;
      count=0;
      str=str+"CALL "+b+'\n';
      str=str+"ALOAD t0 SPILLEDARG "+G.last_spilled+'\n';
      str=str+"ALOAD t1 SPILLEDARG "+(G.last_spilled+1)+'\n';
      str=str+"ALOAD t2 SPILLEDARG "+(G.last_spilled+2)+'\n';
      str=str+"ALOAD t3 SPILLEDARG "+(G.last_spilled+3)+'\n';
      str=str+"ALOAD t4 SPILLEDARG "+(G.last_spilled+4)+'\n';
      str=str+"ALOAD t5 SPILLEDARG "+(G.last_spilled+5)+'\n';
      str=str+"ALOAD t6 SPILLEDARG "+(G.last_spilled+6)+'\n';
      str=str+"ALOAD t7 SPILLEDARG "+(G.last_spilled+7)+'\n';
      str=str+"ALOAD t8 SPILLEDARG "+(G.last_spilled+8)+'\n';
      str=str+"ALOAD t9 SPILLEDARG "+(G.last_spilled+9)+'\n';
      
      GN.exp="v0";
      return _ret;
      }
   }

   /**
    * f0 -> "HALLOCATE"
    * f1 -> SimpleExp()
    */
   public R visit(HAllocate n, A argu) {
      R _ret=null;
      Arg T=(Arg) argu;
      
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      GN.exp="HALLOCATE "+GN.sim_exp;
      
      return _ret;
   }

   /**
    * f0 -> Operator()
    * f1 -> Temp()
    * f2 -> SimpleExp()
    */
   public R visit(BinOp n, A argu) {
      R _ret=null;
      Arg T=(Arg) argu;
      
      if(T.pass==0){
      n.f0.accept(this, argu);
      in_exp=true;
      n.f1.accept(this, argu);
      in_exp=false;
      n.f2.accept(this, argu);
      return _ret;
      }
      else
      {
      String op=(String)n.f0.accept(this, argu);
      String a=(String) n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      String a1="";
      if(G.registor.containsKey(a))
      	  {
      	  	a1=G.reg_map.get(G.registor.get(a));	
      	  }
      	  if(G.spill.contains(a))
      	  {
      	  	if(G.spill_map.containsKey(a))
      	  	{
      	  		str=str+"ALOAD v0 SPILLEDARG "+G.spill_map.get(a)+'\n';
      	  		a1="v0";
      	  	}
      	  	else
      	  	{
      	  		G.spill_map.put(a,G.last_spilled);G.last_spilled++;
      	  		str=str+"ALOAD v0 SPILLEDARG "+G.spill_map.get(a)+'\n';
      	  		a1="v0";
      	  	}
      	  }
       if(a1.compareTo("")!=0)
       {
       	GN.exp=op+" "+a1+" "+GN.sim_exp;
       }
       else{GN.exp="";}
      
      return _ret;
      }
   }

   /**
    * f0 -> "LE"
    *       | "NE"
    *       | "PLUS"
    *       | "MINUS"
    *       | "TIMES"
    *       | "DIV"
    */
   public R visit(Operator n, A argu) {
      R _ret=null;
      Arg T=(Arg) argu;
      if(T.pass==0){
      n.f0.accept(this, argu);
      return _ret;
      }
      else{
      String a=(String)n.f0.accept(this, argu);
      return (R) a;
      }
      
   }

   /**
    * f0 -> Temp()
    *       | IntegerLiteral()
    *       | Label()
    */
   public R visit(SimpleExp n, A argu) {
      R _ret=null;
      Arg T=(Arg) argu;
      
      if(T.pass==0){
      in_exp=true;
      n.f0.accept(this, argu);
      in_exp=false;
      return _ret;
      }
      else
      {GN.sim_exp="";in_sim_exp=true;
      n.f0.accept(this, argu);in_sim_exp=false;
      GN.exp=GN.sim_exp;
      return _ret;
      }
   }

   /**
    * f0 -> "TEMP"
    * f1 -> IntegerLiteral()
    */
   public R visit(Temp n, A argu) {
      R _ret=null;
      Arg T=(Arg) argu;
      if(T.pass==0){
      String a =(String) n.f0.accept(this, argu);
      String b= (String) n.f1.accept(this, argu);
      if(in_exp==true)
      GN.use.add(a+" "+b);
      if(in_def==true)
      GN.def.add(a+" "+b);
      if(in_call==true)
      count++;
      
      return (R) (a+" "+b);
      }
      else
      {
      String a=(String)n.f0.accept(this, argu);
      String b=(String)n.f1.accept(this, argu);
      if(in_sim_exp==true){
      String a1="";
      if(G.registor.containsKey(a+" "+b))
      	  {
      	  	a1=G.reg_map.get(G.registor.get(a+" "+b));	
      	  }
      	  if(G.spill.contains(a+" "+b))
      	  {
      	  	if(G.spill_map.containsKey(a+" "+b))
      	  	{
      	  		str=str+"ALOAD v1 SPILLEDARG "+G.spill_map.get(a+" "+b)+'\n';
      	  		a1="v1";
      	  	}
      	  	else
      	  	{
      	  		G.spill_map.put(a+" "+b,G.last_spilled);G.last_spilled++;
      	  		str=str+"ALOAD v1 SPILLEDARG "+G.spill_map.get(a+" "+b)+'\n';
      	  		a1="v1";
      	  	}
      	  }
       if(a1.compareTo("")!=0)
       {
       	GN.sim_exp=a1;
       }
       else{GN.sim_exp="";}
      }
       if(in_call==true)
       {
       	String a1="";
      		if(G.registor.containsKey(a+" "+b))
      		  {
      	  		a1=G.reg_map.get(G.registor.get(a+" "+b));	
      	  	}
      	  	if(G.spill.contains(a+" "+b))
      	  	{
      	  		if(G.spill_map.containsKey(a+" "+b))
      	  		{
      	  			str=str+"ALOAD v0 SPILLEDARG "+G.spill_map.get(a+" "+b)+'\n';
      	  			a1="v0";
      	  		}
      	  		else
      	  		{
      	  			G.spill_map.put(a+" "+b,G.last_spilled);G.last_spilled++;
      	  			str=str+"ALOAD v0 SPILLEDARG "+G.spill_map.get(a+" "+b)+'\n';
      	  			a1="v0";
      	  		}
      	 	 }
      	 	 if(count<4)
      	 	 {
      	 	 	str=str+"MOVE a"+count+" "+a1+'\n';
      	 	 }
      	 	 else
      	 	 {
      	 	 	str=str+"PASSARG "+(count-3)+" "+a1+'\n';
      	 	 }
      	 	 count++;
       }
      
      return (R) (a+" "+b);
      
      }
   }

   /**
    * f0 -> <INTEGER_LITERAL>
    */
   public R visit(IntegerLiteral n, A argu) {
      R _ret=null;
      Arg T=(Arg) argu;
      if(T.pass==0)
      {
      String a =(String)n.f0.accept(this, argu);
      return (R) a;
      }
      else
      {
      String a=(String)n.f0.accept(this, argu);
      GN.sim_exp=a;
      return (R) a;
      
      }
      
   }

   /**
    * f0 -> <IDENTIFIER>
    */
   public R visit(Label n, A argu) {
      R _ret=null;
      Arg T=(Arg) argu;
      
      if(T.pass==0){
      String a =(String) n.f0.accept(this, argu);
      if(!G.labels.containsKey(a))
      G.labels.put(a,G.N+1);
      return (R) a;
      }
      else
      {
      String a=(String) n.f0.accept(this, argu);
      GN.sim_exp=a;
      if(label)str=str+a+'\n';
      return (R) a;
      
      }
   }

}
