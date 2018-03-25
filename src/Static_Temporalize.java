import java.util.*;
import java.sql.*;

//Static Mapping of the columns that has to be temporalized
public class Static_Temporalize {
	String username,password,schema_name;
	java.sql.PreparedStatement statement=null;
    ResultSet resultSet;
    DatabaseConnection dbCon;
	Map<String, Map<String, String>> temporalize;
	ArrayList<String> hist_tables;
	
	Static_Temporalize(String user, String pwd, String schema, Map<String, Map<String, String>> temp){
		username = user;
		password = pwd;
		schema_name = schema;
		temporalize = temp;
		hist_tables = new ArrayList<String>();
	}
	
	public static void Execute(String user, String pwd, String schema, Map<String, Map<String, String>> t) {
		Static_Temporalize temp = new Static_Temporalize(user, pwd, schema, t);
		temp.dbCon = new DatabaseConnection(user,pwd,schema);
		
		for(Map.Entry<String, Map<String,String> > entry:temp.temporalize.entrySet()) {
			String key = entry.getKey();
			Map<String, String> value = entry.getValue();
			ArrayList<String> PK_and_type = temp.dbCon.getPrimaryKey(key);
			String new_hist_table = temp.create_hist_table(key,PK_and_type,value);
			if(new_hist_table!=null)	temp.hist_tables.add(new_hist_table);
		}
	}
	
	public String create_hist_table(String table, ArrayList<String> pk, Map<String, String> col) {
		String hist_table = "hist_" + table;
		String query = "CREATE TABLE IF NOT EXISTS " + hist_table + "(" + pk.get(0) + " " + pk.get(1);
		for(Map.Entry<String,String> entry:col.entrySet()) {
			query += "," + entry.getKey() + " " + entry.getValue();
		}
		query += ", START_DATE DATETIME, END_DATE DATETIME, PRIMARY KEY(" + pk.get(0);
		
		
		for(Map.Entry<String,String> entry:col.entrySet()) {
			query += "," + entry.getKey();
		}
		
		query += ", START_DATE));";
		
		//System.out.println(query);
		
		if(dbCon.create_table(query).equals("success")) {
			dbCon.add_FK_constraint(table,hist_table,pk.get(0));
			return hist_table;
		}
		
    	return null;
	}	
}
