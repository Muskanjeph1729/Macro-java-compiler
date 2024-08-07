package visitor;

import syntaxtree.*;
import java.util.*;

import java.util.Stack;



public class second_iteration extends GJNoArguDepthFirst < String > {
   public info_class stored_data = new info_class();

   //
   // User-generated visitor methods below
   //

   /**
    * f0 -> MainClass()
    * f1 -> ( TypeDeclaration() )*
    * f2 -> <EOF>
    */
   public String visit(Goal n) {
      stored_data.add_scope("global");
      boolean break_loop=false;

      for (String current_class: stored_data.parent_variable.keySet()) {

         String temp = new String(current_class);
         ArrayList < String > temp_stack = new ArrayList < String > ();
         temp_stack.add(temp);

         while (true) {
            if (stored_data.parent_variable.containsKey(temp))
               temp = stored_data.parent_variable.get(temp);
            else{
               break_loop = true;
               break;
            }

            if (temp_stack.contains(temp))
               stored_data.call_error();
         }
         if(break_loop)
            break;
      }
      String _ret = "";
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);

      stored_data.end_scope();
      return _ret;
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
      String _ret = "";
      stored_data.add_scope(n.f1.f0.tokenImage);
      stored_data.add_scope("main()");
      n.f0.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      n.f6.accept(this);
      n.f7.accept(this);
      n.f8.accept(this);
      n.f9.accept(this);
      n.f10.accept(this);
      n.f12.accept(this);
      n.f13.accept(this);
      n.f14.accept(this);
      n.f15.accept(this);
      n.f16.accept(this);
      stored_data.end_scope();
      stored_data.end_scope();
      return _ret;
   }

   /**
    * f0 -> ClassDeclaration()
    *       | ClassExtendsDeclaration()
    */
   public String visit(TypeDeclaration n) {
     String _ret = "";
      n.f0.accept(this);
      return _ret;
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
      stored_data.add_scope(n.f1.f0.tokenImage);
      String _ret = "";
      n.f0.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      stored_data.end_scope();

      return _ret;
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
      stored_data.add_scope(n.f1.f0.tokenImage);
      if(!stored_data.iterate_list(stored_data.variable_in_scope.get(":global"),n.f3.f0.tokenImage))
         stored_data.call_error();
      String _ret = "";
      n.f0.accept(this);
      n.f2.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      n.f6.accept(this);
      n.f7.accept(this);
      stored_data.end_scope();
      return _ret;
   }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    * f2 -> ";"
    */
   public String visit(VarDeclaration n) {
      String _ret = "";
      n.f0.accept(this);
      n.f2.accept(this);
      return _ret;
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
      String _ret = "";
      n.f0.accept(this);
      String type = n.f1.accept(this); 
      if(stored_data.parent_variable.containsKey(stored_data.current_scope.get(1))){
         String parent_class = stored_data.parent_variable.get(stored_data.current_scope.get(1));

         if( stored_data.type_class_Scope(n.f2.f0.tokenImage+"()", ":global:" + parent_class,false) != ""){
            if(stored_data.type_class_Scope(n.f2.f0.tokenImage+"()", ":global:" + parent_class,false) != type)
               stored_data.call_error();

            String function_class = stored_data.type_class_Scope(n.f2.f0.tokenImage+"()", ":global:" + parent_class,true);
            String function_params =  stored_data.function_parameters.get(function_class + ":" + n.f2.f0.tokenImage+"()");

            if(function_params != stored_data.function_parameters.get(stored_data.scope_string + ":" + n.f2.f0.tokenImage+"()"))
               stored_data.call_error();

         }
      }
      stored_data.add_scope(n.f2.f0.tokenImage+"()");
      n.f3.accept(this);
      n.f4.accept(this);
      stored_data.list = new String();
      stored_data.list = "";
      n.f5.accept(this);
      n.f6.accept(this);
      n.f7.accept(this);
      n.f8.accept(this);
      n.f9.accept(this);
      String temp = n.f10.accept(this);
      if(!stored_data.isequal(type,temp))
         stored_data.call_error();
      n.f11.accept(this);
      n.f12.accept(this);
      stored_data.end_scope();
      return _ret;
   }

   /**
    * f0 -> FormalParameter()
    * f1 -> ( FormalParameterRest() )*
    */
   public String visit(FormalParameterList n) {
      String _ret = "";
      n.f0.accept(this);
      n.f1.accept(this);
      return _ret;
   }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    */
   public String visit(FormalParameter n) {
      String _ret = "";
      String type = n.f0.accept(this);     
      stored_data.list = stored_data.list + type;
      return _ret;
   }

   /**
    * f0 -> ","
    * f1 -> FormalParameter()
    */
   public String visit(FormalParameterRest n) {
      String _ret = "";
      n.f0.accept(this);
      stored_data.list = stored_data.list + ",";
      _ret = n.f1.accept(this);
      return _ret;
   }

   /**
    * f0 -> ArrayType()
    *       | BooleanType()
    *       | IntegerType()
    *       | Identifier()
    */
   public String visit(Type n) {
      String _ret = "";
      stored_data.check_id = 1;
      _ret = n.f0.accept(this);
      stored_data.check_id = 0;
      return _ret;
   }

   /**
    * f0 -> "int"
    * f1 -> "["
    * f2 -> "]"
    */
   public String visit(ArrayType n) {
      String _ret = "int[]";
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      return _ret;
   }

   /**
    * f0 -> "boolean"
    */
   public String visit(BooleanType n) {
      String _ret = "boolean";
      n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> "int"
    */
   public String visit(IntegerType n) {
      String _ret = "int";
      n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> Block()
    *       | AssignmentStatement()
    *       | ArrayAssignmentStatement()
    *       | IfStatement()
    *       | WhileStatement()
    *       | PrintStatement()
    */
   public String visit(Statement n) {
      String _ret = "";
      n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> "{"
    * f1 -> ( Statement() )*
    * f2 -> "}"
    */
   public String visit(Block n) {
      String _ret = "";
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      return _ret;
   }

   /**
    * f0 -> Identifier()
    * f1 -> "="
    * f2 -> Expression()
    * f3 -> ";"
    */
   public String visit(AssignmentStatement n) {
      String _ret = "";
      String vari = n.f0.accept(this);
      n.f1.accept(this);
      String val = n.f2.accept(this);
      n.f3.accept(this);
      if(!stored_data.isequal(vari,val))
         stored_data.call_error();
      return _ret;
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
      String _ret = "";
      String vari = n.f0.accept(this);
      n.f1.accept(this);
      String index = n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      String val = n.f5.accept(this);
      n.f6.accept(this);
      if(vari!= "int[]" || val!="int")
         stored_data.call_error();
      if(index!="int")
         stored_data.call_error();

      return _ret;
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
   public String visit(IfStatement n) {
      String _ret="";
      n.f0.accept(this);
      n.f1.accept(this);
      String temp = n.f2.accept(this);
      if(temp != "boolean")
         stored_data.call_error();
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      n.f6.accept(this);
      return _ret;
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
      n.f0.accept(this);
      n.f1.accept(this);
      String temp = n.f2.accept(this);
      if(temp != "boolean")
         stored_data.call_error();
      n.f3.accept(this);
      n.f4.accept(this);
      return _ret;
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
      n.f0.accept(this);
      n.f1.accept(this);
      String temp = n.f2.accept(this);
      if(temp != "int")
         stored_data.call_error();
      n.f3.accept(this);
      n.f4.accept(this);
      return _ret;
   }

   /**
    * f0 -> AndExpression()
    *       | CompareExpression()
    *       | PlusExpression()
    *       | MinusExpression()
    *       | TimesExpression()
    *       | ArrayLookup()
    *       | ArrayLength()
    *       | MessageSend()
    *       | PrimaryExpression()
    */
   public String visit(Expression n) {
      String _ret="";
      _ret = n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "&"
    * f2 -> PrimaryExpression()
    */
   public String visit(AndExpression n) {
      String _ret="";
      String l = n.f0.accept(this);
      n.f1.accept(this);
      String r = n.f2.accept(this);
      if(l != "boolean" || r!= "boolean")
         stored_data.call_error();
      _ret = "boolean";
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "<"
    * f2 -> PrimaryExpression()
    */
   public String visit(CompareExpression n) {
      String _ret="";
      String l = n.f0.accept(this);
      n.f1.accept(this);
      String r = n.f2.accept(this);
      if(l != "int" || r!= "int")
         stored_data.call_error();
      _ret = "boolean";
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "+"
    * f2 -> PrimaryExpression()
    */
   public String visit(PlusExpression n) {
      String _ret="";
      String l = n.f0.accept(this);
      n.f1.accept(this);
      String r = n.f2.accept(this);
      
      if(l != "int" || r != "int")
         stored_data.call_error();
      _ret = "int";        
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "-"
    * f2 -> PrimaryExpression()
    */
   public String visit(MinusExpression n) {
      String _ret="";
      String l = n.f0.accept(this);
      n.f1.accept(this);
      String r = n.f2.accept(this);
      
      if(l != "int" || r != "int")
         stored_data.call_error();
      _ret = "int";        
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "*"
    * f2 -> PrimaryExpression()
    */
   public String visit(TimesExpression n) {
      String _ret="";
      String l = n.f0.accept(this);
      n.f1.accept(this);
      String r = n.f2.accept(this);
      
      if(l != "int" || r != "int")
         stored_data.call_error();
      _ret = "int";        
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "["
    * f2 -> PrimaryExpression()
    * f3 -> "]"
    */
   public String visit(ArrayLookup n) {
      String _ret="";
      String variable_type = n.f0.accept(this);
      n.f1.accept(this);
      String index = n.f2.accept(this);
      if(variable_type == "int[]")
         _ret="int";
         else
            stored_data.call_error();

      if(index != "int")
         stored_data.call_error();
      n.f3.accept(this);
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> "length"
    */
   public String visit(ArrayLength n) {
      String _ret="";
      String temp = n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      if(temp!="int[]")
         stored_data.call_error();
      _ret = "int";
      return _ret;
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
      String class_name = n.f0.accept(this);
      if(stored_data.iterate_list(stored_data.variable_in_scope.get(":global"),class_name) == false)
         stored_data.variable_error();
      _ret = stored_data.type_class_Scope(n.f2.f0.tokenImage+"()",":global:"+class_name,false);
      if(_ret == "")
         stored_data.call_error();
      n.f1.accept(this);
      n.f3.accept(this);
      String expression_type = n.f4.accept(this);
      String real_class_name = stored_data.type_class_Scope(n.f2.f0.tokenImage+"()",":global:"+class_name,true);
      if(real_class_name=="")
         stored_data.call_error();
      if(expression_type== null)
         expression_type = "";

      String[] function_params = stored_data.function_parameters.get(real_class_name+":" + n.f2.f0.tokenImage+"()").split(",");
      String[] expression_params = expression_type.split(",");

      if(function_params.length != expression_params.length)
         stored_data.call_error();

      for(int i=0;i<function_params.length; i++){
         if(!stored_data.isequal(function_params[i],expression_params[i])){
            stored_data.call_error();
         }
      }
      n.f5.accept(this);
      return _ret;
   }

   /**
    * f0 -> Expression()
    * f1 -> ( ExpressionRest() )*
    */
   public String visit(ExpressionList n) {
      String _ret="";
      String temp = new String();
      temp = n.f0.accept(this);
      stored_data.list = stored_data.list + temp;
      n.f1.accept(this);
      _ret = stored_data.list;
      stored_data.list = new String();
      stored_data.list = "";
      return _ret;
   }

   /**
    * f0 -> ","
    * f1 -> Expression()
    */
   public String visit(ExpressionRest n) {
      String _ret="";
      n.f0.accept(this);
      stored_data.list = stored_data.list + ",";
      stored_data.list = stored_data.list + n.f1.accept(this);
      return _ret;
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
      _ret = n.f0.accept(this);
      //System.out.println(_ret);
      return _ret;
   }

   /**
    * f0 -> <INTEGER_LITERAL>
    */
   public String visit(IntegerLiteral n) {
      String _ret="int";
      n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> "true"
    */
   public String visit(TrueLiteral n) {
      String _ret="boolean";
      n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> "false"
    */
   public String visit(FalseLiteral n) {
      String _ret="boolean";
      n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> <IDENTIFIER>
    */
   public String visit(Identifier n) {
      String _ret="";
      if(stored_data.check_id == 1){
         if(!stored_data.iterate_list(stored_data.variable_in_scope.get(":global"),n.f0.tokenImage))
            stored_data.call_error();
         if(stored_data.main_class_name == n.f0.tokenImage)
            stored_data.call_error();
         return n.f0.tokenImage;
      }

      n.f0.accept(this);
      _ret = stored_data.getType(n.f0.tokenImage);
      if(_ret == "")
         stored_data.call_error();
      return _ret;
   }

   /**
    * f0 -> "this"
    */
   public String visit(ThisExpression n) {
      String _ret="";
      _ret = stored_data.current_scope.get(1);
      n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> "new"
    * f1 -> "int"
    * f2 -> "["
    * f3 -> Expression()
    * f4 -> "]"
    */
   public String visit(ArrayAllocationExpression n) {
      String _ret="int[]";
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      String temp = n.f3.accept(this);
      if (temp != "int")
         stored_data.call_error();
      n.f4.accept(this);
      return _ret;
   }

   /**
    * f0 -> "new"
    * f1 -> Identifier()
    * f2 -> "("
    * f3 -> ")"
    */
   public String visit(AllocationExpression n) {
      String _ret="";
      _ret = n.f1.f0.tokenImage;
      n.f0.accept(this);
      String temp = n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      return _ret;
   }

   /**
    * f0 -> "!"
    * f1 -> Expression()
    */
   public String visit(NotExpression n) {
      String _ret="";
      n.f0.accept(this);
      _ret = n.f1.accept(this);
      if(_ret != "boolean")
         stored_data.call_error();
      return _ret;
   }

   /**
    * f0 -> "("
    * f1 -> Expression()
    * f2 -> ")"
    */
   public String visit(BracketExpression n) {
      String _ret="";
      n.f0.accept(this);
      _ret = n.f1.accept(this);
      n.f2.accept(this);
      return _ret;
   }
}