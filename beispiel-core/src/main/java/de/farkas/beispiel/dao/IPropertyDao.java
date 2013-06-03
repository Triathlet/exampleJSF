package de.farkas.beispiel.dao;

import java.util.List;

import de.farkas.beispiel.dto.PropertyDto;

public interface IPropertyDao {
	

	public void saveProperty(PropertyDto property) ;
	

	public List<PropertyDto> getProperties() ;
	

	public PropertyDto getProperty(String key) ;
	

	public void deleteProperty(String key) ;
}
