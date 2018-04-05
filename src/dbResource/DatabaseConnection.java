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
            System.out.println("Driver Found");
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
            System.out.println("Successfully Connected to Database");
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
    	String query = "CREATE TABLE IF NOT EXISTS " + hist_table + "(" ;
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
		for(Map.Entry<String,String> entry:col.entrySet()) {
			query += "," + entry.getKey() + " " + entry.getValue();
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
    	for(Map.Entry<String,String> entry:col.entrySet()) {
			query += "," + entry.getKey();
		}
		query += ",START_DATE))";
		//System.out.println(query);
    	try {
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
    		statement = connection.prepareStatement(query);
    		statement.execute();
    	}
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	return query;
    }
    
    public String onUpdate_Trigger(String table,String hist_table,Map<String,String> pk,Map<String,String> col) {
    	String query = "create trigger after_" + table + "_update after update on " +
    			table + " for each row begin update "+ hist_table + " set END_DATE = NOW() where "; 
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
    		statement = connection.prepareStatement(query);
    		statement.execute();
    	}
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	return query;
    }

	public String onDelete_Trigger(String table,String hist_table,Map<String,String> pk,Map<String,String> col) {
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
    			query += "," + entry.getKey() + "= OLD." + entry.getKey();
    		}
    	}
    	query += " and END_DATE is NULL; END";
    	//System.out.println(query);
    	try {
    		statement = connection.prepareStatement(query);
    		statement.execute();
    	}
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	return query;
	}
    
    public void add_Triggers(String table,String hist_table,Map<String,String> pk,Map<String,String> col){
   		String insert_trigger = onInsert_Trigger(table,hist_table,pk,col);
   		String update_trigger = onUpdate_Trigger(table,hist_table,pk,col);
   		String delete_trigger = onDelete_Trigger(table,hist_table,pk,col);
   		
   		File file = new File("/home/deepika/eclipse-workspace/Temporal_Event_Store/triggers_queries.txt");
	   	try {
			file.createNewFile();
			FileWriter output = new FileWriter(file);
			output.write(insert_trigger + "\n");
			output.write(update_trigger + "\n");
			output.write(delete_trigger + "\n");
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
    public ResultSet getFirst(Map<String,String> pk, String table, String column) {
    	String query = "SELECT "+column+",START_DATE, END_DATE FROM hist_" + table + " WHERE ";
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
    	resultSet = null;
    	try {
    		statement = connection.prepareStatement(query);
    		resultSet = statement.executeQuery();
    	} 
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	
		return resultSet;
    }
    
  //latest status of the column of the specified row with given pk values 
    public ResultSet getLast(Map<String,String> pk, String table, String column) {
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
    	
    	query2 += "AND "+column+" in ("+query1+")";
    	
    	String query = "SELECT DISTINCT "+column+" START_DATE, END_DATE FROM hist_"+table+" WHERE ";
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
    	query += " AND START_DATE = ("+query2+") AND "+column+" in "+query1;
    	resultSet = null;
    	try {
    		statement = connection.prepareStatement(query);
    		resultSet = statement.executeQuery();
    	} 
    	catch(SQLException e){
    		e.printStackTrace();
    	}
		return resultSet;
    }
    
    //Value that precedes the given value 'cVal' along with its START_DATE
    public ResultSet getPrevious(Map<String,String> pk, String table, String column, String cVal) {
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
    	resultSet = null;
    	try {
    		statement = connection.prepareStatement(query);
    		resultSet = statement.executeQuery();
    	} 
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	return resultSet;
    }
    
    //Value that follows the given value 'cVal' along with its START_DATE
    public ResultSet getNext(Map<String,String> pk, String table, String column, String cVal) {
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
    	resultSet = null;
    	try {
    		statement = connection.prepareStatement(query);
    		resultSet = statement.executeQuery();
    	} 
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	return resultSet;
    }
    
    
    //Indicates the value which precedes the current value and its timestamp, according to the specified day
    public String getPrevious_SCALE(Map<String,String> pk, String table, String column, int yy, int mm, int dd) {
    	String query = "SELECT "+column+" FROM hist_"+table+"WHERE ";
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
    	query += " AND START_DATE <= (SELECT DATE_DIFF(DATE_DIFF(DATE_DIFF(NOW(),INTERVAL "+yy+" YEAR)"
				+ ", INTERVAL "+mm+" MONTH), INTERVAL "+dd+" DAY)) AND "
					+ "END_DATE > (SELECT DATE_DIFF(DATE_DIFF(DATE_DIFF(NOW(),INTERVAL "+yy+" YEAR)"
						+", INTERVAL "+mm+" MONTH), INTERVAL "+dd+" DAY))";
    	resultSet = null;
    	String colVal = null;
    	try {
    		statement = connection.prepareStatement(query);
    		resultSet = statement.executeQuery();
    		colVal = resultSet.getString(0);
    	} 
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	return colVal;
    }
    
    //Indicates the value which follows the current value and its timestamp, according to the specified day
    public String getNext_SCALE(Map<String,String> pk, String table, String column, int yy, int mm, int dd) {
    	String query = "SELECT "+column+" FROM hist_"+table+"WHERE ";
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
    	query += " AND START_DATE <= (SELECT DATE_ADD(DATE_ADD(DATE_ADD(NOW(),INTERVAL "+yy+" YEAR)"
    				+ ", INTERVAL "+mm+" MONTH), INTERVAL "+dd+" DAY)) AND "
    					+ "END_DATE > (SELECT DATE_ADD(DATE_ADD(DATE_ADD(NOW(),INTERVAL "+yy+" YEAR)"
    						+", INTERVAL "+mm+" MONTH), INTERVAL "+dd+" DAY))";
    	resultSet = null;
    	String colVal = null;
    	try {
    		statement = connection.prepareStatement(query);
    		resultSet = statement.executeQuery();
    		colVal = resultSet.getString(0);
    	} 
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	return colVal;
    }
    
    //Indicates all the evolution dates of the column
    public ResultSet getEvolution_History(Map<String,String> pk, String table, String column) {
    	String query = "SELECT DISTINCT "+column+", START_DATE FROM hist_"+table+" WHERE ";
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
    	resultSet = null;
    	try {
    		statement = connection.prepareStatement(query);
    		resultSet = statement.executeQuery();
    	} 
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	
    	return resultSet;
    }
    
    //Indicates the evolution date to the given value
    public ResultSet getEvolution(Map<String,String> pk, String table, String column, String val) {
    	String query = "SELECT "+column+", START_DATE, END_DATE FROM hist_" + table + " WHERE ";
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
    	query += "AND "+column+"='"+val+"' ) AND "+column+"='"+val+"'";
    	resultSet = null;
    	try {
    		statement = connection.prepareStatement(query);
    		resultSet = statement.executeQuery();
    	} 
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	return resultSet;
    }
    
    //Indicates the first evolution date of the column
    public ResultSet getFirst_Evolution(Map<String,String> pk, String table, String column) {
    	String query = "SELECT "+column+", START_DATE, END_DATE FROM hist_" + table + " WHERE ";
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
    	resultSet = null;
    	try {
    		statement = connection.prepareStatement(query);
    		resultSet = statement.executeQuery();
    	} 
    	catch(SQLException e){
    		e.printStackTrace();
    	}
		return resultSet;
    }
    
    //Indicates the last evolution date of the column
    public ResultSet getLast_Evolution(Map<String,String> pk, String table, String column) {
    	String query = "SELECT "+column+", START_DATE, END_DATE FROM hist_" + table + " WHERE ";
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
    	resultSet = null;
    	try {
    		statement = connection.prepareStatement(query);
    		resultSet = statement.executeQuery();
    	} 
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	return resultSet;
    }
    
  //Indicates the evolution date from ‘val1’ to ‘val2’.
    public ResultSet evolutionVal12(Map<String,String> pk, String table, String column, String val1, String val2) {
    	//to retrieve the start_date of val1
    	String query_val1 = "SELECT MAX(START_DATE) FROM hist_"+table+" WHERE ";
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
    		date_val1 = resultSet.getString(0);
    		
    		resultSet=null;
    		statement = connection.prepareStatement(query_val2);
    		resultSet = statement.executeQuery();
    		date_val2 = resultSet.getString(0);
    		
    		if(date_val2.compareTo(date_val1)>=0) {
    			String query = "SELECT DISTINCT"+column+", START_DATE, END_DATE FROM "+table+" WHERE ";
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
    	    	statement = connection.prepareStatement(query);
        		resultSet = statement.executeQuery();
        		
    		}
    		
    	} 
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	return resultSet;
    	
    }
    
    //Indicates the timestamp associated to the value ‘val’
    public ResultSet getTimestamps(Map<String,String> pk, String table, String column, String val) {

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
    	query += " AND "+column+"='"+val+"'";
    	resultSet = null;
    	try {
    		statement = connection.prepareStatement(query);
    		resultSet = statement.executeQuery();
    	} 
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	return resultSet;
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
    	query += " AND START_DATE <= "+date+" AND END_DATE >= "+date;
    	resultSet = null;
    	String colVal = null;
    	try {
    		statement = connection.prepareStatement(query);
    		resultSet = statement.executeQuery();
    		colVal = resultSet.getString(0);
    	} 
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	return colVal;
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