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
	/** 
	 * Loads a {@link IDataSet dataset} from {@link Resource}s obtained from the specified <tt>location</tt>. Each
	 * <tt>location</tt> can be mapped to a number of potential {@link #getResourceLocations resources}, the first
	 * resource that {@link Resource#exists() exists} will be used. {@link Resource}s are loaded using the
	 * {@link ResourceLoader} returned from {@link #getResourceLoader}.
	 * <p>
	 * If no resource can be found then <tt>null</tt> will be returned.
	 * commons-test-ordner wird immer betrachtet, die Spezielle Klasse ueberschreibt diese Dateien, falls vorhanden
	 * 
	 * @see #createDataSet(Resource)
	 * @see com.github.springtestdbunit.dataset.DataSetLoader#loadDataSet(Class, String) java.lang.String)
	 */
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

	/**
	 * Gets the {@link ResourceLoader} that will be used to load the dataset {@link Resource}s.
	 * @param testClass The class under test
	 * @return a resource loader
	 */
	private ResourceLoader getResourceLoader(Class<?> testClass) {
		return new ClassRelativeResourceLoader(testClass);
	}

	/**
	 * Get the resource locations that should be considered when attempting to load a dataset from the specified
	 * location.
	 * @param testClass The class under test
	 * @param location The source location
	 * @return an array of potential resource locations
	 */
	private String[] getResourceLocations(Class<?> testClass, String location) {
		return new String[] { location };
	}

	/**
	 * Factory method used to create the {@link IDataSet dataset}
	 * @param resource an existing resource that contains the dataset data
	 * @return a dataset
	 * @throws IOException 
	 * @throws DataSetException 
	 * @throws Exception if the dataset could not be loaded
	 */
	public IDataSet createDataSet(Resource resource) throws DataSetException, IOException {
		return new CsvURLDataSet(resource.getURL());
	}
	
}
