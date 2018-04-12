# Temporal_event_data_store

Storing events as Data items relating to time instances

Tasks Accomplished:

->Given a schema, user selects a set of tables that have to be temporalised
->For each of the selected tables, user selects a set of columns that have to be temporalized
->Static Mapping : History tables are created (as hist_R) for each of the selected tables (R) whose columns are columns from the original R table 
(selected by the user), START_DATE and END_DATE
->Data is copied from R to hist_R with START_DATE = NOW() i.e, current timestamp and END_DATE as NULL (assumed instead of infinity)
->Dynamic Mapping : Created triggers for after insert, after delete, after update on R table
     1. after_table_insert : Once a row is inserted into transaction tables, this trigger is triggered 	      
     which inserts a row into the corresponding history table with START_DATE = NOW() and END_DATE = NULL
	   2. after_table_delete : Once a row is deleted from transaction table, this trigger is triggered
     which automatically updates the value for END_DATE for the latest row in corresponding history table. 
	   3. after_table_update : Once a row is updated in the transact table, END_DATE for the latest row corresponding to the row
     that has been updated is set equal to NOW() and a new row is inserted into the history table with END_DATE as null 
     and START_DATE as NOW().  
 ->Added CRUD operations - Insert, Update, Delete, Select on R table. 
->For each of the above CRUD operations, corresponding triggers are automatically fired, which updates the history tables 
automatically.
Progress in the week 1/4 – 7/4:

-> Replaced radio buttons with checkboxes wherever multiple options have to be selected
-> Removed foreign key constriant on history table columns to original snapshot table
-> Removed primary key constraint on history table and replaced it with unique key
-> Trigger Queries are written to a file for future reference
-> Added temporal operations
	-> FIRST - getFirst() : Indicates the first value of the column and its timestamp. Select the table and column, provide PK values for the table. Select the column value , start_date and end_date from history table with given PK value start date is minimum of the start dates of all the rows for the given PK values. Query : select column_value, start_date, end_date from hist_table where Pk = PK_values’ and start_date = (select min(start_date) where pk = ‘pk_values’)
	->  LAST - getLast() : Indicates the last value of the column and its timestamp. Select the and column, provide PK values for the table. Select the column value, start_date and end_date from history table with given PK value start date is minimum of the start dates of all the rows for the given PK values where value is latest value for the given PK values. Latest value for the column is found selecting column value whose start date is maximum of all the rows with given Pk values.
	-> PREVIOUS – getPrevious() - Indicates the value which precedes the given value, and its timestamp(in DATETIME format). For the rows corresponding to the specified Pk values, pick those whose start date is less than the start date of given value. Of these, select the row corresponding to the maximum start date out of these.
	-> NEXT – getNext() - Value that follows the given value 'cVal' along with its timestamp. For the rows corresponding to the specified Pk values, pick those whose start date is greater than the start date of given value. Of these, select the row corresponding to the minimum start date out of these.
	-> PREVIOUS_SCALE – getPrevious_Scale() - Indicates the value which precedes the current value and its timestamp, according to the specified granule. Granule is taken as input like dd/mm/yy. The previous value valid before specified duration is computed by using DATE_ADD() and DATE_DIFF functions of mysql
	-> NEXT_SCALE – getNext_Scale() - Indicates the value which precedes the current value and its timestamp, according to the specified granule. Granule is taken as input like dd:mm:yy. The previous value valid before specified duration is computed by using DATE_ADD() and DATE_DIFF() functions of mysql
	-> EVOLUTION HISTORY – getEvolution_History() - Indicates all the evolution dates of the column. Select all the distinct column values, start date	 starting from the time when the row(with given PK values) is entered into the table till the latest value.
	-> EVOLUTION – getEvolution() - Currently returning the start and end date of the given value for given PK values in date format.
	-> FIRST EVOLUTION – getFirst_Evolution()- Indicates the first evolution date of the col. The timestamp(in date format) when the column was first changed
	-> LAST EVOLUTION – getLast_Evolution()- Indicates the last evolution date of the col. The timestamp(in date format) when the column was last changed
	-> EVOLUTION Val1-Val2 – getEvolutionVal12 - Indicates the evolution date from ‘Val1’ to ‘Val2’. Get all the rows whose start_date is in between start dates of Val1 and Val2. Returns only when Val2 comes after Val1 in the history table. 
	-> TIMESTAMPS – getTimestamps() - Indicates the timestamp(in DATETIME format) associated to the value ‘val’
	-> COLUMN value - Indicates the value associated with a date specified somewhere in the query. Given a date, get the value valid at that time
	-> COLUMN.timestamp_name - Indicates the timestamp associated with a value specified somewhere in the query. Taking timestamp_name as input – date/year/month and returning the corresponding timestamp of start_date and end_date in the required format.

Assumptions:
	Every selected table to temporalize contains atleast one primary key – user given or 	autoincrement
