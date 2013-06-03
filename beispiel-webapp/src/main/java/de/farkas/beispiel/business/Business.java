package de.farkas.beispiel.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import de.farkas.beispiel.dao.IPropertyDao;
import de.farkas.beispiel.dto.PropertyDto;

@Service
public class Business implements IBusiness{

	@Autowired
	IPropertyDao propertyDao;
	
	@Transactional
	public void saveProperty(PropertyDto property) {
		propertyDao.saveProperty(property);
	}
	
	@Transactional
	public List<PropertyDto> getProperties() {
		return propertyDao.getProperties();
	}
	
	@Transactional
	public PropertyDto getProperty(String key) {
		return propertyDao.getProperty(key);
	}
	
	@Transactional
	public void deleteProperty(String key) {
		propertyDao.deleteProperty(key);
	}
	
}
