

package visitor;
import java.util.*;

class info_class{
	Stack < String > current_scope = new Stack < > ();
  	HashMap < String,ArrayList < AbstractMap.SimpleEntry < String,String >>> variable_in_scope = new HashMap < String,ArrayList < AbstractMap.SimpleEntry < String,String >>> ();
  	HashMap < String,String > parent_variable = new HashMap < String,String > ();
  	HashMap < String,String > function_parameters = new HashMap < String,String > ();

	String scope_string = new String();
	String main_class_name = new String();
	int check_id = 0;
	String list = new String();

	
	public void call_error() {
	  System.out.println("Type Error");
	  System.exit(0);
	}

	public void variable_error(){
	  System.out.println("Symbol not found");
	  System.exit(0);
	}


	public void add_scope_to_string(String scope_name) {
	  scope_string = scope_string + ":" + scope_name;
	}

	public String get_scope(int level) {
	  int count = current_scope.size();
	  String desired_scope = "";
	  Iterator iterator = current_scope.iterator();

	  while (count != level && iterator.hasNext()) {
	     desired_scope = desired_scope + ":" + iterator.next();
	     count = count - 1;
	  }
	  return desired_scope;
	}

	public void add_scope(String scope_name) {
	  current_scope.push(scope_name);
	  add_scope_to_string(scope_name);
	  if (variable_in_scope.containsKey(scope_string) == false) {
	     ArrayList < AbstractMap.SimpleEntry < String, String >> temp = new ArrayList < AbstractMap.SimpleEntry < String, String >> ();
	     variable_in_scope.put(scope_string, temp);
	  }
	}

	public String end_scope() {
	  String removed_scope = current_scope.pop();
	  scope_string = scope_string.substring(0,scope_string.lastIndexOf(":"));
	  return removed_scope;
	}

	public boolean iterate_list(ArrayList < AbstractMap.SimpleEntry < String, String >> list_name, String variable_name) {
	  if(list_name == null)
	     variable_error();

	  for (AbstractMap.SimpleEntry < String, String > p: list_name) {
	     if (p.getValue().equals(variable_name)){
	        return true;
	     }
	  }

	  return false;
	}

	public int iterate_list_index(ArrayList < AbstractMap.SimpleEntry < String, String >> list_name, String variable_name) {
	  int count = 0;
	  if(list_name == null)
	     variable_error();

	  for (AbstractMap.SimpleEntry < String, String > p: list_name) {
	     if (p.getValue().equals(variable_name))
	        return count;
	     count++;
	  }

	  return -1;
	}

	public void add_variable(String type, String name) {
	  AbstractMap.SimpleEntry < String, String > variable = new AbstractMap.SimpleEntry < String, String > (type, name);

	  if (iterate_list(variable_in_scope.get(scope_string), name))
	     call_error();

	  variable_in_scope.get(scope_string).add(variable);
	}

	public String type_class_Scope(String variable_name, String scope_name, boolean number){
	  while (true) {
	     ArrayList < AbstractMap.SimpleEntry < String, String >> temp_list = variable_in_scope.get(scope_name);
	     if (iterate_list(temp_list, variable_name)){
	        if(number == false)
	           return temp_list.get(iterate_list_index(temp_list, variable_name)).getKey();
	        else
	           return scope_name;
	     }

	     String temp_scope_name = scope_name.substring(scope_name.lastIndexOf(':') + 1);

	     if (parent_variable.containsKey(temp_scope_name) == false)
	        return "";

	     scope_name = ":global:" + parent_variable.get(temp_scope_name);
	  }
	}

	public String getType(String id){
	  for(int i = 0; i<=current_scope.size(); i++){
	     String curr_scope = get_scope(i);
	     String temp = type_class_Scope(id, curr_scope, false);
	     if(temp!="")
	        return temp;
	  }
	   call_error();
	  return "";
	}

	public boolean isequal(String parent_class, String child_class) {
	  while (true) {
	     if (parent_class.equals(child_class))
	        return true;

	     if (parent_variable.containsKey(child_class))
	        child_class = parent_variable.get(child_class);
	     else
	        return false;
	  }
	}
}