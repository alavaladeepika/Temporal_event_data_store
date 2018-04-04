import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.*;

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