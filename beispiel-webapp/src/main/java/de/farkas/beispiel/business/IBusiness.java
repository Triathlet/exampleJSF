package de.farkas.beispiel.business;

import java.util.List;


import org.springframework.transaction.annotation.Transactional;

import de.farkas.beispiel.dto.PropertyDto;


public interface IBusiness {

	public void saveProperty(PropertyDto property) ;
	
	
	public List<PropertyDto> getProperties() ;
	
	
	public PropertyDto getProperty(String key) ;
	

	public void deleteProperty(String key) ;
}
