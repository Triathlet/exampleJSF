package de.farkas.beispiel.dbunitTools;

import java.lang.annotation.*;

@Retention( RetentionPolicy.RUNTIME )
public @interface AddCleanInsertTables {
	
	String[] value();

}
