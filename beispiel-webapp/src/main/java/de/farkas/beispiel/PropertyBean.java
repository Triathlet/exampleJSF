package de.farkas.beispiel;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.farkas.beispiel.business.IBusiness;
import de.farkas.beispiel.dto.PropertyDto;

@Component
@ManagedBean
@Scope("access")
public class PropertyBean {
	
	@Autowired
	IBusiness business;
	
	private List<PropertyDto> listProperties = null;
	
	private List<SelectItem> bereiche = new ArrayList<SelectItem>();
	
	private String bereich = "Rezept";
	
	private String variante = "D";
	
	private String varianteRezept = "D";
	private String varianteKompass = "A";
	
	private List<SelectItem> varianten = new ArrayList<SelectItem>();
	
	private String[] bs = {"Kompass", "Rezept"};
	
	private String[] varA = {"","A", "B"};
	private String[] varC = {"","C", "D"};
	
	public String submitAll() {
		System.out.println(bereich + ";" + variante  + ";" + varianteRezept + ";" + varianteKompass);
		return "ergebnis.xhtml";
	}
	
	public void valueChanged(ValueChangeEvent event) {
		System.out.println("ausgefuehrt" + event.getNewValue() + ";" + varianteRezept + ";" + varianteKompass + ";" + getVariante());
        if (null != event.getNewValue()) {
            if (((String) event.getNewValue()).equals("Kompass")) {
            	varianteRezept = getVariante();
            	setVariante(varianteKompass);
                stringArrayToList(varianten, varA);
            } else {
            	varianteKompass = getVariante();
            	setVariante(varianteRezept);
            	stringArrayToList(varianten, varC);
            }
        }
    }
	
	public void varianteValueChanged(ValueChangeEvent event) {
		if (null != event.getNewValue()) {
			setVariante((String)event.getNewValue());
		} else {
			setVariante("");
		}
		
	}
	
	public void saveProperty() {
		PropertyDto prop = new PropertyDto();
		prop.setKey(this.key);
		prop.setValue(this.value);
		business.saveProperty(prop);
	}
	
	public List<PropertyDto> getListProperties() {
		listProperties = business.getProperties();
		return listProperties;
	}
	
	
	public void deleteProperty(String key) {
		business.deleteProperty(key);
	}
	
	public String key;

	public String value;
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}



	public String getBereich() {
		if (bereich == null || bereich.isEmpty()) {
			bereich = bs[0];
		}	
		return bereich;
	}

	public void setBereich(String bereich) {
		this.bereich = bereich;
	}

	public String getVariante() {
		return variante;
	}

	public void setVariante(String variante) {
		System.out.println("setVarianteCalled" + variante);
		this.variante = variante;
	}

	public List<SelectItem> getVarianten() {
		if (varianten.size() == 0 ) {
			System.out.println("getVarianten0");
			if (getBereich().equals("Kompass")) {
                stringArrayToList(varianten, varA);
            } else {
            	stringArrayToList(varianten, varC);
            }
		}
		return varianten;
	}

	public void setVarianten(List<SelectItem> varianten) {
		this.varianten = varianten;
	}

	public List<SelectItem> getBereiche() {
		if (bereiche == null || bereiche.size() == 0) {
			stringArrayToList(bereiche,bs);
		}	
		return bereiche;
	}

	public void setBereiche(List<SelectItem> bereiche) {
		this.bereiche = bereiche;
	}
	
	private boolean stringArrayToList(List<SelectItem> lis, String[] s) {
		lis.clear();
		for (String st : s) {
			lis.add(new SelectItem(st,st));
		}
		return true;
	}


	
}
