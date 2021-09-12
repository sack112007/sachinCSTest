package customLogs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pojo.EventDetails;

public class DatabaseOperations {
	static Logger logger = LoggerFactory.getLogger("DatabaseOperations");

	private Connection con = null;
	private Statement stmt = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	private String tabelName;

	private void setTable() throws FileNotFoundException, IOException {
		this.tabelName = Config.getProperty("db.tableName");
	}

	public DatabaseOperations connectDatabase() {
		try {
			Class.forName("org.hsqldb.jdbc.JDBCDriver");
			con = DriverManager.getConnection(Config.getProperty("db.url"), Config.getProperty("db.user"),
					Config.getProperty("db.password"));
			if (con != null) {
				logger.debug("Connection created successfully");

			} else {
				logger.debug("Problem with creating connection");
			}
			setTable();
		} catch (Exception e) {
			logger.error("Problem with creating connection");
			e.printStackTrace();
		}
		return this;
	}

	public void insertEvents(List<EventDetails> eventsList) throws SQLException {
		logger.info("Inserting finished events into database...");
		connectDatabase();
		if (!checkTableExist()) {
			createTable();
		}
		preparedStatement = con.prepareStatement("INSERT INTO " + this.tabelName + " VALUES(?,?,?,?,?)");

		for (EventDetails eventDetails : eventsList) {
			preparedStatement.setString(1, eventDetails.getId());
			preparedStatement.setString(2, eventDetails.getType());
			preparedStatement.setString(3, eventDetails.getHost());
			preparedStatement.setInt(4, eventDetails.getDuration());
			preparedStatement.setBoolean(5, eventDetails.isAlert());
			preparedStatement.executeUpdate();
			con.commit();
		}
		releaseConnection();
		logger.info("Successfully inserted events into databse");
	}

	private void releaseConnection() throws SQLException {
		if (con != null)
			con.close();
		if (resultSet != null)
			resultSet.close();
		if (stmt != null)
			stmt.close();
		if (preparedStatement != null)
			preparedStatement.close();
	}

	public boolean checkTableExist() throws SQLException {
		DatabaseMetaData meta = con.getMetaData();
		resultSet = meta.getTables(null, null, this.tabelName.toUpperCase(), new String[] { "TABLE" });
		return resultSet.next();
	}

	public void createTable() {
		logger.info("Creating Table:{}", tabelName);
		try {
			stmt = con.createStatement();
			stmt.executeUpdate("CREATE TABLE " + this.tabelName
					+ " (Id VARCHAR(20) NOT NULL, Type VARCHAR(50), Host VARCHAR(50), Duration INT,Alert Boolean);");
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		logger.info("Table Created Successfully");

	}

}
