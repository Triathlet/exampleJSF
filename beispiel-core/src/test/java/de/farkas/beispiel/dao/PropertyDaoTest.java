package de.farkas.beispiel.dao;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import org.springframework.test.context.transaction.TransactionConfiguration;

import de.farkas.beispiel.dbunitTools.AddCleanInsertTables;
import de.farkas.beispiel.dbunitTools.DBUnitTransactionalTest;

import de.farkas.beispiel.dto.PropertyDto;

@AddCleanInsertTables({"properties"})
@ContextConfiguration(locations="spring-dao-test.xml")
@TransactionConfiguration(transactionManager="txManager")
public class PropertyDaoTest extends DBUnitTransactionalTest {
	
	@Autowired
	private IPropertyDao propertyDao;
	
	
	@Test
	//@DatabaseSetup("sampleData.xml")
	public void getPropertiesTest() {
		
		List<PropertyDto> props = propertyDao.getProperties();
		
		Assert.assertTrue(props.size() == 1);
		
	}
	
	@Test
	//@DatabaseSetup("sampleData.xml")
	public void getPropertyTest() {
		PropertyDto prop = propertyDao.getProperty("TestC");
		Assert.assertEquals("TestC", prop.getKey());
		Assert.assertEquals("4.5.6", prop.getValue());
		Assert.assertEquals(Integer.valueOf( 1 ), prop.getId());
	}
	
	@Test
	//@DatabaseSetup("sampleData.xml")
	public void deletePropertyTest() {
		propertyDao.deleteProperty("TestC");
		Assert.assertTrue(propertyDao.getProperties().size() == 0);
	}
	
	@Test
	//@DatabaseSetup("sampleData.xml")
	//@ExpectedDatabase("expectedData.xml")
	public void savePropertyTest() {
		PropertyDto prop = new PropertyDto();
		prop.setKey("TestA");
		prop.setValue("1.2.3");
		propertyDao.saveProperty(prop);
		
		PropertyDto propTest =  propertyDao.getProperty("TestA");
		
		Assert.assertEquals("TestA", propTest.getKey());
		Assert.assertEquals("1.2.3", propTest.getValue());
		Assert.assertEquals(prop.getId(), propTest.getId());
	}

}
