package dbResource;

import java.util.ArrayList;
import java.util.Map;

public class Temporal_Join {
	public String[][] join_info; 
	public String type = "normal";
	int i,j;
	
	public void initiate_join(String[][] info) {
		i = 0;
		j = 0;
		if(Static_Temporalize.hist_tables.contains(info[0][0])) {
			if(DatabaseConnection.getInstance().getTempColumns(info[0][0]).containsKey(info[0][1])) {
				info[0][0] = "hist_" + info[0][0];
				i = 1;
			}
		}
		if(Static_Temporalize.hist_tables.contains(info[1][0])) {
			if(DatabaseConnection.getInstance().getTempColumns(info[1][0]).containsKey(info[1][1])) {
				info[1][0] = "hist_" + info[1][0];
				j = 1;
				if(i==1) {
					type = "temporal";
				}
			}
		}
		join_info = info;
	}
	
	public ArrayList<String> getJoinColumns(){
		ArrayList<String> join_col = new ArrayList<String>();
		Map<String,String> T1_col = DatabaseConnection.getInstance().getJoinColumns(join_info[0][0]);
		for(Map.Entry<String,String> entry:T1_col.entrySet()) {
			join_col.add(join_info[0][0]+"."+entry.getKey());
		}
		Map<String,String> T2_col = DatabaseConnection.getInstance().getJoinColumns(join_info[1][0]);
		for(Map.Entry<String,String> entryVal:T2_col.entrySet()) {
			join_col.add(join_info[1][0]+"."+entryVal.getKey());
		}
		return join_col;
	}
	
	public ArrayList<Map<String,String>> perform_join(ArrayList<String> col) {
		if(i==1) {
			col.add(join_info[0][0]+".START_DATE");
			col.add(join_info[0][0]+".END_DATE");
		}
		if(j==1) {
			col.add(join_info[1][0]+".START_DATE");
			col.add(join_info[1][0]+".END_DATE");
		}
		return DatabaseConnection.getInstance().join_tables(join_info,type,col);
	}

	public ArrayList<Map<String, String>> perform_overlaps(ArrayList<String> col, Map<String, String> entered_val) {
		if(i==1) {
			col.add(join_info[0][0]+".START_DATE");
			col.add(join_info[0][0]+".END_DATE");
		}
		if(j==1) {
			col.add(join_info[1][0]+".START_DATE");
			col.add(join_info[1][0]+".END_DATE");
		}
		return DatabaseConnection.getInstance().Overlaps_Operator(join_info,col,entered_val);
	}
}

