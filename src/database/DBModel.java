package database;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by vincenthoang on 6/2/17.
 */
public class DBModel {

    private String mDBName;
    private String mTableName;
    private String[] mFieldNames;
    private String[] mFieldTypes;

    public DBModel(String dbName, String tableName, String[] fieldNames, String[] fieldTypes) throws SQLException {
        super();
        mDBName = dbName;
        mTableName = tableName;
        mFieldNames = fieldNames;
        mFieldTypes = fieldTypes;

        if (mFieldNames == null || mFieldTypes == null || mFieldNames.length == 0 || mFieldNames.length != mFieldTypes.length) {
            throw new SQLException("Database field names and types must not be null or mismatched lengths");
        }

        initializeTables();
    }


    /**
     * Creates tables based on the information passed from the Controller to DBModel
     * @throws SQLException Exception thrown in try-with resources
     */
    private void initializeTables() throws SQLException {
        StringBuilder createStatement = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        createStatement.append(mTableName).append("(");

        for (int i = 0, n = mFieldNames.length; i < n; i++) {
            createStatement.append(mFieldNames[i]).append(" ").append(mFieldTypes[i]).append((i < mFieldNames.length - 1) ? "," : ")");
        }

        try (Connection connection = connectToDB();
                Statement statement = connection.createStatement()) {
            statement.executeUpdate(createStatement.toString());
        }
    }

    /**
     * Connects to the database using SQLite JDBC
     * @return connection to database
     * @throws SQLException prints stack trace on SQLException
     */
    private Connection connectToDB() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + mDBName);
        return connection;
    }

    /**
     * Creates a new entry into an existing table in the database
     * @return the primary key of the newly inserted entry, or -1 if there was an error
     */
    public int createEntry(String[] fields, String[] values) throws SQLException {
        // Sanity Check
        if ( fields == null || values == null ||  fields.length == 0 ||  fields.length != values.length) {
            return -1;
        }

        StringBuilder insertStatement = new StringBuilder("INSERT INTO ");
        insertStatement.append(mTableName).append("(");

        for (int i = 0, n = fields.length; i < n; i++) {
            insertStatement.append(fields[i]).append((i < n - 1) ? "," : ") VALUES(");
        }
        for (int i = 0, n = values.length; i < n; i++) {
            String valuesSQL = convertToSQL(fields[i], values[i]);
            insertStatement.append(valuesSQL).append((i < n - 1) ? "," : ")");
        }

        try (Connection connection = connectToDB();
                Statement statement = connection.createStatement()) {
            statement.executeUpdate(insertStatement.toString());

            return statement.getGeneratedKeys().getInt(1);
        }
    }

    /**
     * Deletes a record, or all records, from the database with the provided key
     * @param key Primary key value to delete. "all" to delete all records from the table
     * @throws SQLException thrown if occurred
     */
    public void deleteEntry(String key) throws SQLException {
        try (Connection connection = connectToDB();
                Statement statement = connection.createStatement()) {
            if (key.equalsIgnoreCase("all")) {
                statement.executeUpdate("DELETE FROM " + mTableName);
            } else {
                statement.executeUpdate("DELETE FROM " + mTableName + " WHERE " + mFieldNames[0] + " = " + key);
            }
        }
    }

    /**
     * Gets all entries or a specific entry in the database
     * @param key the specific primary key ID of the entry or "all" for all entries from that table
     */
    public ArrayList<ArrayList<String>> getEntry(String key) throws SQLException {
        try (Connection connection = connectToDB();
             Statement statement = connection.createStatement();
            ResultSet rs = (key.equalsIgnoreCase("all")
                    ? statement.executeQuery("SELECT * FROM " + mTableName)
                    : statement.executeQuery("SELECT * FROM " + mTableName + " WHERE " + mFieldNames[0] + " = " + key))) {

            ArrayList<ArrayList<String>> returnValues = new ArrayList<>();
            while (rs.next()) {
                ArrayList<String> values = new ArrayList<>();
                for (String fieldName : mFieldNames) {
                    values.add(rs.getString(fieldName));
                }
                returnValues.add(values);
            }
            return returnValues;
        }
    }

    public int getEntryCount() throws SQLException {
        try (Connection connection = connectToDB();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM " + mTableName)) {
            int count = 0;
            while (rs.next()) {
             count++;
            }
            return count;
        }

    }

    public String convertToSQL(String field, String value) {
        for (int i = 0, n = mFieldNames.length; i < n; i++) {
            if (field.equalsIgnoreCase(mFieldNames[i])) {
                if (mFieldTypes[i].equalsIgnoreCase("TEXT") || mFieldTypes[i].equalsIgnoreCase("NVARCHAR")) {
                    return "'" + value + "'";
                }
            }
        }
        return value;
    }


}
