package br.cin.ufpe.dass.matchers.core;

import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by diego on 07/03/17.
 */
public class OntologyProperty {

    public enum OntologyPropertyType { PROPERTY, PART_OF_PROPERTY }

    private String name;

    private Set<String> domain = new HashSet<String>();

    private Set<String> range = new HashSet<String>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getDomain() {
        return domain;
    }

    public void setDomain(Set<String> domain) {
        this.domain = domain;
    }

    public Set<String> getRange() {
        return range;
    }

    public void setRange(Set<String> range) {
        this.range = range;
    }
}
