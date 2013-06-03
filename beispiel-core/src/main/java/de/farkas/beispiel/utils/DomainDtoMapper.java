package de.farkas.beispiel.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import de.farkas.beispiel.dto.PropertyDto;
import de.farkas.beispiel.model.Property;

@Component
public class DomainDtoMapper {

	public Property map(PropertyDto source ) {
		Property prop = new Property();
		prop.setId(source.getId());
		prop.setKey(source.getKey());
		prop.setValue(source.getValue());
		return prop;
	}
	
	public PropertyDto map(Property source) {
		PropertyDto prop = new PropertyDto();
		prop.setId(source.getId());
		prop.setKey(source.getKey());
		prop.setValue(source.getValue());
		return prop;
	}
	
	public List<PropertyDto> mapDomain(List<Property> source) {
		List<PropertyDto> props = new ArrayList<PropertyDto>(source.size());
		for (Property prop: source) {
			props.add(map(prop));
		}
		return props;
	}
	
	public List<Property> mapDto(List<PropertyDto> source) {
		List<Property> props = new ArrayList<Property>(source.size());
		for (PropertyDto prop: source) {
			props.add(map(prop));
		}
		return props;
	}
}
