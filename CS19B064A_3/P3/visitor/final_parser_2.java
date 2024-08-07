package visitor;
import syntaxtree.*;
import java.util.*;
//import javafx.util.Pair;

class Pair {

   String first;
   String second;

  public Pair(String first, String second) {
    this.first = first;
    this.second = second;
  }

  public String getKey() {
    return first;
  }

  public void setFirst(String first) {
    this.first = first;
  }

  public String getValue() {
    return second;
  }

  public void setSecond(String second) {
    this.second = second;
  }

  @Override
  public String toString() {
    return "Pair{" +
        "first=" + first +
        ", second=" + second +
        '}';
  }
}


public class final_parser_2 extends GJNoArguDepthFirst<String> {

   
   public ArrayList<String> class_list = new ArrayList<String>();
   public HashMap<String,HashMap<String,ArrayList<Obj>>> class_methods = new HashMap<String,HashMap<String,ArrayList<Obj>>>();
   public HashMap<String,HashMap<String,ArrayList<Obj>>> class_methods_2 = new HashMap<String,HashMap<String,ArrayList<Obj>>>();
   public HashMap<String,HashMap<String,Integer>> class_methods_index = new HashMap<String,HashMap<String,Integer>>();
   public HashMap<String,ArrayList<Pair>> imported_class_methods = new HashMap<String,ArrayList<Pair>>();
   public HashMap<String,ArrayList<Obj>> class_variables = new HashMap<String,ArrayList<Obj>>();
   public HashMap<String,ArrayList<Obj>> class_variables_2 = new HashMap<String,ArrayList<Obj>>();
   HashMap<String,Integer> class_num_variables = new HashMap<String,Integer>();
   public HashMap<String,String> parent_class = new HashMap<String,String>();
   public HashMap<String,ArrayList<String>> children_class = new HashMap<String,ArrayList<String>>();
   HashMap<String, Obj>  local_scope = new HashMap<String, Obj>();
   String curr_class = ""; 
   String curr_type = "";
   boolean debug = false ;
   boolean primary_expression_print = false;
   boolean update_itdentifier = false;
   String expression_append = "";

   String current_class = "";
   boolean is_extended = false;
   boolean is_method_param = false;
   int variable_index;
   int method_index;
   int local_variable_index;
   //String curr_type = "";
   String current_method_name;

   public void do_extension(){
      for(String class_name : class_list){
         method_index = class_methods_index.get(class_name).size();
         variable_index = class_variables_2.get(class_name).size();
         String temp_class = class_name;
         int var_count = variable_index;
         while(temp_class!=null && parent_class.containsKey(temp_class)){
            String parent = parent_class.get(temp_class);
            for(Obj temp : class_variables_2.get(parent)){
               boolean not_present = true;
               for(Obj temp2 : class_variables_2.get(class_name)){
                  //System.out.println( + "  " + temp2.name);
                  //System.out.println(temp.name + "  " + temp2.name);
                  if(temp.name == temp2.name){
                     //System.out.println(temp.name + "  " + temp2.name);
                     not_present=false;
                  }
               }

               if(not_present){
                  Obj new_temp = new Obj();
                  new_temp.name = temp.name;
                  new_temp.index = variable_index;
                  new_temp.origin = temp.origin;
                  new_temp.type = temp.type;
                  //new_temp.index = variable_index;
                  variable_index++;
                  var_count++;
                  class_variables.get(class_name).add(new_temp);
               }
               else{
                  var_count++;
               }
            }
            temp_class = parent_class.get(temp_class);
            for( String entry : class_methods_2.get(parent).keySet() ){
               if(!class_methods.get(class_name).containsKey(entry)){
                  class_methods.get(class_name).put(entry,class_methods_2.get(parent).get(entry));
                  method_index++;
                  Pair p = new Pair(entry,parent);
                  imported_class_methods.get(class_name).add(p);
                  //class_methods_index.get(class_name).put(entry,method_index);
               }

           }
         }
         class_num_variables.put(class_name,var_count);
      }
   }

   public void add_new_class(String class_name){
      class_list.add(class_name);
      class_methods.put(class_name,new HashMap<String,ArrayList<Obj>>());
      class_methods_2.put(class_name,new HashMap<String,ArrayList<Obj>>());
      class_methods_index.put(class_name, new HashMap<String,Integer>());
      class_variables.put(class_name,new ArrayList<Obj>());
      class_variables_2.put(class_name,new ArrayList<Obj>());
      imported_class_methods.put(class_name,new ArrayList<Pair>());
      children_class.put(class_name,new ArrayList<String>());
   }

   boolean build_class_hier = true;

   int temp_top  = 20;
   
   int label_top = 0;
   int param_count = 0;
   int insidefn   = 0;
   boolean typelookup = false;
   boolean heaped = false;
   String print_string = "";
   boolean indentifier_print = true;
   boolean expression_print = false;
   boolean from_allocation = false;
   

   public String getNewTemp(){
      temp_top++;
      return Integer.toString(temp_top-1);
   }

   public String getNewLabel(){
      label_top++;
      return "L" + Integer.toString(label_top-1);
   }

   public Obj get_var(String id){
         if(local_scope.containsKey(id))
            return local_scope.get(id);
         for(Obj temp : class_variables.get(curr_class)){
         if(temp.name == id)
            return temp;
         }
         Obj temp = new Obj();
         temp.index = Integer.parseInt(getNewTemp());
         add_local_var(id,"local",temp.index);
         return temp;
   }
   
   
   public String add_local_var(String id, String type){
      String ind = getNewTemp();
      Obj o =  new Obj();
      o.type = type;
      o.index = Integer.parseInt(ind);
      o.origin = "method";
      local_scope.put(id, o);
      
      return ind;
   }
   
