package dbResource;
import java.util.*;
import java.sql.*;

//Static Mapping of the columns that has to be temporalized
public class Static_Temporalize {
	java.sql.PreparedStatement statement=null;
    ResultSet resultSet;
	Map<String, Map<String, String>> temporalize;
	public static ArrayList<String> hist_tables;
	
	Static_Temporalize(Map<String, Map<String, String>> temp){
		temporalize = temp;
		Static_Temporalize.hist_tables = new ArrayList<String>();
	}
	
	public static void Execute(Map<String, Map<String, String>> t) {
		Static_Temporalize temp = new Static_Temporalize(t);
		
		for(Map.Entry<String, Map<String,String> > entry:temp.temporalize.entrySet()) {
			String key = entry.getKey();
			Map<String, String> value = entry.getValue();
			Map<String,String> PK_and_type = DatabaseConnection.getInstance().getPrimaryKey(key);
			String new_hist_table = temp.create_hist_table(key,PK_and_type,value);
			if(new_hist_table!=null)	Static_Temporalize.hist_tables.add(key);
		}
	}
	
	public String create_hist_table(String table, Map<String,String> pk, Map<String, String> col) {
		String hist_table = "hist_" + table;
		
		if(DatabaseConnection.getInstance().create_table(table,hist_table,pk,col).equals("success")) {
			DatabaseConnection.getInstance().add_Triggers(table,hist_table,pk,col);
			return hist_table;
		}
		
    	return null;
	}	
}
