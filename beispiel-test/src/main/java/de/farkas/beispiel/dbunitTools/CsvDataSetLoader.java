package de.farkas.beispiel.dbunitTools;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import org.dbunit.dataset.CompositeDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.csv.CsvURLDataSet;
import org.dbunit.dataset.filter.ExcludeTableFilter;
import org.springframework.core.io.ClassRelativeResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import com.github.springtestdbunit.dataset.DataSetLoader;

public class CsvDataSetLoader implements DataSetLoader {
	
	public IDataSet loadDataSet(Class<?> testClass, String location) throws Exception {
		//AddCleanInsertTables auslesen
		AddCleanInsertTables annotation = testClass.getAnnotation(AddCleanInsertTables.class);
		String[] insertTables = (annotation == null) ? null : annotation.value();		
		
		//Ressource laden
		Queue<IDataSet> dataSets = new LinkedList<IDataSet>();
		//allgemein, keine Ressourcen-Klasse zum auslesen noetig
		ResourceLoader resourceLoader = getResourceLoader(CsvDataSetLoader.class);
		Resource resource = resourceLoader.getResource("");
		dataSets.add(createDataSet(resource));
		//speziell, angebe ueber location (location="default" fuer normal oder location="<classpath:xyz>" hier auch mehrere)
		resourceLoader = getResourceLoader(testClass);
		String[] resourceLocations = getResourceLocations(testClass, location);
		for (String resourceLocation : resourceLocations) {
			if (resourceLocation.equals("default")) {
				resource = resourceLoader.getResource("");
			} else {
				resource = resourceLoader.getResource(resourceLocation);
			}
			if (resource.exists()) {
				dataSets.add(createDataSet(resource));
			}
		}
		
		IDataSet retDataSet = replaceTables(dataSets);
		
		if (retDataSet != null) {
			return new FilteredDataSet(insertTables, retDataSet);
		}
		
		return null;


	}
	
	private IDataSet replaceTables(Queue<IDataSet> dataSets) throws DataSetException {
		if (dataSets.isEmpty())
			return null;
		IDataSet dataSet = dataSets.poll();
	    while ( !dataSets.isEmpty() )
	    {
	    	IDataSet temp = dataSets.poll();
	    	dataSet = new FilteredDataSet(new ExcludeTableFilter(temp.getTableNames()), dataSet);
			dataSet = new CompositeDataSet(dataSet,temp);
	    }
		
		return dataSet;
	}

	private ResourceLoader getResourceLoader(Class<?> testClass) {
		return new ClassRelativeResourceLoader(testClass);
	}

	private String[] getResourceLocations(Class<?> testClass, String location) {
		return new String[] { location };
	}

	public IDataSet createDataSet(Resource resource) throws DataSetException, IOException {
		return new CsvURLDataSet(resource.getURL());
	}
	
}