   public void add_local_var(String id, String type, int i){
      Obj o =  new Obj();
      o.type = type;
      o.index = i;
      o.origin = "method";
      local_scope.put(id, o);
   }
   

   //
   // Auto class visitors--probably don't need to be overridden.
   //
   public String visit(NodeList n) {
      String _ret="";
      int _count=0;
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
         e.nextElement().accept(this);
         _count++;
      }
      return "";
   }

   public String visit(NodeListOptional n) {
      if ( n.present() ) {
         String _ret="";
         int _count=0;
         for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
            e.nextElement().accept(this);
            _count++;
         }
         return "";
      }
      else
         return "";
   }

   public String visit(NodeOptional n) {
      if ( n.present() )
         return n.node.accept(this);
      else
         return "";
   }

   public String visit(NodeSequence n) {
      String _ret="";
      int _count=0;
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
         e.nextElement().accept(this);
         _count++;
      }
      return "";
   }

   public String visit(NodeToken n) { return ""; }

   //
   // User-generated visitor methods below
   //

   /**
    * f0 -> MainClass()
    * f1 -> ( TypeDeclaration() )*
    * f2 -> <EOF>
    */
   public String visit(Goal n) {
      String _ret="";
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      //class_variables_2.putAll(class_variables);
      //System.out.println(class_methods.get("Cat"));
      do_extension();
      build_class_hier = false;
      
      for(String class_name : class_list){
         int i=0;
         for(String method_name : class_methods.get(class_name).keySet()){
            //String print_class_name = n.f1.f0.tokenImage;
            // for(Pair imported_pair : imported_class_methods.get(n.f1.f0.tokenImage)){
            //    if(imported_pair.getKey()==method_name){
            //       print_class_name=imported_pair.getValue();
            //       break;
            //    }

            // }
            // String temp = getNewTemp();
            // System.out.println( "MOVE TEMP " + temp + " " + print_class_name+"_"+method_name);
            // System.out.println( "HSTORE TEMP " + temp1 + " " + Integer.toString(i*4) + " TEMP " + temp);
            class_methods_index.get(class_name).put(method_name,i);
            i++;
           }
      }

      return "";
   }

   /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "{"
    * f3 -> "public"
    * f4 -> "static"
    * f5 -> "void"
    * f6 -> "main"
    * f7 -> "("
    * f8 -> "String"
    * f9 -> "["
    * f10 -> "]"
    * f11 -> Identifier()
    * f12 -> ")"
    * f13 -> "{"
    * f14 -> PrintStatement()
    * f15 -> "}"
    * f16 -> "}"
    */
   public String visit(MainClass n) {
      if(build_class_hier){
         add_new_class(n.f1.f0.tokenImage);
         current_class = n.f1.f0.tokenImage;
         return "";
      }
      String _ret="";
      if(debug)
      System.out.println("MainClass");
      System.out.println( "MAIN");
      n.f14.accept(this);
      if(debug)
      System.out.println("MainClass");      
      System.out.println( "END");
      //System.out.println(_ret);
      return "";
   }

   /**
    * f0 -> ClassDeclaration()
    *       | ClassExtendsDeclaration()
    */
   public String visit(TypeDeclaration n) {
      String _ret="";
      n.f0.accept(this);
      return "";
   }

   /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "{"
    * f3 -> ( VarDeclaration() )*
    * f4 -> ( MethodDeclaration() )*
    * f5 -> "}"
    */
   public String visit(ClassDeclaration n) {
      if(build_class_hier){
         add_new_class(n.f1.f0.tokenImage);
         current_class = n.f1.f0.tokenImage;
         variable_index = 0;
         n.f3.accept(this);
         is_method_param = true;
         method_index = 0;
         n.f4.accept(this);
         //n.f5.accept(this);
         is_method_param = false;
         return "";

      }
      String _ret="";
      curr_class = n.f1.f0.tokenImage;
      n.f3.accept(this);
      n.f4.accept(this);
      curr_class = "";
      return "";
   }

   /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "extends"
    * f3 -> Identifier()
    * f4 -> "{"
    * f5 -> ( VarDeclaration() )*
    * f6 -> ( MethodDeclaration() )*
    * f7 -> "}"
    */
   public String visit(ClassExtendsDeclaration n) {
      if(build_class_hier){
         add_new_class(n.f1.f0.tokenImage);
         parent_class.put(n.f1.f0.tokenImage,n.f3.f0.tokenImage);
         current_class = n.f1.f0.tokenImage;
         is_extended = true;
         variable_index = 0;
         n.f5.accept(this);
         is_method_param = true;
         method_index = 0;
         n.f6.accept(this);
         // String temp_class = current_class;
         // while(temp_class!=null && parent_class.containsKey(temp_class)){
         //    String parent = parent_class.get(temp_class);
         //    for(Obj temp : class_variables.get(parent)){
         //       Obj new_temp = temp;
         //       new_temp.index = variable_index;
         //       variable_index++;
         //       class_variables.get(current_class).add(new_temp);
         //    }
         //    temp_class = parent_class.get(temp_class);
         //    for( String entry : class_methods.get(parent).keySet() ){
         //       if(!class_methods.get(current_class).containsKey(entry))
         //          class_methods.get(current_class).put(entry,class_methods.get(parent).get(entry));
         //          class_methods_index.get(current_class).put(entry,method_index);
         //          method_index++;

         //   }
         // }
         is_extended = false;
         is_method_param = false;
         return "";
      }
      String _ret="";
      n.f0.accept(this);
      curr_class = n.f1.f0.tokenImage;;
      n.f5.accept(this);
      n.f6.accept(this);
      curr_class = "";
      return "";
   }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    * f2 -> ";"
    */
   public String visit(VarDeclaration n) {
      if(build_class_hier){
         Obj temp_obj = new Obj();
         n.f0.accept(this);
         temp_obj.type = curr_type;
         temp_obj.name = n.f1.f0.tokenImage;
         temp_obj.origin = "class";
         temp_obj.index = variable_index;
         class_variables.get(current_class).add(temp_obj);
         class_variables_2.get(current_class).add(temp_obj);
         variable_index++;
         return "";
      }
      String _ret="";
      String type=n.f0.accept(this);
      switch(insidefn){
      case 1:
         add_local_var(n.f1.f0.tokenImage, type);
         //System.out.println("***"+n.f1.f0.tokenImage+" "+type);
         break;
      case 0:
         break;
      }
      return "";
   }

   /**
    * f0 -> "public"
    * f1 -> Type()
    * f2 -> Identifier()
    * f3 -> "("
    * f4 -> ( FormalParameterList() )?
    * f5 -> ")"
    * f6 -> "{"
    * f7 -> ( VarDeclaration() )*
    * f8 -> ( Statement() )*
    * f9 -> "return"
    * f10 -> Expression()
    * f11 -> ";"
    * f12 -> "}"
    */
   public String visit(MethodDeclaration n) {
      if(build_class_hier){
         current_method_name = n.f2.f0.tokenImage;
         class_methods.get(current_class).put(current_method_name,new ArrayList<Obj>() );
         class_methods_2.get(current_class).put(current_method_name,new ArrayList<Obj>() );
         //class_methods_index.get(current_class).put(current_method_name,method_index);
         method_index++;
         local_variable_index = 0;
         n.f4.accept(this);
         return "";
      }
      String _ret="";
      insidefn = 1;
      param_count = 0;
        System.out.print( "\n"+curr_class + "_" + n.f2.f0.tokenImage);
      //System.out.print( n.f2.f0.tokenImage);
      System.out.print( " [");
      String num_params = n.f4.accept(this);
      switch(num_params){
      case "":
         System.out.print( "1]\nBEGIN\n");
         break;
      default:
         System.out.print( num_params +"]\n BEGIN\n");
         break;
      } 
      n.f7.accept(this);
      n.f8.accept(this);
      String return_temp = n.f10.accept(this);
      System.out.print("RETURN TEMP " + return_temp);
      System.out.print( "\nEND\n");
      insidefn = 0;
      local_scope.clear();
      return "";
   }

   /**
    * f0 -> FormalParameter()
    * f1 -> ( FormalParameterRest() )*
    */
   public String visit(FormalParameterList n) {
      if(build_class_hier){
         n.f0.accept(this);
         n.f1.accept(this);
         return "";
      }
      String _ret="";
      n.f0.accept(this);
      n.f1.accept(this);
      return Integer.toString(param_count+1);
   }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    */
   public String visit(FormalParameter n) {
      if(build_class_hier){
         Obj temp_obj = new Obj();
         n.f0.accept(this);
         temp_obj.type = curr_type;
         temp_obj.name = n.f1.f0.tokenImage;
         temp_obj.origin = "method";
         temp_obj.index = local_variable_index;
         class_methods.get(current_class).get(current_method_name).add(temp_obj);
         class_methods_2.get(current_class).get(current_method_name).add(temp_obj);
         return "";
      }
        param_count++;
      String _ret="";
      String type = n.f0.accept(this);
      add_local_var(n.f1.f0.tokenImage, type, param_count);
      return "";
   }

   /**
    * f0 -> ","
    * f1 -> FormalParameter()
    */
   public String visit(FormalParameterRest n) {
      String _ret="";
      n.f1.accept(this);
      return "";
   }

   /**
    * f0 -> ArrayType()
    *       | BooleanType()
    *       | IntegerType()
    *       | Identifier()
    */
   public String visit(Type n) {
      if(build_class_hier){
         n.f0.accept(this);
         return "";
      }
      typelookup = true;
     n.f0.accept(this);
     typelookup = false;
     
     String _ret = curr_type;
      return _ret;
   }

   /**
    * f0 -> "int"
    * f1 -> "["
    * f2 -> "]"
    */
   public String visit(ArrayType n) {
      curr_type = "int[]";
      return "";
   }

   /**
    * f0 -> "boolean"
    */
   public String visit(BooleanType n) {
      curr_type = "boolean";
      return "";
   }

   /**
    * f0 -> "int"
    */
   public String visit(IntegerType n) {
      curr_type = "int";
      return "";
   }

   /**
    * f0 -> Block()
    *       | AssignmentStatement()
    *       | ArrayAssignmentStatement()
    *       | IfStatement()
    *       | WhileStatement()
    *       | DoStatement()
    *       | PrintStatement()
    */
   public String visit(Statement n) {
      String _ret="";
      n.f0.accept(this);
      return "";
   }

   /**
    * f0 -> "{"
    * f1 -> ( Statement() )*
    * f2 -> "}"
    */
   public String visit(Block n) {
      String _ret="";
      n.f1.accept(this);
      return "";
   }

   /**
    * f0 -> Identifier()
    * f1 -> "="
    * f2 -> Expression()
    * f3 -> ";"
    */
   public String visit(AssignmentStatement n) {
      String _ret="";
      heaped = false;
      String temp_result = n.f2.accept(this);
      String curr_class1 = curr_type;
      update_itdentifier = true;
      String s = n.f0.accept(this);
      String curr_class2 = curr_type;
      if(curr_class1 != curr_class2){
         Obj temp_obj = get_var(n.f0.f0.tokenImage);
         temp_obj.type = curr_class1;
      }
      update_itdentifier = false;
      
      if(heaped){
         //String temp = getNewTemp();
         if(debug)
         System.out.println("AssignmentStatement");
         //System.out.println("MOVE TEMP " + temp + " TEMP " + temp_result);
         //String temp1 = getNewTemp();
         if(debug)
         System.out.println("AssignmentStatement");
         //String result = n.f0.accept(this);
         System.out.println( "HSTORE " + print_string + " TEMP " + temp_result);
      }
      else{
         String temp = getNewTemp();
         if(debug)
         System.out.println("AssignmentStatement");
         //System.out.println("MOVE TEMP " + temp + " TEMP " + temp_result);
         update_itdentifier = false;
         //String s = n.f0.accept(this);
         update_itdentifier = false;
         if(debug)
         System.out.println("AssignmentStatement");
         System.out.println( " MOVE " + "TEMP " + s + " TEMP " + temp_result + " ");
      }

      return "";
   }

   /**
    * f0 -> Identifier()
    * f1 -> "["
    * f2 -> Expression()
    * f3 -> "]"
    * f4 -> "="
    * f5 -> Expression()
    * f6 -> ";"
    */
   public String visit(ArrayAssignmentStatement n) {
      String _ret="";
      heaped = false;
      update_itdentifier = true;
      update_itdentifier = false;
      String array_index_2 = n.f2.accept(this);
      String result_temp_2 = n.f5.accept(this);
      String array_temp = getNewTemp();
      String array_index = getNewTemp();
      String result_temp = getNewTemp();
      String array_temp_2 = n.f0.accept(this);
      // if(debug)
      // System.out.println("ArrayAssignmentStatement");
      // System.out.println("MOVE TEMP " + array_temp + " TEMP " + array_temp_2);
      // System.out.println("MOVE TEMP " + array_index + " TEMP " + array_index_2);
      // System.out.println("MOVE TEMP " + result_temp + " TEMP " + result_temp_2);


      if(heaped){
         String index_plus1 = getNewTemp();
         String final_index = getNewTemp();
         String final_array = getNewTemp();
         System.out.println( "MOVE TEMP " + index_plus1 + " PLUS TEMP " + array_index_2 + " 1");
         System.out.println( "MOVE TEMP " + final_index + " TIMES TEMP " + index_plus1 + " 4");
         System.out.println( "MOVE TEMP " + final_array + " PLUS TEMP " + array_temp_2 + " TEMP " + final_index);
         String return_address_string = getNewTemp();
         System.out.println( "HSTORE TEMP " + final_array + " 0 " + " TEMP " + result_temp_2);
      }
      else{
         String index_plus1 = getNewTemp();
         String final_index = getNewTemp();
         String final_array = getNewTemp();
         System.out.println( "MOVE TEMP " + index_plus1 + " PLUS TEMP " + array_index_2 + " 1");
         System.out.println( "MOVE TEMP " + final_index + " TIMES TEMP " + index_plus1 + " 4");
         System.out.println( "MOVE TEMP " + final_array + " PLUS TEMP " + array_temp_2 + " TEMP " + final_index);
         String return_address_string = getNewTemp();
         System.out.println( "HSTORE TEMP " + final_array+ " 0 " + " TEMP " + result_temp_2);

      }
      return "";
   }

   /**
    * f0 -> IfthenElseStatement()
    *       | IfthenStatement()
    */
   public String visit(IfStatement n) {
      String _ret = "";
      n.f0.accept(this);
      return "";
   }

   /**
    * f0 -> "if"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    */
   public String visit(IfthenStatement n) {
      String _ret="";
      String label1 = getNewLabel();
      //expression_print = true;
      String expression_result = n.f2.accept(this);
      //expression_print = false;
      if(debug)
      System.out.println("IfthenStatement");
      System.out.println( "CJUMP TEMP " + expression_result + " " + label1);
      n.f4.accept(this);
      if(debug)
      System.out.println("IfthenStatement");
      System.out.println( label1 + " NOOP");
      return "";
   }

   /**
    * f0 -> "if"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    * f5 -> "else"
    * f6 -> Statement()
    */
   public String visit(IfthenElseStatement n) {
      String _ret="";
      String label1 = getNewLabel();
      String label2 = getNewLabel();
      //expression_print = true;
      String expression_result = n.f2.accept(this);
      //expression_print = false;
      if(debug)
      System.out.println("IfthenElseStatement");
      System.out.println( "CJUMP TEMP " + expression_result + " " + label1);
      n.f4.accept(this);
      if(debug)
      System.out.println("IfthenElseStatement");
      System.out.println(" JUMP " + label2);
      System.out.println( label1  + " NOOP");
      n.f6.accept(this);
      if(debug)
      System.out.println("IfthenElseStatement");
      System.out.println( label2  + " NOOP");
      return "";
   }

   /**
    * f0 -> "while"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    */
   public String visit(WhileStatement n) {
      String _ret="";
      String label1 = getNewLabel();
      String label2 = getNewLabel();
      //expression_print = true;
      String expression_result = n.f2.accept(this);
      //expression_print = false;
      if(debug)
      System.out.println("WhileStatement");
      System.out.println(label1+" NOOP");
      
      System.out.println( "CJUMP " + "TEMP " + expression_result + " " + label2);
      n.f4.accept(this);
      String expression_result_2 = n.f2.accept(this);
      if(debug)
      System.out.println("WhileStatement");
      System.out.println("MOVE TEMP " + expression_result + " TEMP " + expression_result_2 );
      System.out.println( " JUMP " + label1 + " ");
      System.out.println( label2  + " NOOP");
      return "";
   }

   /**
    * f0 -> "do"
    * f1 -> Statement()
    * f2 -> "while"
    * f3 -> "("
    * f4 -> Expression()
    * f5 -> ")"
    * f6 -> ";"
    */
   public String visit(DoStatement n) {
      String _ret="";
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      n.f6.accept(this);
      return "";
   }

   /**
    * f0 -> "System.out.println"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> ";"
    */
   public String visit(PrintStatement n) {
      String _ret="";
      //expression_print = true;
      String expression_result = n.f2.accept(this);
      //expression_print = false;
      if(debug)
      System.out.println("PrintStatement");
      System.out.println( "PRINT TEMP " + expression_result);
      return "";
   }

   /**
    * f0 -> OrExpression()
    *       | AndExpression()
    *       | CompareExpression()
    *       | neqExpression()
    *       | AddExpression()
    *       | MinusExpression()
    *       | TimesExpression()
    *       | DivExpression()
    *       | ArrayLookup()
    *       | ArrayLength()
    *       | MessageSend()
    *       | PrimaryExpression()
    */
   public String visit(Expression n) {
      String _ret="";
      String result = n.f0.accept(this);
      return result;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "&&"
    * f2 -> PrimaryExpression()
    */
   public String visit(AndExpression n) {
      String _ret="";
      String label1 = getNewLabel();
      String label2 = getNewLabel();
      String expression1_result = n.f0.accept(this);
      if(debug)
      System.out.println("AndExpression");
      System.out.println( "CJUMP TEMP " + expression1_result + " " +label2);
      String expression2_result = n.f2.accept(this);
      if(debug)
      System.out.println("AndExpression");
      System.out.println( "CJUMP TEMP " + expression2_result + " " + label2);
      String result_temp = getNewTemp();
      System.out.println( "MOVE TEMP " + result_temp + " 1");
      System.out.println( "JUMP " + label1);
      System.out.println( label2 + " NOOP");
      //System.out.println( "JUMP " + label2 + " \n";
      System.out.println( "MOVE TEMP " + result_temp + " 0");
      System.out.println( label1 + " NOOP");
      return result_temp;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "||"
    * f2 -> PrimaryExpression()
    */
   public String visit(OrExpression n) {
      String _ret="";
      String label1 = getNewLabel();
      String label2 = getNewLabel();
      String expression1_result = n.f0.accept(this);
      if(debug)
      System.out.println("OrExpression");
      System.out.println( "CJUMP TEMP " + expression1_result + label1);
      String expression2_result = n.f2.accept(this);
      if(debug)
      System.out.println("OrExpression");
      System.out.println( "CJUMP TEMP " + expression2_result + label1);
      String result_temp = getNewTemp();
      System.out.println( "MOVE TEMP " + result_temp + " 0");
      System.out.println( "JUMP " + label2);
      System.out.println( label1 + " NOOP");
      //System.out.println( "JUMP " + label1 + " \n";
      System.out.println( "MOVE TEMP " + result_temp + " 1");
      System.out.println( label2+ " NOOP");
      return result_temp;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "<="
    * f2 -> PrimaryExpression()
    */
   public String visit(CompareExpression n) {
      String _ret="";
      String expression1 = n.f0.accept(this);
      String expression1_result = getNewTemp();
      String expression2_result = getNewTemp();
      String expression2 = n.f2.accept(this);
      if(debug)
      System.out.println("CompareExpression");
      //System.out.println("MOVE TEMP " + expression1_result + " TEMP " + expression1);
      //System.out.println("MOVE TEMP " + expression2_result + " TEMP " + expression2);
      String comparision_result = getNewTemp();
      //String label = getNewLabel();
      if(debug)
      System.out.println("CompareExpression");
      System.out.println( "MOVE TEMP " + comparision_result + " LE TEMP " + expression1 + " TEMP " + expression2);
      return comparision_result;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "!="
    * f2 -> PrimaryExpression()
    */
   public String visit(neqExpression n) {
      String _ret="";
      String expression1_result = n.f0.accept(this);
      String expression2_result = n.f2.accept(this);
      String comparision_result = getNewTemp();
      if(debug)
      System.out.println("neqExpression");
      System.out.println( "MOVE TEMP " + comparision_result + " NE TEMP " + expression1_result + " TEMP " + expression2_result);
      return comparision_result;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "+"
    * f2 -> PrimaryExpression()
    */
   public String visit(AddExpression n) {
      String _ret="";
      //primary_expression_print = true;
      String expression1_result = n.f0.accept(this);
      String expression2_result = n.f2.accept(this);
      String comparision_result = getNewTemp();
      //primary_expression_print = false;
      if(debug)
      System.out.println("AddExpression");
      System.out.println( "MOVE TEMP " + comparision_result + " PLUS TEMP " + expression1_result + " TEMP " + expression2_result);
      return comparision_result;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "-"
    * f2 -> PrimaryExpression()
    */
   public String visit(MinusExpression n) {
      String _ret="";
      //primary_expression_print = true;
      String expression1_result = n.f0.accept(this);
      String expression2_result = n.f2.accept(this);
      //primary_expression_print = false;
      String comparision_result = getNewTemp();
      //String label = getNewLabel();
      if(debug)
      System.out.println("MinusExpression");
      System.out.println( "MOVE TEMP " + comparision_result + " MINUS TEMP " + expression1_result + " TEMP " + expression2_result);
      return comparision_result;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "*"
    * f2 -> PrimaryExpression()
    */
   public String visit(TimesExpression n) {
      String _ret="";
      //primary_expression_print = true;
      String expression1_result = n.f0.accept(this);
      String expression2_result = n.f2.accept(this);
      String comparision_result = getNewTemp();
      //primary_expression_print = false;
      //String label = getNewLabel();
      if(debug)
      System.out.println("TimesExpression");
      System.out.println( "MOVE TEMP " + comparision_result + " TIMES TEMP " + expression1_result + " TEMP " + expression2_result);
      return comparision_result;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "/"
    * f2 -> PrimaryExpression()
    */
   public String visit(DivExpression n) {
      String _ret="";
      //primary_expression_print = true;
      String expression1_result = n.f0.accept(this);
      String expression2_result = n.f2.accept(this);
      String comparision_result = getNewTemp();
      //primary_expression_print = false;
      //String label = getNewLabel();
      if(debug)
      System.out.println("DivExpression");
      System.out.println( "MOVE TEMP " + comparision_result + " DIV TEMP " + expression1_result + " TEMP " + expression2_result);
      return comparision_result;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "["
    * f2 -> PrimaryExpression()
    * f3 -> "]"
    */
   public String visit(ArrayLookup n) {
      String _ret="";
      //primary_expression_print = true;
      String array_temp = n.f0.accept(this);
      String array_temp_new = getNewTemp();
      if(debug)
      System.out.println("ArrayLookup");
      //System.out.println("MOVE TEMP " + array_temp_new + " TEMP " + array_temp);
      String array_temp_2 = n.f2.accept(this);
      String array_size = getNewTemp();
      if(debug)
      //primary_expression_print = false;
      System.out.println("ArrayLookup");
      //System.out.println( "MOVE TEMP " + array_size + " TEMP " + array_temp_2 );
      String array_size_plus1 = getNewTemp();
      System.out.println( "MOVE TEMP " + array_size_plus1 + " PLUS TEMP " + array_temp_2 + " 1");
      String array_size_times4 = getNewTemp();
      System.out.println( "MOVE TEMP " + array_size_times4 + " TIMES TEMP " + array_size_plus1 + " 4");
      String final_array_address = getNewTemp();
      System.out.println("MOVE TEMP " + final_array_address + " PLUS TEMP " + array_temp + " TEMP " + array_size_times4);
      String return_address = getNewTemp();
      System.out.println("HLOAD TEMP " + return_address + " TEMP " + final_array_address + " 0 ");
      return return_address;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> "length"
    */
   public String visit(ArrayLength n) {
      String _ret="";
      String array_initial = getNewTemp();
      String array_local = n.f0.accept(this);
      if(debug)
      System.out.println("ArrayLength");
      //System.out.println( "MOVE TEMP " + array_initial + " TEMP " + array_local);
      String temp2 = getNewTemp();
      String temp3 = getNewTemp();
      System.out.println( "HLOAD TEMP " + temp2 + " TEMP " + array_local + " 0 ");
      System.out.println( "MOVE TEMP " + temp3 + " TEMP " + temp2);
      return temp3;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> Identifier()
    * f3 -> "("
    * f4 -> ( ExpressionList() )?
    * f5 -> ")"
    */
   public String visit(MessageSend n) {
      String _ret="";
      String temp7 = getNewTemp();
      indentifier_print = false;
      String priexp_result = n.f0.accept(this);
      if(heaped && !from_allocation){
         if(debug)
         System.out.println("MessageSend");
         System.out.println( "HLOAD TEMP " + temp7 + " " + print_string);
      }
      else if(!from_allocation){
         if(debug)
         System.out.println("MessageSend");
         System.out.println( "MOVE TEMP " + temp7 + " " + print_string);
      }
      indentifier_print = true;
      if(from_allocation){
         temp7 = print_string;
      }
      //System.out.println("Hello");

      //System.out.println(curr_type);
      //System.out.println(class_methods_index.get(curr_type));
      int fnindex = class_methods_index.get(curr_type).get(n.f2.f0.tokenImage);
      //update_itdentifier = false;
      expression_append = "";
      String args = n.f4.accept(this);
      args = expression_append;
      //update_itdentifier = true;
      String temp9 = getNewTemp();
      String temp10 = getNewTemp();
      if(debug)
      System.out.println("MessageSend");
      if(from_allocation){
         System.out.println( "HLOAD TEMP " + temp9 + " " + temp7 + "0 ");
         //from_allocation = false;
      }
      else
         System.out.println( "HLOAD TEMP " + temp9 + " TEMP " + temp7 + " 0 ");
      // for( String checking : class_methods_index.get(curr_type).keySet())
      //    System.out.println(checking + " " + class_methods_index.get(curr_type).get(checking));
      System.out.println( "HLOAD TEMP " + temp10 + " TEMP " + temp9 + " " + Integer.toString(fnindex*4));
      String temp11 = getNewTemp();
      if(debug)
      System.out.println("MessageSend");
      if(priexp_result != " 0 "){
         System.out.println( "MOVE TEMP " + temp11 + " " + "CALL TEMP " + temp10 +" ( " + "TEMP " + priexp_result + args + " ) ");
      }
      else{
         if(from_allocation){
            System.out.println( "MOVE TEMP " + temp11 + " " + "CALL TEMP " + temp10 +" ( " + " " + temp7 + args + " ) ");
            from_allocation=false;
         }
         else{
            System.out.println( "MOVE TEMP " + temp11 + " " + "CALL TEMP " + temp10 +" ( " + "TEMP " + temp7 + args + " ) ");
         }
      }
      print_string = " TEMP " + temp11;
      return temp11;

      //return "";
   }

   /**
    * f0 -> Expression()
    * f1 -> ( ExpressionRest() )*
    */
   public String visit(ExpressionList n) {
      String _ret="";
      if(debug)
      System.out.println("ExpressionList");
      expression_append += " TEMP " + n.f0.accept(this);
      n.f1.accept(this);
      return "";
   }

   /**
    * f0 -> ","
    * f1 -> Expression()
    */
   public String visit(ExpressionRest n) {
      String _ret="";
      if(debug)
      System.out.println("ExpressionRest");
      expression_append += " TEMP " + n.f1.accept(this);
      return "";
   }

   /**
    * f0 -> IntegerLiteral()
    *       | TrueLiteral()
    *       | FalseLiteral()
    *       | Identifier()
    *       | ThisExpression()
    *       | ArrayAllocationExpression()
    *       | AllocationExpression()
    *       | NotExpression()
    *       | BracketExpression()
    */
   public String visit(PrimaryExpression n) {
      String _ret="";
      String return_result = n.f0.accept(this);
      return return_result;
   }

   /**
    * f0 -> <INTEGER_LITERAL>
    */
   public String visit(IntegerLiteral n) {
      String temp = getNewTemp();
      if(debug)
      System.out.println("IntegerLiteral");
      System.out.println("MOVE TEMP " + temp + " " + n.f0.tokenImage);
      return temp;
   }

   /**
    * f0 -> "true"
    */
   public String visit(TrueLiteral n) {
      String temp = getNewTemp();
      if(debug)
      System.out.println("TrueLiteral");
      System.out.println("MOVE TEMP " + temp + " " + "1");
      return temp;
   }

   /**
    * f0 -> "false"
    */
   public String visit(FalseLiteral n) {
      String temp = getNewTemp();
      if(debug)
      System.out.println("FalseLiteral");
      System.out.println("MOVE TEMP " + temp + " " + "0");
      return temp;
   }

   /**
    * f0 -> <IDENTIFIER>
    */
   public String visit(Identifier n) {
      if(build_class_hier){
         curr_type = n.f0.tokenImage;
         return "";
      }
      String _ret="";
      if(typelookup){
        curr_type = n.f0.tokenImage;
        return "";
      }
      else{
         Obj o = get_var(n.f0.tokenImage);
         curr_type = o.type;
         if(o.origin == "class"){
            heaped = true;
            print_string = "TEMP 0 " + Integer.toString((o.index+1)*4);
            if(indentifier_print){
               String temp7 = getNewTemp();
               if(debug)
               System.out.println("Identifier");
               System.out.println( "HLOAD TEMP " + temp7 + " " + print_string);
               return temp7;
            }
            return "0";
         }
         else{
            heaped = false;
            print_string = "TEMP " + o.index + " ";
            if(!update_itdentifier){
               String return_result = getNewTemp();
               if(debug)
               System.out.println("Identifier");
               System.out.println("MOVE TEMP " + return_result + " TEMP " + Integer.toString(o.index));
               return return_result;
            }
            return Integer.toString(o.index);
         }

      }
   }

   /**
    * f0 -> "this"
    */
   public String visit(ThisExpression n) {
         curr_type = curr_class;
         print_string="TEMP 0";
      return " 0 ";
   }

   /**
    * f0 -> "new"
    * f1 -> "int"
    * f2 -> "["
    * f3 -> Expression()
    * f4 -> "]"
    */
   public String visit(ArrayAllocationExpression n) {
      String _ret="";
      String array_size = getNewTemp();
      expression_print = true;
      String calculated_array_size = n.f3.accept(this);
      expression_print = false;
      if(debug)
      System.out.println("ArrayAllocationExpression");
      System.out.println( "MOVE TEMP " + array_size + " TEMP " + calculated_array_size);
      String array_size_plus1 = getNewTemp();
      System.out.println( "MOVE TEMP " + array_size_plus1 + " PLUS TEMP " + array_size + " 1");
      String array_size_times4 = getNewTemp();
      System.out.println( "MOVE TEMP " + array_size_times4 + " TIMES TEMP " + array_size_plus1 + " 4");
      String final_array_address = getNewTemp();
      System.out.println( "MOVE TEMP " + final_array_address + " HALLOCATE TEMP " + array_size_times4);
      String final_final_array_address = getNewTemp();
      System.out.println( "HSTORE TEMP " + final_array_address + " 0 " + " TEMP " + array_size);
      System.out.println( "MOVE TEMP " + final_final_array_address + " TEMP " + final_array_address);
      String temp6 = getNewTemp();
      System.out.println( "MOVE TEMP " + temp6 + " TIMES TEMP " + array_size + " 4");
      String temp7 = getNewTemp();
      //System.out.println( "MOVE TEMP " + temp7 + " PLUS TEMP " + final_array_address + " TEMP " + temp6);
      String temp8 = getNewTemp();
      System.out.println( "MOVE TEMP " + temp8 + " PLUS TEMP " + temp6 + " 4");
      String temp9 = getNewTemp();
      System.out.println( "MOVE TEMP " + temp7 + " 4 ");
      System.out.println( "MOVE TEMP " + temp9 + " LE TEMP " + temp7 + " TEMP " + temp8);
      String temp10 = getNewTemp();
      System.out.println( "MOVE TEMP " + temp10 + " 0");
      String label0 = getNewLabel();
      System.out.println( label0 + " NOOP");
      String label1 = getNewLabel();
      System.out.println( "CJUMP TEMP " + temp9 + " " + label1);
      String temp12 = getNewTemp();
      System.out.println( "MOVE TEMP " + temp12 + " PLUS TEMP " + final_final_array_address + " TEMP " + temp7);
      System.out.println( "HSTORE TEMP " + temp12 + " 0 " + "TEMP " + temp10);
      String temp13 = getNewTemp();
      System.out.println( "MOVE TEMP " + temp13 + " PLUS TEMP " + temp7 + " 4 ");
      System.out.println( "MOVE TEMP " + temp7 + " TEMP " + temp13);
      System.out.println( "MOVE TEMP " + temp9 + " LE TEMP " + temp7 + " TEMP " + temp8);
      System.out.println( "JUMP " + label0);
      System.out.println( label1 + " NOOP");
      String temp11 = getNewTemp();
      System.out.println( "MOVE TEMP " + temp11 + " TEMP " + final_array_address);
      String return_address = getNewTemp();
      return_address = temp11;
      //System.out.println( "MOVE TEMP " + return_address + " TEMP " + temp11);
      curr_type = "int[]";
      return return_address;
   }

   /**
    * f0 -> "new"
    * f1 -> Identifier()
    * f2 -> "("
    * f3 -> ")"
    */
   public String visit(AllocationExpression n) {
      String _ret="";
      int meth_num = class_methods.get(n.f1.f0.tokenImage).size();
      //int var_num = class_variables.get(n.f1.f0.tokenImage).size();
      int var_num = class_num_variables.get(n.f1.f0.tokenImage);
     boolean vars_present = false;
     from_allocation = true;
     String temp0 = getNewTemp();
     String temp0_temp = getNewTemp();
     String size_store = getNewTemp();
     //System.out.println("MOVE TEMP " + size_store + " " + Integer.toString(var_num*4 + 4));
     //System.out.println( "MOVE TEMP " + temp0_temp + " HALLOCATE " + "TEMP " + size_store);
     //System.out.println("MOVE TEMP " + temp0 + " TEMP " + temp0_temp);
     if(debug)
     System.out.println("AllocationExpression");
     System.out.println( "MOVE TEMP " + temp0 + " HALLOCATE " + Integer.toString(var_num*4 + 4));
     size_store = getNewTemp();
     String temp1 = getNewTemp();
     String temp1_temp = getNewTemp();
     //System.out.println("MOVE TEMP " + size_store + " " + Integer.toString(meth_num*4));
     //System.out.println( "MOVE TEMP " + temp1_temp + " HALLOCATE " + "TEMP " + size_store);
     //System.out.println("MOVE TEMP " + temp1 + " TEMP " + temp1_temp);
     System.out.println( "MOVE TEMP " + temp1 + " HALLOCATE " + Integer.toString(meth_num*4));
     int i = 0;
     for(String method_name : class_methods.get(n.f1.f0.tokenImage).keySet()){
      String print_class_name = n.f1.f0.tokenImage;
      for(Pair imported_pair : imported_class_methods.get(n.f1.f0.tokenImage)){
         if(imported_pair.getKey()==method_name){
            print_class_name=imported_pair.getValue();
            break;
         }

      }
      String temp = getNewTemp();
      System.out.println( "MOVE TEMP " + temp + " " + print_class_name+"_"+method_name);
      String index = Integer.toString(class_methods_index.get(n.f1.f0.tokenImage).get(method_name)*4);
      System.out.println( "HSTORE TEMP " + temp1 + " " + index + " TEMP " + temp);
      //class_methods_index.get(n.f1.f0.tokenImage).put(method_name,i);
      i++;
     }
     System.out.println( "HSTORE TEMP " + temp0 + " " + " 0 TEMP " + temp1);
     // if(var_num>0)
     //    System.out.println( "MOVE TEMP " + temp4 + " 0");
     //String temp4 = getNewTemp();
     //System.out.println( "MOVE TEMP " + temp4 + " 0");
     for(int j=0;j<var_num;j++){
      String temp4 = getNewTemp();
      System.out.println( "MOVE TEMP " + temp4 + " 0");
      System.out.println( "HSTORE TEMP " + temp0 + " " + Integer.toString((j+1)*4) + " " + "TEMP " + temp4);
      vars_present = true;
     }
     //System.out.println( "HSTORE TEMP " + temp0 + " " + " 0 TEMP " + temp1);
     String temp5 = getNewTemp();
     if(vars_present){
        System.out.println( "MOVE TEMP " + temp5 + " TEMP " + temp0);
      }
      else{
         System.out.println( "MOVE TEMP " + temp5 + " TEMP " + temp0);
      }
      String final_return = getNewTemp();
      final_return = temp5;
      //System.out.println("MOVE TEMP " + final_return + " TEMP " + temp5);

     curr_type = n.f1.f0.tokenImage;
     print_string = " TEMP " + final_return + " ";
     from_allocation=false;
      return final_return;
   }

   /**
    * f0 -> "!"
    * f1 -> Expression()
    */
   public String visit(NotExpression n) {
      String _ret="";
      String result1 = getNewTemp();
      if(debug)
      System.out.println("NotExpression");
      expression_print = true;
      String expression_result = n.f1.accept(this);
      expression_print = false;
      System.out.println( "MOVE TEMP " + result1 + " TEMP " + expression_result);
      String result2 = getNewTemp();
      System.out.println( "MOVE TEMP " + result2 + " MINUS " + " 1 " + " TEMP " + result1);
      return result2;
   }

   /**
    * f0 -> "("
    * f1 -> Expression()
    * f2 -> ")"
    */
   public String visit(BracketExpression n) {
      String _ret="";
      return n.f1.accept(this);
   }

   /**
    * f0 -> Identifier()
    * f1 -> ( IdentifierRest() )*
    */
   public String visit(IdentifierList n) {
      String _ret="";
      n.f0.accept(this);
      System.out.print(" ");
      n.f1.accept(this);
      return "";
   }

   /**
    * f0 -> ","
    * f1 -> Identifier()
    */
   public String visit(IdentifierRest n) {
      String _ret="";
      System.out.print(" ");
      n.f1.accept(this);
      return "";
   }

}
