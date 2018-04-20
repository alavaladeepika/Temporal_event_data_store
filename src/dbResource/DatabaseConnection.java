package dbResource;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.*;

import initGUI.WelcomeFrame;

//For opening the Database Connection
public class DatabaseConnection{
	private static DatabaseConnection instance;
	
	java.sql.PreparedStatement statement=null;
    ResultSet resultSet;
    Connection connection = null;

    public static String schema;
    public static String username;
    public static String password;

    private DatabaseConnection(){
    	
        connection = null;

        try{
            Class.forName("com.mysql.jdbc.Driver");
            //System.out.println("Driver Found");
        }

        catch (ClassNotFoundException e){
            //System.out.println("Driver Not Found: " + e);
        	WelcomeFrame.showAlertMessage("Driver not Found!");
        	return;
        } 

        String url = "jdbc:mysql://127.0.0.1:3306/"+schema+"?useSSL=true";
        try
        {
            connection = (Connection)DriverManager.getConnection(url, username, password);
            //System.out.println("Successfully Connected to Database");
            WelcomeFrame.success = true;
            
        }
        catch(SQLException e)
        {
            //System.out.println("SQL Exception: " + e); 
        	WelcomeFrame.showAlertMessage("Wrong Credentials! Please try again.");
        }                   

    }
    
    public static DatabaseConnection getInstance() {
    	return instance;
    }
    
    public static DatabaseConnection getInstance(String user, String pwd, String schema_name) {
    	username = user;
    	password = pwd;
    	schema = schema_name;
    	instance = new DatabaseConnection();
    	return instance;
    }
    
    public ArrayList<String> getTables() {
    	ArrayList<String> tableNames = new ArrayList<String>();
    	String query = "select TABLE_NAME FROM information_schema.TABLES where TABLE_TYPE = 'BASE TABLE' and TABLE_SCHEMA = ?";
		
    	try {
    		statement = connection.prepareStatement(query);
    		statement.setString(1,schema);
    		resultSet = statement.executeQuery();
    		
    		while(resultSet.next()) {
        		tableNames.add(resultSet.getString("TABLE_NAME"));
        	}
    	}
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	
    	return tableNames;
    }
    
    public Map<String,String> getColumns(String tableName) {
    	Map<String,String> ColumnNames = new HashMap<String,String>();
    	String query = "select COLUMN_NAME, COLUMN_TYPE FROM information_schema.COLUMNS where TABLE_SCHEMA = ? "
    			+ "and TABLE_NAME = ? and COLUMN_KEY!='PRI'";
		
    	try {
    		statement = connection.prepareStatement(query);
    		statement.setString(1,schema);
    		statement.setString(2,tableName);
    		resultSet = statement.executeQuery();
    		
    		while(resultSet.next()) {
    			String key = resultSet.getString("COLUMN_NAME"); 
    			String value = resultSet.getString("COLUMN_TYPE");
    			
    			ColumnNames.put(key,value);
        	}
    	}
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	
    	return ColumnNames;
    }
    
    public Map<String,String> getTempColumns(String tableName) {
    	Map<String,String> ColumnNames = new HashMap<String,String>();
    	String query = "select COLUMN_NAME, COLUMN_TYPE FROM information_schema.COLUMNS where TABLE_SCHEMA = ? "
    			+ "and TABLE_NAME = ? and (COLUMN_NAME!='START_DATE' AND COLUMN_NAME!='END_DATE')";
		
    	try {
    		statement = connection.prepareStatement(query);
    		statement.setString(1,schema);
    		statement.setString(2,"hist_" + tableName);
    		resultSet = statement.executeQuery();
    		
    		while(resultSet.next()) {
    			String key = resultSet.getString("COLUMN_NAME"); 
    			String value = resultSet.getString("COLUMN_TYPE");
    			
    			ColumnNames.put(key,value);
        	}
    	}
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	
    	return ColumnNames;
    }
    
    public Map<String,String> getJoinColumns(String tableName) {
    	Map<String,String> ColumnNames = new HashMap<String,String>();
    	String query = "select COLUMN_NAME, COLUMN_TYPE FROM information_schema.COLUMNS where TABLE_SCHEMA = ? "
    			+ "and TABLE_NAME = ? and (COLUMN_NAME!='START_DATE' AND COLUMN_NAME!='END_DATE')";
		
    	try {
    		statement = connection.prepareStatement(query);
    		statement.setString(1,schema);
    		statement.setString(2,tableName);
    		resultSet = statement.executeQuery();
    		
    		while(resultSet.next()) {
    			String key = resultSet.getString("COLUMN_NAME"); 
    			String value = resultSet.getString("COLUMN_TYPE");
    			
    			ColumnNames.put(key,value);
        	}
    	}
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	
    	return ColumnNames;
    }
    
    public Map<String,String> getPrimaryKey(String table) {
    	String query = "select COLUMN_NAME, COLUMN_TYPE FROM information_schema.COLUMNS where TABLE_SCHEMA = ? "
    			+ "and TABLE_NAME = ? and COLUMN_KEY = 'PRI'";
		Map<String,String> pk_and_type = new HashMap<String,String>();
    	
    	try {
    		statement = connection.prepareStatement(query);
    		statement.setString(1,schema);
    		statement.setString(2,table);
    		resultSet = statement.executeQuery();
    		
    		while(resultSet.next()) {
    			pk_and_type.put(resultSet.getString("COLUMN_NAME"),resultSet.getString("COLUMN_TYPE"));
        	}
    	}
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	return pk_and_type;
    }
    
