import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

//Initial window
public class TemporalWelcome{
	String username, pwd;
	DatabaseConnection connection;
	Scanner sc = new Scanner(System.in);
	String schema;
	ArrayList<String> TempTables = new ArrayList<String>();
	Map<String, Map<String,String> > TempColumnNames = new HashMap<String, Map<String,String> >();
	
	public static void main(String[] args){
		TemporalWelcome tempDB = new TemporalWelcome();
		
		tempDB.Login();
		tempDB.chooseTempTables();
		tempDB.chooseTempColumns();
		
		tempDB.connection.closeConnection(); //closing the database connection
		
		tempDB.staticTemporalize();
		
		return;
	}
	
	public void Login() {
		System.out.println("Welcome to Temporal Event Data Store\n"
				+ "Please Login into the Database\n");
		System.out.print("Enter Username : ");
		username = sc.nextLine();
		System.out.print("Enter Password : ");
		pwd = sc.nextLine();
		System.out.print("Provide name of the schema : ");
		schema = sc.nextLine();
		connection = new DatabaseConnection(username,pwd,schema);
		return;
	}
	
	public void chooseTempTables(){
		System.out.println("The tables available are : ");
		ArrayList<String> tableNames = new ArrayList<String>();
		tableNames = connection.getTables();

		for(int i=0;i<tableNames.size();i++) {
			System.out.println(i+1 + " : " + tableNames.get(i));
		}
		System.out.println("0 : Exit");
		
		int choice = 1;
		System.out.println("Select the tables (the no:s opposite to the names) "
				+ "you want to temporalise : ");
		while(choice != 0) {
			choice = sc.nextInt();
			if(choice!=0) {
				TempTables.add(tableNames.get(choice-1));
			}
		}
		return;
	}
	
	public void chooseTempColumns(){
		Map<String, Map<String,String> > ColumnNames = new HashMap<String, Map<String, String> >();
		
		for(int i=0;i<TempTables.size();i++) {
			ColumnNames.put(TempTables.get(i), connection.getColumns(TempTables.get(i)));
		}	
		
		for(Map.Entry<String, Map<String,String> > entry:ColumnNames.entrySet()) {
			String key = entry.getKey();
			Map<String,String> value = entry.getValue();
			
			for(Map.Entry<String,String> entryVal:value.entrySet()) {
				System.out.println(entryVal.getKey());
			}
			System.out.println("exit");
			
			System.out.println("Select the column names of '" +
							  key + "' you want to temporalise : ");
			
			String tempCol = "Start";
			Map<String,String> tempCols = new HashMap<String,String>();
			while(!tempCol.equals("exit")) {
				tempCol = sc.next();
				if(!tempCol.equals("exit")) {
					tempCols.put(tempCol, value.get(tempCol));
				}
			}
			TempColumnNames.put(key, tempCols);
		}
		return;
	}
	
	public void staticTemporalize() {
		 Static_Temporalize.Execute(username, pwd, schema, TempColumnNames);
	}
	
}