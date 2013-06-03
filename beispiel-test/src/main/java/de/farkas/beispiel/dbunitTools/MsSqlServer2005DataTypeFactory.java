package de.farkas.beispiel.dbunitTools;

import org.dbunit.dataset.datatype.DataType;
import org.dbunit.dataset.datatype.DataTypeException;
import org.dbunit.ext.mssql.MsSqlDataTypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MsSqlServer2005DataTypeFactory extends MsSqlDataTypeFactory {
	
	private static final Logger log = LoggerFactory.getLogger(MsSqlDataTypeFactory.class);
	
	public static final int DATETIME = 11;
	
	public DataType createDataType(int sqlType, String sqlTypeName) throws DataTypeException
	    {
	    	if(log.isDebugEnabled())
	    		log.debug("createDataType(sqlType={}, sqlTypeName={}) - start", String.valueOf(sqlType), sqlTypeName); 
	        
	    	switch(sqlType) {
	            case DATETIME: return DataType.TIMESTAMP; // datetime
	            default: return super.createDataType(sqlType, sqlTypeName);
	        }


	    }

}
