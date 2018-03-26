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
            System.out.println("Driver Not Found: " + e);
        }

        String url = "jdbc:mysql://127.0.0.1:3306/"+schema+"?useSSL=true";
        try
        {
            connection = (Connection)DriverManager.getConnection(url, username, password);
            System.out.println("Successfully Connected to Database");
            
        }
        catch(SQLException e)
        {
            System.out.println("SQL Exception: " + e); 
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
    
    public ArrayList<String> getPrimaryKey(String table) {
    	String query = "select COLUMN_NAME, COLUMN_TYPE FROM information_schema.COLUMNS where TABLE_SCHEMA = ? "
    			+ "and TABLE_NAME = ? and COLUMN_KEY = 'PRI'";
		ArrayList<String> pk_and_type = new ArrayList<String>();
    	
    	try {
    		statement = connection.prepareStatement(query);
    		statement.setString(1,schema);
    		statement.setString(2,table);
    		resultSet = statement.executeQuery();
    		
    		while(resultSet.next()) {
    			pk_and_type.add(resultSet.getString("COLUMN_NAME"));
    			pk_and_type.add(resultSet.getString("COLUMN_TYPE"));
    			return pk_and_type;
        	}
    	}
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	return null;
    }
    
    public void copy_data(String table,String hist_table,String pk,Map<String, String> col) {
		String query = "INSERT INTO " + hist_table + "(" + pk;
				
		for(Map.Entry<String,String> entry:col.entrySet()) {
			query += "," + entry.getKey();
		}
		query += ") SELECT " + pk;
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
    
    public String create_table(String table,String hist_table,ArrayList<String> pk,Map<String, String> col) {
    	String query = "CREATE TABLE IF NOT EXISTS " + hist_table + "(" + pk.get(0) + " " + pk.get(1);
		for(Map.Entry<String,String> entry:col.entrySet()) {
			query += "," + entry.getKey() + " " + entry.getValue();
		}
		query += ", START_DATE DATETIME DEFAULT NOW(), END_DATE DATETIME, PRIMARY KEY(" + pk.get(0);
    	for(Map.Entry<String,String> entry:col.entrySet()) {
			query += "," + entry.getKey();
		}
		query += ",START_DATE))";
		//System.out.println(query);
    	try {
    		statement = connection.prepareStatement(query);
    		statement.execute();
    		copy_data(table,hist_table,pk.get(0),col);
    		return "success";
    	}
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	return "failure";
    }
    
    public void onInsert_Trigger(String table,String hist_table,String pk,Map<String,String> col) {
    	String query = "create trigger after_" + table + "_insert after insert on " +
    			table + " for each row begin insert into " + hist_table + " set " + pk + "= NEW." + pk;
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
    }
    
    public void onUpdate_Trigger(String table,String hist_table,String pk,Map<String,String> col) {
    	String query = "create trigger after_" + table + "_insert after insert on " +
    			table + " for each row begin insert into " + hist_table + " set " + pk + "= NEW." + pk;
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
    }

	public void onDelete_Trigger(String table,String hist_table,String pk,Map<String,String> col) {
		String query = "create trigger after_" + table + "_insert after insert on " +
    			table + " for each row begin insert into " + hist_table + " set " + pk + "= NEW." + pk;
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
	}
    
    public void add_FK_constraint(String ref_table,String table,String pk,Map<String,String> col) {
    	String query = "ALTER TABLE " + table + " ADD CONSTRAINT FOREIGN KEY(" + pk 
    			+ ") REFERENCES " + ref_table + "(" + pk + ");";
    	try {
    		//System.out.println(query);
    		statement = connection.prepareStatement(query);
    		statement.execute();
    		
    		onInsert_Trigger(ref_table,table,pk,col);
    		onUpdate_Trigger(ref_table,table,pk,col);
    		onDelete_Trigger(ref_table,table,pk,col);
    	}
    	catch(SQLException e){
    		e.printStackTrace();
    	}
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