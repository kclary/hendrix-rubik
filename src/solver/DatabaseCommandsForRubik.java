package solver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;

import cube.Direction;
import cube.Update;


public class DatabaseCommandsForRubik {
	Statement stat;
	SQLCommands commandClass;
	
	String solutionsTable;
	
	public DatabaseCommandsForRubik() {
		solutionsTable = "solutions";
		
		commandClass = new SQLCommands();
		try {
			createConnection();
        } catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}
	
	private void printEverythingInADatabase(String tableName) {
		Vector<String> fieldsForRubTableSelect = new Vector<String>();
		fieldsForRubTableSelect.add("*");
		String cmd = commandClass.selectFrom(tableName, fieldsForRubTableSelect);

		try {
			stat.execute(cmd);
			ResultSet results = stat.getResultSet();
			while (results.next()) {
                for (int c = 1; c <= 4; ++c) {
                    System.out.print("printed\t" + results.getString(c) + "\t");
                }
                System.out.println();
            } 
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void addASingleFinalPosition(StateOfColors colorSetup, Vector<Update> updates, int startingPos) {
		
		String nameID = colorSetup.getID();
		
		String indices = "";
		StringBuilder stringBuilderIndices = new StringBuilder(indices);
		
		String movements = "";
		StringBuilder stringBuilderMovementTypes = new StringBuilder(movements);
		
		
		String directions = "";
		StringBuilder stringBuilderDirections = new StringBuilder(directions);
		for(int i = startingPos; i < updates.size(); i++) {
			Update curUpdate = updates.get(i);
			int indexToChange = curUpdate.getRow();
			Direction columnOrRowMovement = curUpdate.getDir();
			String colString;
			if(columnOrRowMovement == Direction.Y) {
				colString = "Y";
			} else {
				colString = "Z";
			}
			int direction = curUpdate.getD();
			
			stringBuilderIndices.append(indexToChange);
			stringBuilderDirections.append(direction);
			stringBuilderMovementTypes.append(colString);
			
			stringBuilderIndices.append(":");
			stringBuilderDirections.append(":");
			stringBuilderMovementTypes.append(":");
		}
		
		String indicesFinal = stringBuilderIndices.toString();
		String directionsFinal = stringBuilderDirections.toString();
		String movementTypeFinal = stringBuilderMovementTypes.toString();
		
		String cmd = commandClass.insertToTableNonAuto(solutionsTable, nameID, indicesFinal, directionsFinal, movementTypeFinal);
		
		try {
			stat.execute(cmd);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addAllSolutionsToDatabase(Vector<StateOfColors> colors, Vector<Update> solutionWithoutDatabase) {
		for(int i = 0 ; i < solutionWithoutDatabase.size(); i++) {

			StateOfColors curColors = colors.get(i);
			if (!databaseContainsID(curColors.getID())) {
				addASingleFinalPosition(curColors, solutionWithoutDatabase, i);				
			}
        }
	}

	private void createConnection() throws ClassNotFoundException, SQLException  {
		Class.forName("org.sqlite.JDBC");
        Connection con = DriverManager.getConnection("jdbc:sqlite:testdb");
        stat = con.createStatement();
	}

	private void createSolutionsTables() {
       
		String cmd2 = commandClass.dropTable(solutionsTable);
        try {
			stat.execute(cmd2);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        String One = "ID VARCHAR(40)";
        String Two = "Indices VARCHAR(40)";
        String Three = "Directions VARCHAR(40)";
        String Four = "Movements VARCHAR(40)";
        
        Vector<String> fieldsForSolutionsCreation = new Vector<String>();
        
        fieldsForSolutionsCreation.add(One);
        fieldsForSolutionsCreation.add(Two);
        fieldsForSolutionsCreation.add(Three);
        fieldsForSolutionsCreation.add(Four);
        
        String cmdSolutions = commandClass.createTable(solutionsTable, fieldsForSolutionsCreation);
        try {
        	stat.execute(cmdSolutions);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean databaseContainsID( String idChecking ) {
		Vector<String> fieldsForRubTableSelect = new Vector<String>();
		fieldsForRubTableSelect.add("*");
		String cmd = commandClass.selectAllWhere(solutionsTable, "ID", idChecking);
		try {
			stat.execute(cmd);
			ResultSet results = stat.getResultSet();
			if(results.next()){
				stat.close();
				results.close();
				return true;				
			} else{
				stat.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public Vector<Update> getEndingUpdatesFromDatabase(String curID) {
		Vector<String> solutions = getSolutionMovementsforID(solutionsTable, "ID", curID);
		String ID = solutions.get(0);
		String indices = solutions.get(1);
		String movementDirection = solutions.get(2);
		String columnOrRowMovement = solutions.get(3);
		Vector<Update> databaseUpdates = breakUpAndMakeIntoUpdates(indices, movementDirection, columnOrRowMovement);
		return databaseUpdates;
	}
	
	private Vector<String> getSolutionMovementsforID(String tableName, String position, String idChecking) {
		Vector<String> fieldsForSolutionsTableSelect = new Vector<String>();
		fieldsForSolutionsTableSelect.add("*");
		String cmd = commandClass.selectAllWhere(tableName, position, idChecking);
		ResultSet results = null;
		Vector<String> resultsVector = new Vector<String>();
		try {
			if (stat.execute(cmd)) {
                results = stat.getResultSet();
                while (results.next()) {
                	resultsVector.add(results.getString(1));
                	resultsVector.add(results.getString(2));
                	resultsVector.add(results.getString(3));
                	resultsVector.add(results.getString(4));
                }
            }
			results = stat.getResultSet();
			return resultsVector;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultsVector;
	}
	
	private Vector<Update> breakUpAndMakeIntoUpdates(String indices, String movementDirection, String columnOrRowMovement) {
		Vector<Update> updates = new Vector<Update>();
		String[] indicesArray = indices.split(":");
		String[] movementDirectionArray = movementDirection.split(":");
		String[] colOrRowTypeArray = columnOrRowMovement.split(":");
		for(int i = 0; i < indicesArray.length; i++) {
			String curIndex = indicesArray[i];
			int index = new Integer(curIndex);
			String curDirection = movementDirectionArray[i];
			String curType = colOrRowTypeArray[i];
			Direction curTypeDirection;

			if(curType.equalsIgnoreCase("Y")) {
				curTypeDirection = Direction.Y;
			} else if (curType.equalsIgnoreCase("Z")) {
				curTypeDirection = Direction.Z;
			} else {
				curTypeDirection = Direction.Z;
			}
			Update curUpdate = new Update(index, new Integer(curDirection), curTypeDirection);
			updates.add(curUpdate);
		}
		
		return updates;
	}
	
}