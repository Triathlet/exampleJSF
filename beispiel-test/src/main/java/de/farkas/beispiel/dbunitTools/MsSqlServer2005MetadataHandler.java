package de.farkas.beispiel.dbunitTools;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.dbunit.database.IMetadataHandler;
import org.dbunit.util.SQLHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MsSqlServer2005MetadataHandler implements IMetadataHandler {

    private static final Logger logger = LoggerFactory.getLogger(MsSqlServer2005MetadataHandler.class);

    public ResultSet getColumns(DatabaseMetaData databaseMetaData, String schemaName, String tableName) 
    throws SQLException 
    {
        if(logger.isTraceEnabled())
            logger.trace("getColumns(databaseMetaData={}, schemaName={}, tableName={}) - start", 
                    new Object[] {databaseMetaData, schemaName, tableName} );
        
       // ResultSet resultSet = databaseMetaData.getColumns(
        //        null, schemaName, tableName, "%");
        
        //Workaround fuer sqlserver2005 und dbunit 2.4.9 => 23. Spalte muss einfach mit '' gesetzt werden
        //Create Table metaDataTable ( TABLE_QUALIFIER sysname, TABLE_OWNER sysname, TABLE_NAME sysname, COLUMN_NAME sysname, DATA_TYPE smallint, TYPE_NAME sysname, PRECISION int, LENGTH int, SCALE smallint, RADIX smallint, NULLABLE smallint, REMARKS varchar(254), COLUMN_DEF nvarchar(4000), SQL_DATA_TYPE smallint, SQL_DATETIME_SUB smallint, CHAR_OCTET_LENGTH int, ORDINAL_POSITION int, IS_NULLABLE varchar(254), SS_DATA_TYPE tinyint );
        //String selectMetaDataSql = "select TABLE_QUALIFIER , TABLE_OWNER , TABLE_NAME , COLUMN_NAME , cast (Cast(SQL_DATA_TYPE as varchar) + isnull(cast( SQL_DATETIME_SUB as varchar),'') as int ) as DATA_TYPE , TYPE_NAME , PRECISION , LENGTH , SCALE , RADIX , NULLABLE , REMARKS , COLUMN_DEF , SQL_DATA_TYPE , SQL_DATETIME_SUB , CHAR_OCTET_LENGTH , ORDINAL_POSITION , IS_NULLABLE , SS_DATA_TYPE, '', '','','','' from metaDataTable;"; 
        String selectMetaDataSql = "select *, '', '','','','' from metaDataTable;";
        String deleteMetaDataSql = "delete from metaDataTable;";
        String insertMetaDataSql = "insert into metaDataTable exec sp_columns @table_name = '" + tableName + ((schemaName != null) ? "', @table_qualifier = '" + schemaName : "") + "';\n";
        
        Connection dbCon = databaseMetaData.getConnection();
        
        Statement stmt = dbCon.createStatement();
        stmt.execute(deleteMetaDataSql);
        stmt.execute(insertMetaDataSql);
        ResultSet rs = stmt.executeQuery(selectMetaDataSql);
        
        
        return rs;
    }

    public boolean matches(ResultSet resultSet,
            String schema, String table, boolean caseSensitive) 
    throws SQLException 
    {
        return matches(resultSet, null, schema, table, null, caseSensitive);
    }

    public boolean matches(ResultSet columnsResultSet, String catalog,
            String schema, String table, String column,
            boolean caseSensitive) throws SQLException 
    {
        if(logger.isTraceEnabled())
            logger.trace("matches(columnsResultSet={}, catalog={}, schema={}," +
            		" table={}, column={}, caseSensitive={}) - start", 
                    new Object[] {columnsResultSet, catalog, schema, 
                            table, column, Boolean.valueOf(caseSensitive)});
        
        String catalogName = columnsResultSet.getString(1);
        String schemaName = columnsResultSet.getString(2);
        String tableName = columnsResultSet.getString(3);
        String columnName = columnsResultSet.getString(4);

        if(logger.isDebugEnabled()){
            logger.debug("Comparing the following values using caseSensitive={} (searched<=>actual): " +
                    "catalog: {}<=>{} schema: {}<=>{} table: {}<=>{} column: {}<=>{}", 
                    new Object[] {
                        Boolean.valueOf(caseSensitive),
                        catalog, catalogName,
                        schema, schemaName,
                        table, tableName,
                        column, columnName
                    });
        }
        
        boolean areEqual = 
                areEqualIgnoreNull(catalog, catalogName, caseSensitive) &&
                areEqualIgnoreNull(schema, schemaName, caseSensitive) &&
                areEqualIgnoreNull(table, tableName, caseSensitive) &&
                areEqualIgnoreNull(column, columnName, caseSensitive);
        return areEqual;
    }

    private boolean areEqualIgnoreNull(String value1, String value2,
            boolean caseSensitive) {
        return SQLHelper.areEqualIgnoreNull(value1, value2, caseSensitive);
    }

    public String getSchema(ResultSet resultSet) throws SQLException {
        if(logger.isTraceEnabled())
            logger.trace("getColumns(resultSet={}) - start", resultSet);

        String schemaName = resultSet.getString(2);
        return schemaName;
    }
    
    public boolean tableExists(DatabaseMetaData metaData, String schemaName, String tableName) 
    throws SQLException 
    {
        if(logger.isTraceEnabled())
            logger.trace("tableExists(metaData={}, schemaName={}, tableName={}) - start", 
                    new Object[] {metaData, schemaName, tableName} );
        
        ResultSet tableRs = metaData.getTables(null, schemaName, tableName, null);
        try 
        {
            return tableRs.next();
        }
        finally
        {
            SQLHelper.close(tableRs);
        }
    }

    public ResultSet getTables(DatabaseMetaData metaData, String schemaName, String[] tableType) 
    throws SQLException
    {
        if(logger.isTraceEnabled())
            logger.trace("getTables(metaData={}, schemaName={}, tableType={}) - start", 
                    new Object[] {metaData, schemaName, tableType} );

        return metaData.getTables(null, schemaName, "%", tableType);
    }

    public ResultSet getPrimaryKeys(DatabaseMetaData metaData, String schemaName, String tableName) 
    throws SQLException
    {
        if(logger.isTraceEnabled())
            logger.trace("getPrimaryKeys(metaData={}, schemaName={}, tableName={}) - start", 
                    new Object[] {metaData, schemaName, tableName} );

        ResultSet resultSet = metaData.getPrimaryKeys(
                null, schemaName, tableName);
        return resultSet;
    }

}
