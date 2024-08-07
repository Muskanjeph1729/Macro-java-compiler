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

   public class pass {
      int choice_made;
      String valop;
      String isreg;
   }
   int flag = 0;
   int int1, int2, int3;

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
      if ( n.present() ){
         return n.node.accept(this,argu);
      }
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
    * f1 -> "["
    * f2 -> IntegerLiteral()
    * f3 -> "]"
    * f4 -> "["
    * f5 -> IntegerLiteral()
    * f6 -> "]"
    * f7 -> "["
    * f8 -> IntegerLiteral()
    * f9 -> "]"
    * f10 -> StmtList()
    * f11 -> "END"
    * f12 -> ( SpillInfo() )?
    * f13 -> ( Procedure() )*
    * f14 -> <EOF>
    */
   public R visit(Goal n, A argu) {
      R _ret = null;
      System.out.println(".text\n.globl main\nmain:");
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      String x = (String)n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      String y = (String)n.f5.accept(this, argu);
      n.f6.accept(this, argu);
      n.f7.accept(this, argu);
      String z = (String) n.f8.accept(this, argu);

      Integer a1 = Integer.parseInt(x);
      Integer a2 = Integer.parseInt(y);
      Integer a3 = Integer.parseInt(z);

      int1 = Integer.parseInt(x);
      int2 = Integer.parseInt(y);
      int3 = Integer.parseInt(z);

      Integer mem = (a2 + 1) * 4;

      System.out.println("move $fp, $sp");
      String out1 = "sw $ra, -4($fp)\nsubu $sp, $sp, " + mem;
      System.out.println(out1);
      n.f9.accept(this, argu);
      n.f10.accept(this, argu);

      String out2 = "addu $sp, $sp, " + mem + "\nlw $ra, -4($fp)\nj $ra ";
      System.out.println(out2);
      n.f11.accept(this, argu);
      n.f12.accept(this, argu);
      n.f13.accept(this, argu);
      n.f14.accept(this, argu);
      System.out.println(".text \n.globl _halloc \n_halloc: \nli $v0, 9 \nsyscall \nj $ra"); 
      System.out.println(".text \n.globl _print \n_print: \nli $v0, 1 \nsyscall \nla $a0, newl \nli $v0, 4 \nsyscall \nj $ra\n.data\n.align 0\n");
      //System.out.println(".text \n.globl _error \n_error: \nli $v0, 4\nsyscall\nli $v0, 10\nsyscall");
      //System.out.println(".text \n.globl _exitret \n_exitret: \nli $v0, 10\nsyscall \n.data\n.align 0\nnewline: 	.asciiz \"\\n\"");
      System.out.println("newl:  .asciiz \"\\n\"");
      System.out.println(".data\n.align 0\nstr_er:	.asciiz \" ERROR: abnormal termination\\n \"");
      return _ret;
   }

   /**
    * f0 -> ( ( Label() )? Stmt() )*
    */
   public R visit(StmtList n, A argu) {
      R _ret = null;
      flag = 1;
      n.f0.accept(this, argu);
      flag = 0;
      return _ret;
   }

   /**
    * f0 -> Label()
    * f1 -> "["
    * f2 -> IntegerLiteral()
    * f3 -> "]"
    * f4 -> "["
    * f5 -> IntegerLiteral()
    * f6 -> "]"
    * f7 -> "["
    * f8 -> IntegerLiteral()
    * f9 -> "]"
    * f10 -> StmtList()
    * f11 -> "END"
    * f12 -> ( SpillInfo() )?
    */
   public R visit(Procedure n, A argu) {
      R _ret=null;
      String function = (String) n.f0.accept(this, argu);
      String out = ".text\n.globl           " + function;
      System.out.println(out);
      System.out.println(function+":");
      n.f1.accept(this, argu);
      String x = (String)n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      String y = (String)n.f5.accept(this, argu);
      n.f6.accept(this, argu);
      n.f7.accept(this, argu);
      String z = (String) n.f8.accept(this, argu);

      Integer a1 = Integer.parseInt(x);
      Integer a2 = Integer.parseInt(y);
      Integer a3 = Integer.parseInt(z);

      int1 = Integer.parseInt(x);
      int2 = Integer.parseInt(y);
      int3 = Integer.parseInt(z);

      Integer mem= (a2 + 2) * 4;
      
      String out1 = "sw $fp, -8($sp)\nmove $fp, $sp\nsw $ra, -4($fp)\nsubu $sp, $sp, " + mem;
      System.out.println(out1);
      n.f9.accept(this, argu);
      n.f10.accept(this, argu);
      n.f11.accept(this, argu);
      n.f12.accept(this, argu);
      String out2 = "addu $sp, $sp, " + mem + "\nlw $ra, -4($fp)\nlw $fp, " + "-8" + "($sp)\nj $ra";
      System.out.println(out2);
      return _ret;
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
    *       | ALoadStmt()
    *       | AStoreStmt()
    *       | PassArgStmt()
    *       | CallStmt()
    */
   public R visit(Stmt n, A argu) {
      R _ret = null;
      Integer prev = flag;
      flag = 0;
      n.f0.accept(this, argu);
      flag = prev;
      return _ret;
   }

   /**
    * f0 -> "NOOP"
    */
   public R visit(NoOpStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      System.out.println("nop");
      return _ret;
   }

   /**
    * f0 -> "ERROR"
    */
   public R visit(ErrorStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "CJUMP"
    * f1 -> Reg()
    * f2 -> Label()
    */
   public R visit(CJumpStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      String a =(String)n.f1.accept(this, argu);
      String b = (String) n.f2.accept(this, argu);
      String out = "beqz $" + a + b;
      System.out.println(out);
      return _ret;
   }

   /**
    * f0 -> "JUMP"
    * f1 -> Label()
    */
   public R visit(JumpStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      String a = (String) n.f1.accept(this, argu);
      String out = "b " + a;
      System.out.println(out);
      return _ret;
   }

   /**
    * f0 -> "HSTORE"
    * f1 -> Reg()
    * f2 -> IntegerLiteral()
    * f3 -> Reg()
    */
   public R visit(HStoreStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      String a=(String)n.f1.accept(this, argu);
      String offset=(String)n.f2.accept(this, argu);
      String b = (String) n.f3.accept(this, argu);
      String out = "sw $" + b + ", " + offset + "($" + a + ")";
      System.out.println(out);
      return _ret;
   }

   /**
    * f0 -> "HLOAD"
    * f1 -> Reg()
    * f2 -> Reg()
    * f3 -> IntegerLiteral()
    */
   public R visit(HLoadStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      String a=(String)n.f1.accept(this, argu);
      String b=(String)n.f2.accept(this, argu);
      String offset = (String) n.f3.accept(this, argu);
      String out = "lw $" + a + ", " + offset + "($" + b + ")";
      System.out.println(out);
      return _ret;
   }

   /**
    * f0 -> "MOVE"
    * f1 -> Reg()
    * f2 -> Exp()
    */
   public R visit(MoveStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      String a = (String)n.f1.accept(this, argu);
      n.f2.accept(this, (A)a);
      return _ret;
   }

   /**
    * f0 -> "PRINT"
    * f1 -> SimpleExp()
    */
   public R visit(PrintStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      pass a = (pass) n.f1.accept(this, argu);
      if (a.choice_made == 0) {
         String out = "move $a0 $" + a.valop;
         System.out.println(out);
      }
      else if (a.choice_made == 1) {
         String out = "li $a0 " + a.valop;
         System.out.println(out);
      }
      else if (a.choice_made == 2) {
         String out = "la $a0 " + a.valop;
         System.out.println(out);
      }
      System.out.println("jal _print \n");
      return _ret;
   }

   /**
    * f0 -> "ALOAD"
    * f1 -> Reg()
    * f2 -> SpilledArg()
    */
   public R visit(ALoadStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      String a = (String)n.f1.accept(this, argu);
      String b = (String) n.f2.accept(this, argu);
      String out = "lw $" + a + ", " + b;
      System.out.println(out);
      return _ret;
   }

   /**
    * f0 -> "ASTORE"
    * f1 -> SpilledArg()
    * f2 -> Reg()
    */
   public R visit(AStoreStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      String a = (String) n.f1.accept(this, argu);
      String b = (String) n.f2.accept(this, argu);
      String out = "sw $" + b + ", " + a;
      System.out.println(out);
      return _ret;
   }

   /**
    * f0 -> "PASSARG"
    * f1 -> IntegerLiteral()
    * f2 -> Reg()
    */
   public R visit(PassArgStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      String a = (String) n.f1.accept(this, argu);
      String b = (String) n.f2.accept(this, argu);

      Integer val = Integer.parseInt(a);

      val = (-1) * (val * 4 + 8);

      String out = "sw $" + b + ", " + val + "($sp)";
      System.out.println(out);
      return _ret;
   }

   /**
    * f0 -> "CALL"
    * f1 -> SimpleExp()
    */
   public R visit(CallStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      pass a=(pass)n.f1.accept(this, argu);
      if (a.choice_made == 0) {
         String out = "jalr $" + a.valop;
         System.out.println(out);
      }
      else if (a.choice_made == 2) {
         String out = "jal " + a.valop;
         System.out.println(out);
      }
       return _ret;
   }

   /**
    * f0 -> HAllocate()
    *       | BinOp()
    *       | SimpleExp()
    */
   public R visit(Exp n, A argu) {
      R _ret = null;
      pass a=(pass)n.f0.accept(this, argu);
      
      String b = (String) argu;
      if (n.f0.which == 2) {
         if (a.choice_made == 0) {
            String out = "move $" + b + " $" + a.valop;
            System.out.println(out);
         }
         else if (a.choice_made == 1) {
            String out = "li $" + b + " " + a.valop;
            System.out.println(out);
         }
         else if (a.choice_made == 2) {
            String out = "la $" + b + a.valop;
            System.out.println(out);
         }
      }
      return _ret;
   }

   /**
    * f0 -> "HALLOCATE"
    * f1 -> SimpleExp()
    */
   public R visit(HAllocate n, A argu) {
      
      n.f0.accept(this, argu);
      pass a = (pass)n.f1.accept(this, argu);
      String b = (String) argu;
      if (a.choice_made == 1) {
         String out = "li $a0 " + a.valop + "\njal _halloc \nmove $" + b + " $v0";
         System.out.println(out);
      }
      else if (a.choice_made == 0) {
         String out = "move $a0 $" + a.valop + "\njal _halloc \nmove $" + b + " $v0";
         System.out.println(out);
      }
        
      pass anew = new pass();
      anew.choice_made = -1;
      
      return (R) anew;
   }

   /**
    * f0 -> Operator()
    * f1 -> Reg()
    * f2 -> SimpleExp()
    */
   public R visit(BinOp n, A argu) {

      String op = (String)n.f0.accept(this, argu);
      String a = (String)n.f1.accept(this, argu);
      pass b = (pass) n.f2.accept(this, argu);
      
      if (b.choice_made == 0) {
         String out = op + " $" + (String) argu + ", $" + a + ", $" + b.valop;
         System.out.println(out);
      }
      if (b.choice_made == 1) {
         String out = op + " $" + (String) argu + ", $" + a + ", " + b.valop;
         System.out.println(out);
      }
      if (b.choice_made == 2) {
         String out = "la $v0 " + b.valop;
         System.out.println(out);
         out = op + " $" + (String) argu + ", $" + a + ", $v0";
         System.out.println(out);
      }
      pass bnew=new pass();
      bnew.choice_made =-1;
      return (R) bnew;
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
      n.f0.accept(this, argu);
      Integer a = n.f0.which;
      String out = "";
      if (a == 0) {
         out = "sle ";
      }
      else if (a == 1) {
         out = "sne ";
      }
      else if (a == 2) {
         out = "add ";
      }
      else if (a == 3) {
         out = "sub ";
      }
      else if (a == 4) {
         out = "mul ";
      }
      else if (a == 5) {
         out = "div ";
      }
      if (a == 0 || a == 1 || a == 2 || a == 3 || a == 4 || a == 5) {
         return (R) out;
      }
      return _ret;
   }

   /**
    * f0 -> "SPILLEDARG"
    * f1 -> IntegerLiteral()
    */
   public R visit(SpilledArg n, A argu) {
      R _ret = null;
      n.f0.accept(this, argu);
      String r = (String) n.f1.accept(this, argu);
      Integer val = Integer.parseInt(r);

      int mem = int1;

      if (mem >= 4) {
         mem = mem - 4;
      } else {
         mem = 0;
      }
      // if (mem < 4) {
      //    mem = 0;
      // }
      // else {
      //    mem = mem - 4;
      // }
      // val = val + 3 + mem;
      // val = val * 4;
      // val = (-1) * val;
      val = (4) * (val + mem);
      String out = " " + val + "($sp)";
      return (R) (out);
   }

   /**
    * f0 -> Reg()
    *       | IntegerLiteral()
    *       | Label()
    */
   public R visit(SimpleExp n, A argu) {
      R _ret=null;
      pass a = new pass();
      a.choice_made = n.f0.which;
      a.valop = (String) n.f0.accept(this, argu);
      if (n.f0.which == 0) {
         a.isreg = "Register";
      }
      else if (n.f0.which == 1) {
         a.isreg = "Integer";
      }
      else if (n.f0.which == 2) {
         a.isreg = "Label";
      }
      return (R)a ;
   }

   /**
    * f0 -> "a0"
    *       | "a1"
    *       | "a2"
    *       | "a3"
    *       | "t0"
    *       | "t1"
    *       | "t2"
    *       | "t3"
    *       | "t4"
    *       | "t5"
    *       | "t6"
    *       | "t7"
    *       | "s0"
    *       | "s1"
    *       | "s2"
    *       | "s3"
    *       | "s4"
    *       | "s5"
    *       | "s6"
    *       | "s7"
    *       | "t8"
    *       | "t9"
    *       | "v0"
    *       | "v1"
    */
   public R visit(Reg n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);

      Integer a = n.f0.which;
      if (a == 0) {
         return (R) "a0 ";
      }
      else if (a == 1) {
         return (R) "a1 ";
      }
      else if (a == 2) {
         return (R) "a2 ";
      }
      else if (a == 3) {
         return (R) "a3 ";
      }
      else if (a == 4) {
         return (R) "t0 ";
      }
      else if (a == 5) {
         return (R) "t1 ";
      }
      else if (a == 6) {
         return (R) "t2 ";
      }
      else if (a == 7) {
         return (R) "t3 ";
      }
      else if (a == 8) {
         return (R) "t4 ";
      }
      else if (a == 9) {
         return (R) "t5 ";
      }
      else if (a == 10) {
         return (R) "t6 ";
      }
      else if (a == 11) {
         return (R) "t7 ";
      }
      else if (a == 12) {
         return (R) "s0 ";
      }
      else if (a == 13) {
         return (R) "s1 ";
      }
      else if (a == 14) {
         return (R) "s2 ";
      }
      else if (a == 15) {
         return (R) "s3 ";
      }
      else if (a == 16) {
         return (R) "s4 ";
      }
      else if (a == 17) {
         return (R) "s5 ";
      }
      else if (a == 18) {
         return (R) "s6 ";
      }
      else if (a == 19) {
         return (R) "s7 ";
      }
      else if (a == 20) {
         return (R) "t8 ";
      }
      else if (a == 21) {
         return (R) "t9 ";
      }
      else if (a == 22) {
         return (R) "v0 ";
      }
      else if (a == 23) {
         return (R) "v1 ";
      }

      return _ret;
   }

   /**
    * f0 -> <INTEGER_LITERAL>
    */
   public R visit(IntegerLiteral n, A argu) {
      R _ret=null;
      String a = (String) n.f0.accept(this, argu);
      return (R)a;
   }

   /**
    * f0 -> <IDENTIFIER>
    */
   public R visit(Label n, A argu) {
      R _ret = null;
      String a = (String) n.f0.accept(this, argu);
      if (flag == 1) {
         String out=":  ";
         System.out.println(a + out);
      }
      return (R)a;
   }

   /**
    * f0 -> "//"
    * f1 -> SpillStatus()
    */
   public R visit(SpillInfo n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> <SPILLED>
    *       | <NOTSPILLED>
    */
   public R visit(SpillStatus n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      return _ret;
   }

}
