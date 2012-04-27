
package solver;
import java.util.Vector;


public class SQLCommands {
	public String getSQLCommandSelectWhere(String tableName, String column, String ComparingTo){
		    String cmd = "SELECT * FROM " + tableName + " WHERE " + column + " = " + "'" + ComparingTo + "'";
		    return cmd;
	}
	public String	 getSQLCommandSelectDoubleWhere(String tableName, String firstColumn, String firstCondition, String firstComparingTo, String secondColumn, String secondCondition, String secondComparingTo ){
		    String cmd = "SELECT * FROM " + tableName + " WHERE " + firstColumn + " " + firstCondition + " " + "" + firstComparingTo + " and " + secondColumn + " " + secondCondition + " " + "" + secondComparingTo + "";
		    return cmd;
	}
	public String getSQLCommandSelectBetween(String tableName, String column, String firstValue, String secondValue){
		    String cmd = "SELECT * FROM " + tableName + " WHERE " + column + " BETWEEN " + " " + firstValue + " and " + secondValue;
		    return cmd;
	}
	
	public String selectAllWhere(String tableName, String column, String Condition) {
		    String cmd = "SELECT * FROM " + tableName + " WHERE " + column + " = " + "'" + Condition + "'";
		    return cmd;
	}
	
	public String dropTable(String tableName) {
		    String cmd = "DROP TABLE " + tableName;
		    return cmd;
	}
	public String selectFrom(String tableName, Vector<String> columns) {
		String cmd;
		if(columns.get(0) == "*"){
		        cmd = "SELECT * FROM " + tableName;
		}
	    else{
	        cmd = "SELECT ";
	        for(int i = 0 ; i < columns.size(); i++){
	            if(i > 0){
	                cmd += ",";
	            }
	            cmd += "'";
	            cmd += columns.get(i);
	            cmd += "'";
	        }
	        cmd += " FROM " + tableName;
	    }
		return cmd;
	}

	    public String createTable(String tableName, Vector<String> fields){
		    String cmd = "CREATE TABLE " + tableName + " (";
		    for(int i = 0 ; i < fields.size(); i++){
		    	if(i > 0){
		    		cmd += ", ";
		    	}
		    	cmd += fields.get(i);
		    }
		    cmd += ")";
		    return cmd;	    
	    }

	    public String insertToTable(String tableName, Vector<String> fields){
		    String cmd = "INSERT INTO " + tableName +" VALUES (";
		    for (int i = 0 ; i < fields.size(); i ++) {
		        if(i > 0) {
		            cmd += ",";
		        }
		        cmd += "'";
		        cmd += fields.get(i);
		        cmd += "'";
		    }
		    cmd += ")";
		    return cmd;
	    }

	    public String insertToTableNonAuto(String tableName, String id, String rowsToChange, String directions, String movements){
		    String cmd = "INSERT INTO " + tableName +" VALUES ('" + id + "', '" + rowsToChange + "', '" + directions + "', '" + movements + "')";
		    return cmd;
	    }
}