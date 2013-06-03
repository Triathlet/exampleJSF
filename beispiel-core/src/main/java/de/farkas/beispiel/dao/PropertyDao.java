package de.farkas.beispiel.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import de.farkas.beispiel.dto.PropertyDto;
import de.farkas.beispiel.model.Property;
import de.farkas.beispiel.utils.DomainDtoMapper;

@Repository
public class PropertyDao implements IPropertyDao {
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	DomainDtoMapper mapper;
	
	public void saveProperty(PropertyDto property) {
		Property prop = mapper.map(property);
		sessionFactory.getCurrentSession().saveOrUpdate(prop);
		property.setId(prop.getId());
	}
	

	@SuppressWarnings("unchecked")
	public List<PropertyDto> getProperties() {
		List<Property> props = sessionFactory.getCurrentSession().createCriteria(Property.class).list();
		return mapper.mapDomain(props);
	}
	
	
	public PropertyDto getProperty(String key) {
		return mapper.map(getProp(key));
	}
	
	public void deleteProperty(String key) {
		Property prop = getProp(key);
		sessionFactory.getCurrentSession().delete(prop);
	}
	
	private Property getProp(String key) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Property.class);
		criteria.add(Restrictions.eq("key", key));
		return (Property) criteria.uniqueResult();
	}

}
