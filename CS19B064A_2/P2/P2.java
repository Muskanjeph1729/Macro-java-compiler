import syntaxtree.*;
import visitor.*;

public class P2 {
   public static void main(String [] args) {
      try {
         Node root = new MiniJavaParser(System.in).Goal();
         first_iteration f = new first_iteration();
         root.accept(f);
         second_iteration s = new second_iteration();
         s.stored_data = f.stored_data;
         root.accept(s);
         System.out.println("Program type checked successfully");
      }
      catch (ParseException e) {
         System.out.println(e.toString());
      }
   }
} 