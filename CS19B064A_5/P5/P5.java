import syntaxtree.*;
import visitor.*;

@SuppressWarnings("unchecked")

public class P5 {
   public static void main(String [] args) {
      try {
         Node root = new MiniRAParser(System.in).Goal();
         root.accept(new GJDepthFirst(),null); // Your assignment part is invoked here.
         //System.out.println("Program parsed successfully");
      }
      catch (ParseException e) {
         System.out.println(e.toString());
      }
   }
} 

