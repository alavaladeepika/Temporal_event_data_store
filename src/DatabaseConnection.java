import java.sql.*;
import java.util.*;

//For opening the Database Connection
public class DatabaseConnection{
	java.sql.PreparedStatement statement=null;
    ResultSet resultSet;
    Connection connection = null;
    //String query = null;
    String schema_name = null;

    public DatabaseConnection(String uname, String pwd, String schema){
    	String user = uname;
        String password = pwd;
        connection = null;

        try{
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver Found");
        }

        catch (ClassNotFoundException e){
            System.out.println("Driver Not Found: " + e);
        }

        schema_name = schema;
        String url = "jdbc:mysql://127.0.0.1:3306/"+schema+"?useSSL=true";
        try
        {
            connection = (Connection)DriverManager.getConnection(url, user, password);
            System.out.println("Successfully Connected to Database");
            
        }
        catch(SQLException e)
        {
            System.out.println("SQL Exception: " + e); 
        }                   

    }
    
    public ArrayList<String> getTables() {
    	ArrayList<String> tableNames = new ArrayList<String>();
    	String query = "select TABLE_NAME FROM information_schema.TABLES where TABLE_TYPE = 'BASE TABLE' and TABLE_SCHEMA = ?";
		
    	try {
    		statement = connection.prepareStatement(query);
    		statement.setString(1,schema_name);
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
    	String query = "select COLUMN_NAME, DATA_TYPE FROM information_schema.COLUMNS where TABLE_SCHEMA = ? "
    			+ "and TABLE_NAME = ? and COLUMN_KEY!='PRI' and DATA_TYPE!='enum'";
		
    	try {
    		statement = connection.prepareStatement(query);
    		statement.setString(1,schema_name);
    		statement.setString(2,tableName);
    		resultSet = statement.executeQuery();
    		
    		while(resultSet.next()) {
    			String key = resultSet.getString("COLUMN_NAME"); 
    			String value = resultSet.getString("DATA_TYPE");
    			
    			//Assuming the lengths of the strings doesn't exceed 20
    			if(value.equals("varchar")) { 
    				value = value + "(20)";
    			}
    			ColumnNames.put(key,value);
        	}
    	}
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	
    	return ColumnNames;
    }
    
    public ArrayList<String> getPrimaryKey(String table) {
    	String query = "select COLUMN_NAME, DATA_TYPE FROM information_schema.COLUMNS where TABLE_SCHEMA = ? "
    			+ "and TABLE_NAME = ? and COLUMN_KEY = 'PRI'";
		ArrayList<String> pk_and_type = new ArrayList<String>();
    	
    	try {
    		statement = connection.prepareStatement(query);
    		statement.setString(1,schema_name);
    		statement.setString(2,table);
    		resultSet = statement.executeQuery();
    		
    		while(resultSet.next()) {
    			pk_and_type.add(resultSet.getString("COLUMN_NAME"));
    			pk_and_type.add(resultSet.getString("DATA_TYPE"));
    			return pk_and_type;
        	}
    	}
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	return null;
    }
    
    public String create_table(String query) {
    	try {
    		statement = connection.prepareStatement(query);
    		statement.execute();
    		return "success";
    	}
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	return "failure";
    }
    
    public void add_FK_constraint(String ref_table,String table,String pk) {
    	String query = "ALTER TABLE " + table + " ADD CONSTRAINT FOREIGN KEY(" + pk 
    			+ ") REFERENCES " + ref_table + "(" + pk + ");";
    	try {
    		//System.out.println(query);
    		statement = connection.prepareStatement(query);
    		statement.execute();
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