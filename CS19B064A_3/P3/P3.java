import syntaxtree.*;
import visitor.*;

public class P3 {
   public static void main(String [] args) {
      try {
         Node root = new MiniJavaParser(System.in).Goal();

         final_parser_2 trying_2 = new final_parser_2();
         root.accept(trying_2);
         root.accept(trying_2);
      }
      catch (ParseException e) {
         System.out.println(e.toString());
      }
   }
} 