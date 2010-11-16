package com.j256.ormlite.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableInfo;

public class MysqlDatabaseTypeTest extends BaseJdbcDatabaseTypeTest {

	@Override
	protected void setDatabaseParams() throws SQLException {
		databaseUrl = "jdbc:mysql:ormlite";
		connectionSource = new JdbcConnectionSource(DEFAULT_DATABASE_URL);
		databaseType = new MysqlDatabaseType();
	}

	@Override
	protected boolean isDriverClassExpected() {
		return false;
	}

	@Override
	protected boolean isConnectionExpected() throws IOException {
		return false;
	}

	@Test
	public void testBoolean() throws Exception {
		TableInfo<AllTypes> tableInfo = new TableInfo<AllTypes>(databaseType, AllTypes.class);
		assertEquals(9, tableInfo.getFieldTypes().length);
		FieldType booleanField = tableInfo.getFieldTypes()[1];
		assertEquals("booleanField", booleanField.getDbColumnName());
		StringBuilder sb = new StringBuilder();
		List<String> additionalArgs = new ArrayList<String>();
		List<String> statementsBefore = new ArrayList<String>();
		databaseType.appendColumnArg(sb, booleanField, additionalArgs, statementsBefore, null, null);
		assertTrue(sb.toString().contains("TINYINT(1)"));
	}

	@Test
	public void testGeneratedIdBuilt() throws Exception {
		TableInfo<GeneratedId> tableInfo = new TableInfo<GeneratedId>(databaseType, GeneratedId.class);
		assertEquals(2, tableInfo.getFieldTypes().length);
		StringBuilder sb = new StringBuilder();
		List<String> additionalArgs = new ArrayList<String>();
		List<String> statementsBefore = new ArrayList<String>();
		databaseType.appendColumnArg(sb, tableInfo.getFieldTypes()[0], additionalArgs, statementsBefore, null, null);
		assertTrue(sb.toString().contains(" AUTO_INCREMENT"));
		assertEquals(1, additionalArgs.size());
		assertTrue(additionalArgs.get(0).contains("PRIMARY KEY"));
	}

	@Test
	public void testTableSuffix() throws Exception {
		MysqlDatabaseType dbType = new MysqlDatabaseType();
		String suffix = "ewfwefef";
		dbType.setCreateTableSuffix(suffix);
		StringBuilder sb = new StringBuilder();
		dbType.appendCreateTableSuffix(sb);
		assertTrue(sb.toString().contains(suffix));
	}

	@Test
	public void testDateTime() throws Exception {
		MysqlDatabaseType dbType = new MysqlDatabaseType();
		StringBuilder sb = new StringBuilder();
		dbType.appendDateType(sb, 0);
		assertEquals("DATETIME", sb.toString());
	}

	@Test
	public void testObject() throws Exception {
		MysqlDatabaseType dbType = new MysqlDatabaseType();
		StringBuilder sb = new StringBuilder();
		dbType.appendObjectType(sb);
		assertEquals("BLOB", sb.toString());
	}
}
