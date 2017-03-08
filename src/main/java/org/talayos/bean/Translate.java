package org.talayos.bean;

import lombok.Data;

import java.util.Map;

/**
 * Created by jluiscabrera on 3/2/17.
 */
@Data
public class Translate implements Comparable<Translate>{
    private String nameProperty;
    private Map<String,String> languages;

    public int compareTo(Translate compareTranslate) {

        String propertyComparator = ((Translate) compareTranslate).getNameProperty();

        return this.nameProperty.toUpperCase().compareTo(propertyComparator.toUpperCase());
    }
}
