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