    public void copy_data(String table,String hist_table,Map<String,String> pk,Map<String, String> col) {
		String query = "INSERT INTO " + hist_table + "(";
		int i=0;
    	for(Map.Entry<String,String> entry:pk.entrySet()) {
    		if(i==0) {
    			query += entry.getKey();
    			i=1;
    		}
    		else {
    			query += "," + entry.getKey();
    		}
    	}
		for(Map.Entry<String,String> entry:col.entrySet()) {
			query += "," + entry.getKey();
		}
		query += ") SELECT ";
		i=0;
    	for(Map.Entry<String,String> entry:pk.entrySet()) {
    		if(i==0) {
    			query += entry.getKey();
    			i=1;
    		}
    		else {
    			query += "," + entry.getKey();
    		}
    	}
		for(Map.Entry<String,String> entry:col.entrySet()) {
				query += "," + entry.getKey();
		}
		query += " FROM "+ table;
		//System.out.println(query);
		
		try {
			statement = connection.prepareStatement(query);
			statement.executeUpdate();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
    }
    
    public String create_table(String table,String hist_table,Map<String,String> pk,Map<String, String> col) {
    	String drop_query = "drop table IF EXISTS "+hist_table ;
    	String query = "CREATE TABLE " + hist_table + "(" ;
    	int i=0;
    	for(Map.Entry<String,String> entry:pk.entrySet()) {
    		if(i==0) {
    			query += entry.getKey() + " " + entry.getValue();
    			i=1;
    		}
    		else {
    			query += "," + entry.getKey() + " " + entry.getValue();
    		}
    	}
    	if(i==1) query += ",";
		for(Map.Entry<String,String> entry:col.entrySet()) {
			if(i==1) {
				query += entry.getKey() + " " + entry.getValue();
				i=0;
			}
			else query += "," + entry.getKey() + " " + entry.getValue();
		}
		query += ", START_DATE DATETIME DEFAULT NOW(), END_DATE DATETIME, UNIQUE(";
		i=0;
    	for(Map.Entry<String,String> entry:pk.entrySet()) {
    		if(i==0) {
    			query += entry.getKey();
    			i=1;
    		}
    		else {
    			query += "," + entry.getKey();
    		}
    	}
    	if(i==1) query += ",";
    	for(Map.Entry<String,String> entry:col.entrySet()) {
    		if(i==1) {
				query += entry.getKey();
				i=0;
			}
			else query += "," + entry.getKey();
		}
		query += ",START_DATE))";
		//System.out.println(query);
    	try {
    		statement = connection.prepareStatement(drop_query);
    		statement.execute();
    		
    		statement = connection.prepareStatement(query);
    		statement.execute();
    		copy_data(table,hist_table,pk,col);
    		return "success";
    	}
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	return "failure";
    }
    
    public String onInsert_Trigger(String table,String hist_table,Map<String,String> pk,Map<String,String> col) {
    	String drop_query = "drop trigger IF EXISTS after_"+table+"_insert" ;
    	String query = "create trigger after_" + table + "_insert after insert on " +
    			table + " for each row begin insert into " + hist_table + " set " ;
    	int i=0;
    	for(Map.Entry<String,String> entry:pk.entrySet()) {
    		if(i==0) {
    			query += entry.getKey() + "= NEW." + entry.getKey();
    			i=1;
    		}
    		else {
    			query += "," + entry.getKey() + "= NEW." + entry.getKey();
    		}
    	}
    	for(Map.Entry<String,String> entry:col.entrySet()) {
    		query += "," + entry.getKey() + "= NEW." + entry.getKey();
    	}
    	query += "; END";
    	//System.out.println(query);
    	try {
    		statement = connection.prepareStatement(drop_query);
    		statement.execute();
    		
    		statement = connection.prepareStatement(query);
    		statement.execute();
    	}
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	return query;
    }
    
    public String onUpdate_Trigger(String table,String hist_table,Map<String,String> pk,Map<String,String> col) {
    	String drop_query = "drop trigger IF EXISTS after_"+table+"_update" ;
    	String query = "create trigger after_" + table + "_update after update on " +
    			table + " for each row begin update "+ hist_table + " set END_DATE = NOW() where "; 
    	int i=0;
    	for(Map.Entry<String,String> entry:pk.entrySet()) {
    		if(i==0) {
    			query += entry.getKey() + "= NEW." + entry.getKey();
    			i=1;
    		}
    		else {
    			query += " AND " + entry.getKey() + "= NEW." + entry.getKey();
    		}
    	}
    	query += " AND END_DATE is NULL; insert into " + hist_table + " set ";
    	i=0;
    	for(Map.Entry<String,String> entry:pk.entrySet()) {
    		if(i==0) {
    			query += entry.getKey() + "= NEW." + entry.getKey();
    			i=1;
    		}
    		else {
    			query += "," + entry.getKey() + "= NEW." + entry.getKey();
    		}
    	}
    	for(Map.Entry<String,String> entry:col.entrySet()) {
    		query += "," + entry.getKey() + "= NEW." + entry.getKey();
    	}
    	query += "; END";
    	
    	//System.out.println(query);
    	try {
    		statement = connection.prepareStatement(drop_query);
    		statement.execute();
    		
    		statement = connection.prepareStatement(query);
    		statement.execute();
    	}
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	return query;
    }

	public String onDelete_Trigger(String table,String hist_table,Map<String,String> pk,Map<String,String> col) {
		String drop_query = "drop trigger IF EXISTS after_"+table+"_delete";
		String query = "create trigger after_" + table + "_delete after delete on " +
    			table + " for each row begin update " + hist_table + " set END_DATE = NOW()"
    					+ " where ";
    	int i=0;
    	for(Map.Entry<String,String> entry:pk.entrySet()) {
    		if(i==0) {
    			query += entry.getKey() + "= OLD." + entry.getKey();
    			i=1;
    		}
    		else {
    			query += " AND " + entry.getKey() + "= OLD." + entry.getKey();
    		}
    	}
    	query += " AND END_DATE is NULL; END";
    	//System.out.println(query);
    	try {
    		statement = connection.prepareStatement(drop_query);
    		statement.execute();
    		
    		statement = connection.prepareStatement(query);
    		statement.execute();
    	}
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	return query;
	}
	
	public String uniqueness_beforeInsert_Trigger(String table,String hist_table,Map<String,String> pk) {
    	String drop_query = "drop trigger IF EXISTS before_"+hist_table+"_insert";
    	String query = "create trigger before_"+hist_table + "_insert before insert on hist_" +
    			table + " for each row begin IF(EXISTS(SELECT * FROM hist_"+table+" where ";
    	int i=0;
    	for(Map.Entry<String,String> entry:pk.entrySet()) {
    		if(i==0) {
    			query += entry.getKey() + "= NEW." + entry.getKey();
    			i=1;
    		}
    		else {
    			query += " AND " + entry.getKey() + "= NEW." + entry.getKey();
    		}
    	}
    	query += " AND (START_DATE < new.END_DATE OR new.END_DATE is NULL) AND (END_DATE > new.START_DATE OR END_DATE IS NULL))) THEN"
    			+ " SIGNAL SQLSTATE VALUE '45000' SET MESSAGE_TEXT = 'INSERT failed due to overlap of dates'; END IF; END";
    	//System.out.println(query);
    	try {
    		statement = connection.prepareStatement(drop_query);
    		statement.execute();
    		
    		statement = connection.prepareStatement(query);
    		statement.execute();
    	}
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	return query;
    }
	
	public String uniqueness_beforeUpdate_Trigger(String table,String hist_table,Map<String,String> pk) {
    	String drop_query = "drop trigger  IF EXISTS before_"+hist_table+"_update" ;
    	String query = "create trigger before_" + hist_table + "_update before update on hist_" +
    			table + " for each row begin IF(EXISTS(SELECT * FROM hist_"+table+" where ";
    	int i=0;
    	for(Map.Entry<String,String> entry:pk.entrySet()) {
    		if(i==0) {
    			query += entry.getKey() + "= NEW." + entry.getKey();
    			i=1;
    		}
    		else {
    			query += " AND " + entry.getKey() + "= NEW." + entry.getKey();
    		}
    	}
    	query += " AND (START_DATE < new.END_DATE OR new.END_DATE is NULL) AND (END_DATE > new.START_DATE OR END_DATE IS NULL))) THEN "
    			+ "SIGNAL SQLSTATE VALUE '45000' SET MESSAGE_TEXT = 'UPDATE failed due to overlap of dates'; END IF; END";
    	//System.out.println(query);
    	try {
    		statement = connection.prepareStatement(drop_query);
    		statement.execute();
    		
    		statement = connection.prepareStatement(query);
    		statement.execute();
    	}
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	return query;
    }
	
    public void add_Triggers(String table,String hist_table,Map<String,String> pk,Map<String,String> col){
   		String after_insert_trigger = onInsert_Trigger(table,hist_table,pk,col);
   		String after_update_trigger = onUpdate_Trigger(table,hist_table,pk,col);
   		String after_delete_trigger = onDelete_Trigger(table,hist_table,pk,col);
   		
   		String uniqueness_before_insert_trigger = uniqueness_beforeInsert_Trigger(table,hist_table,pk);
   		String uniqueness_before_update_trigger = uniqueness_beforeUpdate_Trigger(table,hist_table,pk);
   		
   		File file = new File("/home/deepika/eclipse-workspace/Temporal_Event_Store/triggers_queries.txt");
	   	try {
			file.createNewFile();
			FileWriter output = new FileWriter(file);
			output.write(after_insert_trigger + "\n");
			output.write(after_update_trigger + "\n");
			output.write(after_delete_trigger + "\n");
			output.write(uniqueness_before_insert_trigger + "\n");
			output.write(uniqueness_before_update_trigger + "\n");
			output.close();
		} 
	   	catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public Map<String,String> getInsert_UpdateColumns(String tableName) {
    	Map<String,String> ColumnNames = new HashMap<String,String>();
    	String query = "select COLUMN_NAME, COLUMN_TYPE FROM information_schema.COLUMNS where TABLE_SCHEMA = ? "
    			+ "and TABLE_NAME = ? and EXTRA!='auto_increment'";
		
    	try {
    		statement = connection.prepareStatement(query);
    		statement.setString(1,schema);
    		statement.setString(2,tableName);
    		resultSet = statement.executeQuery();
    		
    		while(resultSet.next()) {
    			String key = resultSet.getString("COLUMN_NAME"); 
    			String value = resultSet.getString("COLUMN_TYPE");
    			//System.out.println(key+value);
    			
    			ColumnNames.put(key,value);
        	}
    	}
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	
    	return ColumnNames;
    }
    
    public Map<String,String> getSelect_DeleteColumns(String tableName) {
    	Map<String,String> ColumnNames = new HashMap<String,String>();
    	String query = "select COLUMN_NAME, COLUMN_TYPE FROM information_schema.COLUMNS where TABLE_SCHEMA = ? "
    			+ "and TABLE_NAME = ?";
    	try {
    		statement = connection.prepareStatement(query);
    		statement.setString(1,schema);
    		statement.setString(2,tableName);
    		//System.out.println(query);
    		resultSet = statement.executeQuery();
    		
    		while(resultSet.next()) {
    			String key = resultSet.getString("COLUMN_NAME"); 
    			String value = resultSet.getString("COLUMN_TYPE");
    			//System.out.println(key+value);
    			
    			ColumnNames.put(key,value);
        	}
    	}
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	
    	return ColumnNames;
    }
    
    public void insertRow(Map<String,String> row,String table) {
		String query = "INSERT INTO " + table +"(";
    	int i=0;
    	ArrayList<String> values = new ArrayList<String>();
    	for(Map.Entry<String,String> entry:row.entrySet()) {
    		if(i==0) {
    			query += entry.getKey();
    			i=1;
    		}
    		else {
    			query += "," + entry.getKey();
    		}
    		values.add(entry.getValue());
    	}
    	query += ") VALUES('"+values.get(0);
    	for(int j=1;j<values.size();j++) {
    		query += "','" + values.get(j);
    	}
    	query += "')";
    	//System.out.println(query);
    	try {
    		statement = connection.prepareStatement(query);
    		statement.executeUpdate();
    	}
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    }
    
    public void updateRow(Map<String,String> pk,Map<String,String> row,String table) {
		String query = "UPDATE " + table +" SET ";
		int i=0;
    	for(Map.Entry<String,String> entry:row.entrySet()) {
    		if(i==0) {
    			query += entry.getKey() + "='" + entry.getValue() + "'";
    			i=1;
    		}
    		else {
    			query += "," + entry.getKey() + "='" + entry.getValue() + "'";
    		}
    	}
    	query += " WHERE ";
    	i=0;
    	for(Map.Entry<String,String> entry:pk.entrySet()) {
    		if(i==0) {
    			query += entry.getKey() + "='" + entry.getValue() + "'";
    			i=1;
    		}
    		else {
    			query += " AND " + entry.getKey() + "='" + entry.getValue() + "'";
    		}
    	}
    	//System.out.println(query);
    	try {
    		statement = connection.prepareStatement(query);
    		statement.executeUpdate();
    	}
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    }
    
    public void deleteRow(Map<String,String> row,String table) {
		String query = "DELETE FROM " + table +" WHERE ";
    	int i=0;
    	for(Map.Entry<String,String> entry:row.entrySet()) {
    		if(i==0) {
    			query += entry.getKey() + "='" + entry.getValue() + "'";
    			i=1;
    		}
    		else {
    			query += " AND " + entry.getKey() + "='" + entry.getValue() + "'";
    		}
    	}
    	//System.out.println(query);
    	try {
    		statement = connection.prepareStatement(query);
    		statement.executeUpdate();
    	}
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    }
    
    public ResultSet selectRows(Map<String,String> row,String table) {
    	ResultSet result=null;
		String query = "SELECT * FROM " + table +" WHERE ";
		int i=0;
    	for(Map.Entry<String,String> entry:row.entrySet()) {
    		if(i==0) {
    			query += entry.getKey() + "='" + entry.getValue() + "'";
    			i=1;
    		}
    		else {
    			query += " AND " + entry.getKey() + "='" + entry.getValue() + "'";
    		}
    	}
    	//System.out.println(query);
    	try {
    		statement = connection.prepareStatement(query);
    		result = statement.executeQuery();
    	}
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	return result;
    }
    
    //first status of the column of the specified row with given pk values 
    public ArrayList<Map<String,String>> getFirst(Map<String,String> pk, String table, String column) {
    	ArrayList<Map<String,String>> result = new ArrayList<Map<String,String>>();
    	String query = "SELECT "+column+",START_DATE,END_DATE FROM hist_" + table + " WHERE ";
    	int i=0;
    	for(Map.Entry<String,String> entry:pk.entrySet()) {
    		if(i==0) {
    			query += entry.getKey() + "='" + entry.getValue() + "'";
    			i=1;
    		}
    		else {
    			query += " AND " + entry.getKey() + "='" + entry.getValue() + "'";
    		}
    	}
    	i=0;
    	query += " AND START_DATE = (SELECT MIN(START_DATE) FROM hist_"+ table + " WHERE ";
    	for(Map.Entry<String,String> entry:pk.entrySet()) {
    		if(i==0) {
    			query += entry.getKey() + "='" + entry.getValue() + "'";
    			i=1;
    		}
    		else {
    			query += " AND " + entry.getKey() + "='" + entry.getValue() + "'";
    		}
    	}
    	query += ")";
    	//System.out.println(query);
    	resultSet = null;
    	try {
    		statement = connection.prepareStatement(query);
    		resultSet = statement.executeQuery();
    		while(resultSet.next()) {
    			Map<String,String> row = new HashMap<String,String>();
    			row.put(column, resultSet.getString(column));
    			row.put("START_DATE", resultSet.getString("START_DATE"));
    			row.put("END_DATE", resultSet.getString("END_DATE"));
    			result.add(row);
    		}
    	} 
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	
		return result;
    }
    
  //latest status of the column of the specified row with given pk values 
    public ArrayList<Map<String,String>> getLast(Map<String,String> pk, String table, String column) {
    	ArrayList<Map<String,String>> result = new ArrayList<Map<String,String>>();
    	String query1 = "SELECT DISTINCT "+column+" FROM hist_" + table + " WHERE ";
    	int i=0;
    	for(Map.Entry<String,String> entry:pk.entrySet()) {
    		if(i==0) {
    			query1 += entry.getKey() + "='" + entry.getValue() + "'";
    			i=1;
    		}
    		else {
    			query1 += " AND " + entry.getKey() + "='" + entry.getValue() + "'";
    		}
    	}
    	i=0;
    	query1 += " AND START_DATE = (SELECT MAX(START_DATE) FROM hist_"+table+" WHERE ";
    	for(Map.Entry<String,String> entry:pk.entrySet()) {
    		if(i==0) {
    			query1 += entry.getKey() + "='" + entry.getValue() + "'";
    			i=1;
    		}
    		else {
    			query1 += " AND " + entry.getKey() + "='" + entry.getValue() + "'";
    		}
    	}
    	
    	query1 +=")";
    	
    	
    	String query2 = "SELECT MIN(START_DATE) FROM hist_"+table+" WHERE ";
    	i=0;
    	for(Map.Entry<String,String> entry:pk.entrySet()) {
    		if(i==0) {
    			query2 += entry.getKey() + "='" + entry.getValue() + "'";
    			i=1;
    		}
    		else {
    			query2 += " AND " + entry.getKey() + "='" + entry.getValue() + "'";
    		}
    	}
    	
    	query2 += " AND "+column+" in ("+query1+")";
    	
    	String query = "SELECT DISTINCT "+column+" , START_DATE, END_DATE FROM hist_"+table+" WHERE ";
    	i=0;
    	for(Map.Entry<String,String> entry:pk.entrySet()) {
    		if(i==0) {
    			query += entry.getKey() + "='" + entry.getValue() + "'";
    			i=1;
    		}
    		else {
    			query += " AND " + entry.getKey() + "='" + entry.getValue() + "'";
    		}
    	}
    	query += " AND START_DATE = ("+query2+") AND "+column+" in ("+query1+")";
    	//System.out.println(query);
    	resultSet = null;
    	try {
    		statement = connection.prepareStatement(query);
    		resultSet = statement.executeQuery();
    		while(resultSet.next()) {
    			Map<String,String> row = new HashMap<String,String>();
    			row.put(column, resultSet.getString(column));
    			row.put("START_DATE", resultSet.getString("START_DATE"));
    			row.put("END_DATE", resultSet.getString("END_DATE"));
    			result.add(row);
    		}
    	} 
    	catch(SQLException e){
    		e.printStackTrace();
    	}
		return result;
    }
    
    //Value that precedes the given value 'cVal' along with its START_DATE
    public ArrayList<Map<String,String>> getPrevious(Map<String,String> pk, String table, String column, String cVal) {
    	ArrayList<Map<String,String>> result = new ArrayList<Map<String,String>>();
    	String query = "SELECT " + column + ", START_DATE FROM hist_"+ table+
    			" WHERE START_DATE = (SELECT MAX(START_DATE) FROM hist_"+table
    			+" WHERE START_DATE < (SELECT MIN(START_DATE) FROM hist_"+table
    			+" WHERE "+column+"="+cVal +" AND ";
    			
    	int i=0;
    	for(Map.Entry<String,String> entry:pk.entrySet()) {
    		if(i==0) {
    			query += entry.getKey() + "='" + entry.getValue() + "'";
    			i=1;
    		}
    		else {
    			query += " AND " + entry.getKey() + "='" + entry.getValue() + "'";
    		}
    	}
    	
    	query += ") AND ";
    	i=0;
    	for(Map.Entry<String,String> entry:pk.entrySet()) {
    		if(i==0) {
    			query += entry.getKey() + "='" + entry.getValue() + "'";
    			i=1;
    		}
    		else {
    			query += " AND " + entry.getKey() + "='" + entry.getValue() + "'";
    		}
    	}
    	query += ") AND ";
    	i=0;
    	for(Map.Entry<String,String> entry:pk.entrySet()) {
    		if(i==0) {
    			query += entry.getKey() + "='" + entry.getValue() + "'";
    			i=1;
    		}
    		else {
    			query += " AND " + entry.getKey() + "='" + entry.getValue() + "'";
    		}
    	}
    	//System.out.println(query);
    	resultSet = null;
    	try {
    		statement = connection.prepareStatement(query);
    		resultSet = statement.executeQuery();
    		while(resultSet.next()) {
    			Map<String,String> row = new HashMap<String,String>();
    			row.put(column, resultSet.getString(column));
    			row.put("START_DATE", resultSet.getString("START_DATE"));
    			result.add(row);
    		}
    	} 
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	return result;
    }
    
    //Value that follows the given value 'cVal' along with its START_DATE
    public ArrayList<Map<String,String>> getNext(Map<String,String> pk, String table, String column, String cVal) {
    	ArrayList<Map<String,String>> result = new ArrayList<Map<String,String>>();
    	String query = "SELECT " + column + ", START_DATE FROM hist_"+ table+
    			" WHERE START_DATE = (SELECT MIN(START_DATE) FROM hist_"+table
    			+" WHERE START_DATE > (SELECT MAX(START_DATE) FROM hist_"+table
    			+" WHERE "+column+"="+cVal +" AND ";
    			
    	int i=0;
    	for(Map.Entry<String,String> entry:pk.entrySet()) {
    		if(i==0) {
    			query += entry.getKey() + "='" + entry.getValue() + "'";
    			i=1;
    		}
    		else {
    			query += " AND " + entry.getKey() + "='" + entry.getValue() + "'";
    		}
    	}
    	
    	query += ") AND ";
    	i=0;
    	for(Map.Entry<String,String> entry:pk.entrySet()) {
    		if(i==0) {
    			query += entry.getKey() + "='" + entry.getValue() + "'";
    			i=1;
    		}
    		else {
    			query += " AND " + entry.getKey() + "='" + entry.getValue() + "'";
    		}
    	}
    	query += ") AND ";
    	i=0;
    	for(Map.Entry<String,String> entry:pk.entrySet()) {
    		if(i==0) {
    			query += entry.getKey() + "='" + entry.getValue() + "'";
    			i=1;
    		}
    		else {
    			query += " AND " + entry.getKey() + "='" + entry.getValue() + "'";
    		}
    	}
    	//System.out.println(query);
    	resultSet = null;
    	try {
    		statement = connection.prepareStatement(query);
    		resultSet = statement.executeQuery();
    		while(resultSet.next()) {
    			Map<String,String> row = new HashMap<String,String>();
    			row.put(column, resultSet.getString(column));
    			row.put("START_DATE", resultSet.getString("START_DATE"));
    			result.add(row);
    		}
    	} 
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	return result;
    }
    
    
    //Indicates the value which precedes the current value and its timestamp, according to the specified day
    public String getPrevious_SCALE(Map<String,String> pk, String table, String column, String scale) {
    	String query = "SELECT "+column+" FROM hist_"+table+" WHERE ";
    	int i=0;
    	for(Map.Entry<String,String> entry:pk.entrySet()) {
    		if(i==0) {
    			query += entry.getKey() + "='" + entry.getValue() + "'";
    			i=1;
    		}
    		else {
    			query += " AND " + entry.getKey() + "='" + entry.getValue() + "'";
    		}
    	}
    	query += " AND START_DATE <= (SELECT DATE_ADD(NOW(),INTERVAL -1 " + scale.toUpperCase() + ")) AND "
					+ "END_DATE > (SELECT DATE_ADD(NOW(),INTERVAL -1 " + scale.toUpperCase() + "))";
    	//System.out.println(query);
    	resultSet = null;
    	String colVal = null;
    	try {
    		statement = connection.prepareStatement(query);
    		resultSet = statement.executeQuery();
    		while(resultSet.next()) {
    			colVal = resultSet.getString(column);
    			break;
    		}
    	} 
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	return colVal;
    }
    
    //Indicates the value which follows the current value and its timestamp, according to the specified day
    public String getNext_SCALE(Map<String,String> pk, String table, String column, String scale) {
    	String query = "SELECT "+column+" FROM hist_"+table+" WHERE ";
    	int i=0;
    	for(Map.Entry<String,String> entry:pk.entrySet()) {
    		if(i==0) {
    			query += entry.getKey() + "='" + entry.getValue() + "'";
    			i=1;
    		}
    		else {
    			query += " AND " + entry.getKey() + "='" + entry.getValue() + "'";
    		}
    	}
    	query += " AND START_DATE <= (SELECT DATE_ADD(NOW(),INTERVAL 1 " + scale.toUpperCase() + ")) AND "
				+ "END_DATE > (SELECT DATE_ADD(NOW(),INTERVAL 1 " + scale.toUpperCase() + "))";
    	//System.out.println(query);
    	resultSet = null;
    	String colVal = null;
    	try {
    		statement = connection.prepareStatement(query);
    		resultSet = statement.executeQuery();
    		while(resultSet.next()) {
    			colVal = resultSet.getString(column);
    			break;
    		}
    	} 
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	return colVal;
    }
    
    //Indicates all the evolution dates of the column
    public ArrayList<Map<String,String>> getEvolution_History(Map<String,String> pk, String table, String column) {
    	ArrayList<Map<String,String>> result = new ArrayList<Map<String,String>>();
    	String query = "SELECT "+column+", MIN(START_DATE) FROM hist_"+table+" WHERE ";
    	int i=0;
    	for(Map.Entry<String,String> entry:pk.entrySet()) {
    		if(i==0) {
    			query += entry.getKey() + "='" + entry.getValue() + "'";
    			i=1;
    		}
    		else {
    			query += " AND " + entry.getKey() + "='" + entry.getValue() + "'";
    		}
    	}
    	query += "GROUP BY "+column;
    	//System.out.println(query);
    	resultSet = null;
    	try {
    		statement = connection.prepareStatement(query);
    		resultSet = statement.executeQuery();
    		while(resultSet.next()) {
    			Map<String,String> row = new HashMap<String,String>();
    			row.put(column, resultSet.getString(column));
    			row.put("START_DATE", resultSet.getString("MIN(START_DATE)"));
    			result.add(row);
    		}
    	} 
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	
    	return result;
    }
    
    //Indicates the evolution date to the given value
    public ArrayList<Map<String,String>> getEvolution(Map<String,String> pk, String table, String column, String val) {
    	ArrayList<Map<String,String>> result = new ArrayList<Map<String,String>>();
    	String query = "SELECT "+column+", DATE(START_DATE), DATE(END_DATE) FROM hist_" + table + " WHERE ";
    	int i=0;
    	for(Map.Entry<String,String> entry:pk.entrySet()) {
    		if(i==0) {
    			query += entry.getKey() + "='" + entry.getValue() + "'";
    			i=1;
    		}
    		else {
    			query += " AND " + entry.getKey() + "='" + entry.getValue() + "'";
    		}
    	}
    	i=0;
    	query += " AND START_DATE = (SELECT MIN(START_DATE) FROM hist_"+ table + " WHERE ";
    	for(Map.Entry<String,String> entry:pk.entrySet()) {
    		if(i==0) {
    			query += entry.getKey() + "='" + entry.getValue() + "'";
    			i=1;
    		}
    		else {
    			query += " AND " + entry.getKey() + "='" + entry.getValue() + "'";
    		}
    	}
    	query += "AND "+column+"='"+val+"' ) AND "+column+"='"+val+"'";
    	//System.out.println(query);
    	resultSet = null;
    	try {
    		statement = connection.prepareStatement(query);
    		resultSet = statement.executeQuery();
    		while(resultSet.next()) {
    			Map<String,String> row = new HashMap<String,String>();
    			row.put(column, resultSet.getString(column));
    			row.put("START_DATE", resultSet.getString("DATE(START_DATE)"));
    			row.put("END_DATE", resultSet.getString("DATE(END_DATE)"));
    			result.add(row);
    		}
    	} 
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	return result;
    }
    
    //Indicates the first evolution date of the column
    public ArrayList<Map<String,String>> getFirst_Evolution(Map<String,String> pk, String table, String column) {
    	ArrayList<Map<String,String>> result = new ArrayList<Map<String,String>>();
    	String query = "SELECT DATE(START_DATE), DATE(END_DATE) FROM hist_" + table + " WHERE ";
    	int i=0;
    	for(Map.Entry<String,String> entry:pk.entrySet()) {
    		if(i==0) {
    			query += entry.getKey() + "='" + entry.getValue() + "'";
    			i=1;
    		}
    		else {
    			query += " AND " + entry.getKey() + "='" + entry.getValue() + "'";
    		}
    	}
    	i=0;
    	query += " AND START_DATE = (SELECT MIN(START_DATE) FROM hist_"+ table + " WHERE ";
    	for(Map.Entry<String,String> entry:pk.entrySet()) {
    		if(i==0) {
    			query += entry.getKey() + "='" + entry.getValue() + "'";
    			i=1;
    		}
    		else {
    			query += " AND " + entry.getKey() + "='" + entry.getValue() + "'";
    		}
    	}
    	query += ")";
    	//System.out.println(query);
    	resultSet = null;
    	try {
    		statement = connection.prepareStatement(query);
    		resultSet = statement.executeQuery();
    		while(resultSet.next()) {
    			Map<String,String> row = new HashMap<String,String>();
    			row.put("START_DATE", resultSet.getString("DATE(START_DATE)"));
    			row.put("END_DATE", resultSet.getString("DATE(END_DATE)"));
    			result.add(row);
    		}
    	} 
    	catch(SQLException e){
    		e.printStackTrace();
    	}
		return result;
    }
    
    //Indicates the last evolution date of the column
    public ArrayList<Map<String,String>> getLast_Evolution(Map<String,String> pk, String table, String column) {
    	ArrayList<Map<String,String>> result = new ArrayList<Map<String,String>>();
    	String query = "SELECT DATE(START_DATE), DATE(END_DATE) FROM hist_" + table + " WHERE ";
    	int i=0;
    	for(Map.Entry<String,String> entry:pk.entrySet()) {
    		if(i==0) {
    			query += entry.getKey() + "='" + entry.getValue() + "'";
    			i=1;
    		}
    		else {
    			query += " AND " + entry.getKey() + "='" + entry.getValue() + "'";
    		}
    	}
    	i=0;
    	query += " AND START_DATE = (SELECT MAX(START_DATE) FROM hist_"+ table + " WHERE ";
    	for(Map.Entry<String,String> entry:pk.entrySet()) {
    		if(i==0) {
    			query += entry.getKey() + "='" + entry.getValue() + "'";
    			i=1;
    		}
    		else {
    			query += " AND " + entry.getKey() + "='" + entry.getValue() + "'";
    		}
    	}
    	query += ")";
    	//System.out.println(query);
    	resultSet = null;
    	try {
    		statement = connection.prepareStatement(query);
    		resultSet = statement.executeQuery();
    		while(resultSet.next()) {
    			Map<String,String> row = new HashMap<String,String>();
    			row.put("START_DATE", resultSet.getString("DATE(START_DATE)"));
    			row.put("END_DATE", resultSet.getString("DATE(END_DATE)"));
    			result.add(row);
    		}
    	} 
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	return result;
    }
    
  //Indicates the evolution date from ‘val1’ to ‘val2’.
    public ArrayList<Map<String,String>> getEvolutionVal12(Map<String,String> pk, String table, String column, String val1, String val2) {
    	ArrayList<Map<String,String>> result = new ArrayList<Map<String,String>>();
    	//to retrieve the start_date of val1
    	String query_val1 = "SELECT MAX(START_DATE) FROM hist_"+ table +" WHERE ";
    	int i=0;
    	for(Map.Entry<String,String> entry:pk.entrySet()) {
    		if(i==0) {
    			query_val1 += entry.getKey() + "='" + entry.getValue() + "'";
    			i=1;
    		}
    		else {
    			query_val1 += " AND " + entry.getKey() + "='" + entry.getValue() + "'";
    		}
    	}
    	query_val1 += " AND "+column+"='"+val1+"'";
    	//to retrieve the start_date of val2
    	String query_val2 = "SELECT MIN(START_DATE) FROM hist_"+table+" WHERE ";
    	i=0;
    	for(Map.Entry<String,String> entry:pk.entrySet()) {
    		if(i==0) {
    			query_val2 += entry.getKey() + "='" + entry.getValue() + "'";
    			i=1;
    		}
    		else {
    			query_val2 += " AND " + entry.getKey() + "='" + entry.getValue() + "'";
    		}
    	}
    	query_val2 += " AND "+column+"='"+val2+"'";
    	resultSet = null;
    	String date_val1=null, date_val2 = null;
    	try {
    		statement = connection.prepareStatement(query_val1);
    		resultSet = statement.executeQuery();
    		if(resultSet.next())	date_val1 = resultSet.getString("MAX(START_DATE)");
    		
    		resultSet=null;
    		statement = connection.prepareStatement(query_val2);
    		resultSet = statement.executeQuery();
    		if(resultSet.next())	date_val2 = resultSet.getString("MIN(START_DATE)");
    		
    		if(date_val2.compareTo(date_val1)>=0) {
    			String query = "SELECT DISTINCT "+column+", START_DATE, END_DATE FROM hist_"+table+" WHERE ";
    			i=0;
    	    	for(Map.Entry<String,String> entry:pk.entrySet()) {
    	    		if(i==0) {
    	    			query += entry.getKey() + "='" + entry.getValue() + "'";
    	    			i=1;
    	    		}
    	    		else {
    	    			query += " AND " + entry.getKey() + "='" + entry.getValue() + "'";
    	    		}
    	    	}
    	    	resultSet = null;
    	    	query += "AND START_DATE BETWEEN '"+date_val1+"' AND '"+date_val2+"'";
    	    	//System.out.println(query_val1);
    	    	//System.out.println(query_val2);
    	    	statement = connection.prepareStatement(query);
        		resultSet = statement.executeQuery();
        		while(resultSet.next()) {
        			Map<String,String> row = new HashMap<String,String>();
        			row.put(column, resultSet.getString(column));
        			row.put("START_DATE", resultSet.getString("START_DATE"));
        			row.put("END_DATE", resultSet.getString("END_DATE"));
        			result.add(row);
        		}
    		}
    		
    	} 
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	return result;
    	
    }
    
    //Indicates the timestamp associated to the value ‘val’
    public ArrayList<Map<String,String>> getTimestamps(Map<String,String> pk, String table, String column, String val) {
    	ArrayList<Map<String,String>> result = new ArrayList<Map<String,String>>();
    	String query = "SELECT START_DATE, END_DATE FROM hist_"+table+" WHERE ";
    	int i=0;
    	for(Map.Entry<String,String> entry:pk.entrySet()) {
    		if(i==0) {
    			query += entry.getKey() + "='" + entry.getValue() + "'";
    			i=1;
    		}
    		else {
    			query += " AND " + entry.getKey() + "='" + entry.getValue() + "'";
    		}
    	}
    	query += " AND "+column+"='"+val+"' ORDER BY START_DATE LIMIT 1";
    	//System.out.println(query);
    	resultSet = null;
    	try {
    		statement = connection.prepareStatement(query);
    		resultSet = statement.executeQuery();
    		while(resultSet.next()) {
    			Map<String,String> row = new HashMap<String,String>();
    			row.put("START_DATE", resultSet.getString("START_DATE"));
    			row.put("END_DATE", resultSet.getString("END_DATE"));
    			result.add(row);
    		}
    	} 
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	return result;
    }
    
    //Indicates the value associated with a date specified somewhere in the query
    public String getColumn_Value(Map<String,String> pk, String table, String column, String date) {
    	String query = "SELECT DISTINCT "+column+" FROM hist_"+table+" WHERE ";
    	int i=0;
    	for(Map.Entry<String,String> entry:pk.entrySet()) {
    		if(i==0) {
    			query += entry.getKey() + "='" + entry.getValue() + "'";
    			i=1;
    		}
    		else {
    			query += " AND " + entry.getKey() + "='" + entry.getValue() + "'";
    		}
    	}
    	query += " AND START_DATE <= '"+date+"' AND (END_DATE >= '"+date+"' OR END_DATE IS NULL)";
    	//System.out.println(query);
    	resultSet = null;
    	String colVal = null;
    	try {
    		statement = connection.prepareStatement(query);
    		resultSet = statement.executeQuery();
    		while(resultSet.next()) {
    			colVal = resultSet.getString(column);
    			break;
    		}
    	} 
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	return colVal;
    }
    
    
    public ArrayList<Map<String,String>> getColumn_Timestamp_Name(Map<String,String> pk, String table, String column, String colVal, String timestamp_name) {
    	ArrayList<Map<String,String>> result = new ArrayList<Map<String,String>>();
    	String query = "SELECT "+column+", "+timestamp_name.toUpperCase()+"(START_DATE), "+timestamp_name.toUpperCase()+"(END_DATE) FROM hist_" + table + " WHERE ";
    	int i=0;
    	for(Map.Entry<String,String> entry:pk.entrySet()) {
    		if(i==0) {
    			query += entry.getKey() + "='" + entry.getValue() + "'";
    			i=1;
    		}
    		else {
    			query += " AND " + entry.getKey() + "='" + entry.getValue() + "'";
    		}
    	}
    	i=0;
    	query += " AND START_DATE = (SELECT MIN(START_DATE) FROM hist_"+ table + " WHERE ";
    	for(Map.Entry<String,String> entry:pk.entrySet()) {
    		if(i==0) {
    			query += entry.getKey() + "='" + entry.getValue() + "'";
    			i=1;
    		}
    		else {
    			query += " AND " + entry.getKey() + "='" + entry.getValue() + "'";
    		}
    	}
    	query += "AND "+column+"='"+colVal+"' ) AND "+column+"='"+colVal+"'";
    	//System.out.println(query);
    	resultSet = null;
    	try {
    		statement = connection.prepareStatement(query);
    		resultSet = statement.executeQuery();
    		while(resultSet.next()) {
    			Map<String,String> row = new HashMap<String,String>();
    			row.put(column, resultSet.getString(column));
    			row.put("START_DATE("+timestamp_name.toUpperCase()+")", resultSet.getString(timestamp_name.toUpperCase()+"(START_DATE)"));
    			row.put("END_DATE("+timestamp_name.toUpperCase()+")", resultSet.getString(timestamp_name.toUpperCase()+"(END_DATE)"));
    			result.add(row);
    		}
    	} 
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	return result;
    }
    
    //Temporal Join(Assume to be of the same schema)
    public ArrayList<String[][]> getPosTemporalJoinTables() {
    	String query = "SELECT TABLE_NAME,COLUMN_NAME,REFERENCED_TABLE_NAME,REFERENCED_COLUMN_NAME FROM"
    			+ " INFORMATION_SCHEMA.KEY_COLUMN_USAGE WHERE REFERENCED_TABLE_SCHEMA = ? AND TABLE_SCHEMA = ?";
    	ArrayList<String[][]> tempJoinTables = new ArrayList<String[][]>();
    	try {
    		statement = connection.prepareStatement(query);
    		statement.setString(1,schema);
    		statement.setString(2,schema);
    		resultSet = statement.executeQuery();
    		
    		while(resultSet.next()) {
    			String[][] joinInfo = new String[2][2];
    			joinInfo[0][0] = resultSet.getString("TABLE_NAME");
    			joinInfo[0][1] = resultSet.getString("COLUMN_NAME");
    			joinInfo[1][0] = resultSet.getString("REFERENCED_TABLE_NAME");
    			joinInfo[1][1] = resultSet.getString("REFERENCED_COLUMN_NAME");
    			
    			tempJoinTables.add(joinInfo);
        	}
    	}
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	
    	return tempJoinTables;
    }
    
    public ArrayList<Map<String,String>> join_tables(String[][] join_info,String type,ArrayList<String> col) {
    	String query = null;
    	resultSet = null;
    	ArrayList<Map<String,String>> result = new ArrayList<Map<String,String>>();
    	if(type.equals("normal")) {
    		query = TablesJoinQuery(join_info,col);
    	}
    	else if(type.equals("temporal")) {
    		query = temporal_join(join_info,col);
    	}
    	
    	try {
    		statement = connection.prepareStatement(query);
    		resultSet = statement.executeQuery();
    		while(resultSet.next()) {
    			Map<String,String> row = new HashMap<String,String>();
   		    	for(int i=0;i<col.size();i++) {
   		    		row.put(col.get(i),resultSet.getString(col.get(i)));
   		    	}
    			result.add(row);
    		}
    	}
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	return result;
    }
    
    public String TablesJoinQuery(String[][] join_info,ArrayList<String> col) {
		String query = "SELECT " + col.get(0);
    	for(int i=0;i<col.size();i++) {
   			query += ", " + col.get(i);
    	}
    	query += " from "+join_info[0][0]+" INNER JOIN "+ join_info[1][0]
    			+ " ON " +join_info[0][0]+"."+join_info[0][1]+" = " + join_info[1][0] +"." + join_info[1][1];
    	
    	//System.out.println(query);
    	return query;
    }
    
	public String temporal_join(String[][] join_info,ArrayList<String> col) {
		String query = "SELECT " + col.get(0);
    	for(int i=0;i<col.size();i++) {
   			query += ", " + col.get(i);
    	}
    	query += " from "+join_info[0][0]+" INNER JOIN "+ join_info[1][0]
    			+ " ON " +join_info[0][0]+"."+join_info[0][1]+" = " + join_info[1][0] +"." + join_info[1][1]
						+ " and (" + join_info[0][0] + ".START_DATE < " + join_info[1][0] + ".END_DATE or "+join_info[1][0]+".END_DATE is NULL) "
							+ "and ("+join_info[0][0]+".END_DATE > "+join_info[1][0]+".START_DATE or "+join_info[0][0]+".END_DATE is NULL)";
    	//System.out.println(query);
    	return query;
	}
	
	public ArrayList<Map<String,String>> Coalesce_Operator(Map<String,String> pk,String table,Map<String,String> col) {
        ArrayList<Map<String,String>> result = new ArrayList<Map<String,String>>();
        String query = "SELECT DISTINCT f.START_DATE, l.END_DATE";
        for(Map.Entry<String,String> entry:pk.entrySet()) {
   			query += ", f."+entry.getKey();
    	}
        
        query += " FROM "+table+" f, "+table+" l WHERE f.START_DATE < l.END_DATE";
        for(Map.Entry<String,String> entry:pk.entrySet()) {
            query += " AND f."+entry.getKey()+" = "+entry.getValue()+" AND l."+entry.getKey()+" = "+entry.getValue();
    	}
        for(Map.Entry<String,String> entry:col.entrySet()) {
        	query += " AND f."+entry.getKey()+" = l."+entry.getKey();
    	}
        query += " AND NOT EXISTS(SELECT * FROM "+table+" a2 WHERE ";
        for(Map.Entry<String,String> entry:pk.entrySet()) {
            query += "f."+entry.getKey()+" = "+entry.getValue()+" AND l."+entry.getKey()+" = "+entry.getValue()+" AND a2."+entry.getKey()+" = "+entry.getValue()+" AND ";
    	}
        for(Map.Entry<String,String> entry:col.entrySet()) {
            query += "a2."+entry.getKey()+" = f."+entry.getKey() + " AND ";
        }
        query += "((a2.START_DATE < f.START_DATE AND f.START_DATE <= a2.END_DATE) "
                + "OR (a2.START_DATE <= l.END_DATE AND l.END_DATE < a2.END_DATE)))";
       
        System.out.println(query);
        resultSet = null;
        try {
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            while(resultSet.next()) {
                Map<String,String> row = new HashMap<String,String>();
                for(Map.Entry<String,String> entry:pk.entrySet()) {
                    row.put(entry.getKey(), resultSet.getString("f."+entry.getKey()));
                }
                for(Map.Entry<String,String> entry:col.entrySet()) {
                    row.put(entry.getKey(), resultSet.getString("f."+entry.getKey()));
                }
                row.put("START_DATE", resultSet.getString("f.START_DATE"));
                row.put("END_DATE", resultSet.getString("l.END_DATE"));
                result.add(row);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return result;
    }
	
	public ArrayList<Map<String,String>> Overlaps_Operator(String[][] join_info,ArrayList<String> col, Map<String, String> entered_val) {
		ArrayList<Map<String,String>> result = new ArrayList<Map<String,String>>();
		String query = "SELECT " + col.get(0);
    	for(int i=0;i<col.size();i++) {
   			query += ", " + col.get(i);
    	}
    	query += " from "+join_info[0][0]+" INNER JOIN "+ join_info[1][0]
    			+ " ON " +join_info[0][0]+"."+join_info[0][1]+" = " + join_info[1][0] +"." + join_info[1][1]
						+ " and (" + join_info[0][0] + ".START_DATE < " + join_info[1][0] + ".END_DATE or "+join_info[1][0]+".END_DATE is NULL) "
							+ "and ("+join_info[0][0]+".END_DATE > "+join_info[1][0]+".START_DATE or "+join_info[0][0]+".END_DATE is NULL)";
    	for(Map.Entry<String,String> entry:entered_val.entrySet()) {
            query += " and "+entry.getKey()+" = "+entry.getValue();
        }
		//System.out.println(query);
        resultSet = null;
		try {
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            while(resultSet.next()) {
                Map<String,String> row = new HashMap<String,String>();
                for(int i=0;i<col.size();i++) {
   		    		row.put(col.get(i),resultSet.getString(col.get(i)));
   		    	}
    			result.add(row);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return result;
    }
	
	public ArrayList<Map<String,String>> Difference_Operator(String[][] tables,Map<String,String> pk,String ref_FK) {
		String query1 = "select" ;
		int i = 0;
		for(Map.Entry<String,String> entry:pk.entrySet()) {
			if(i==0) {
				query1 += " T1."+entry.getKey()+" AS "+entry.getKey();
			}
			else {
				query1 += ", T1."+entry.getKey()+" AS "+entry.getKey();
			}
        }
		query1 += ", T1.START_DATE AS START_DATE, A1.START_DATE AS END_DATE from "+tables[0][0]+" AS T1, "+tables[1][0]+" AS A1 where A1."+
				tables[1][1]+"= "+ref_FK+" AND A1."+tables[1][1]+"= T1."+tables[0][1] + " AND T1.START_DATE < A1.START_DATE AND A1.START_DATE < T1.END_DATE";
		for(Map.Entry<String,String> entry:pk.entrySet()) {
			query1 += " AND T1."+entry.getKey()+" = "+entry.getValue();
        }
		query1 += " AND NOT EXISTS ( SELECT * FROM "+tables[1][0]+" AS A2 WHERE T1."+tables[0][1]+ "= A2." + tables[1][1] +" AND "
				+ "A2."+tables[1][1]+"=" + ref_FK + " AND T1.START_DATE < A2.END_DATE AND A2.START_DATE < A1.START_DATE )";
		
		String query2 = "select";
		i = 0;
		for(Map.Entry<String,String> entry:pk.entrySet()) {
			if(i==0) {
				query2 += " T1."+entry.getKey()+" AS "+entry.getKey();
			}
			else {
				query2 += ", T1."+entry.getKey()+" AS "+entry.getKey();
			}
        }
		query2 += ", A1.END_DATE AS START_DATE, T1.END_DATE AS END_DATE from "+tables[0][0]+" AS T1, "+tables[1][0]+" AS A1 where A1."+
				tables[1][1]+"= "+ref_FK+" AND A1."+tables[1][1]+"= T1."+tables[0][1] + " AND T1.START_DATE < A1.START_DATE AND A1.START_DATE < T1.END_DATE";
		for(Map.Entry<String,String> entry:pk.entrySet()) {
			query2 += " AND T1."+entry.getKey()+" = "+entry.getValue();
        }
		query2 += " AND NOT EXISTS ( SELECT * FROM "+tables[1][0]+" AS A2 WHERE T1."+tables[0][1]+ "= A2." + tables[1][1] +" AND "
				+ "A2."+tables[1][1]+"=" + ref_FK + " AND A1.END_DATE < A2.END_DATE AND A2.START_DATE < T1.END_DATE )";
		
		String query3 = "select";
		i = 0;
		for(Map.Entry<String,String> entry:pk.entrySet()) {
			if(i==0) {
				query3 += " T1."+entry.getKey()+" AS "+entry.getKey();
			}
			else {
				query3 += ", T1."+entry.getKey()+" AS "+entry.getKey();
			}
        }
		query3 += ", A1.END_DATE AS START_DATE, A2.START_DATE AS END_DATE from "+tables[0][0]+" AS T1, "+tables[1][0]+" AS A1, " + tables[1][0]+" AS A2 where A1."+
				tables[1][1]+"= "+ref_FK+" AND A2." + tables[1][1] +"= "+ref_FK+ 
				" AND A1."+tables[1][1]+"= T1."+tables[0][1] + 
				" AND A2."+ tables[1][1]+"= T1."+tables[0][1] +
				" AND A1.END_DATE < A2.START_DATE AND T1.START_DATE < A1.END_DATE AND A2.START_DATE < T1.END_DATE";
		for(Map.Entry<String,String> entry:pk.entrySet()) {
			query3 += " AND T1."+entry.getKey()+" = "+entry.getValue();
        }
		query3 += " AND NOT EXISTS ( SELECT * FROM "+tables[1][0]+" AS A3 WHERE T1."+tables[0][1]+ "= A3." + tables[1][1] +" AND "
				+ "A3."+tables[1][1]+"=" + ref_FK + " AND A1.END_DATE < A3.END_DATE AND A3.START_DATE < A2.START_DATE )";
		
		String query4 = "select ";
		i = 0;
		for(Map.Entry<String,String> entry:pk.entrySet()) {
			if(i==0) {
				query4 += entry.getKey();
			}
			else {
				query4 += ", "+entry.getKey();
			}
        }
		query4 += ", START_DATE, END_DATE from "+tables[0][0]+" AS T1 WHERE ";
		i=0;
		for(Map.Entry<String,String> entry:pk.entrySet()) {
			if(i==0) {
				query4 += "T1."+entry.getKey()+" = "+entry.getValue();
			}
			else {
				query4 += " AND T1."+entry.getKey()+" = "+entry.getValue();
			}
        }
		query4 += " AND NOT EXISTS ( SELECT * FROM "+tables[1][0]+" AS A3 WHERE T1."+tables[0][1]+"=A3."+tables[1][1]+" AND "
				+ "A3."+tables[1][1]+"="+ref_FK+" AND T1.START_DATE < A3.END_DATE AND A3.START_DATE < T1.END_DATE )";
		
		String query = "(" + query1 + ") UNION (" + query2 + ") UNION (" + query3 + ") UNION (" + query4 + ")"; 
		
		ArrayList<Map<String,String>> result = new ArrayList<Map<String,String>>();
		
		System.out.println(query);
        resultSet = null;
		try {
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            while(resultSet.next()) {
                Map<String,String> row = new HashMap<String,String>();
                for(Map.Entry<String,String> entry:pk.entrySet()) {
        			row.put(tables[0][0]+"."+entry.getKey(),resultSet.getString(entry.getKey()));
                }
                row.put("START_DATE",resultSet.getString("START_DATE"));
                row.put("END_DATE",resultSet.getString("END_DATE"));
    			result.add(row);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return result;
	}

    public void closeConnection() {
    	try {
    		if(connection!=null) {
    			connection.close();
    		}
    	}
    	catch(SQLException s) {
    		s.printStackTrace();
    	}
    }
}