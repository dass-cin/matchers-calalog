package br.cin.ufpe.dass.matchers.core;

import fr.inrialpes.exmo.ontowrap.BasicOntology;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by diego on 07/03/17.
 */
@CompoundIndexes(
        {@CompoundIndex(name = "uri_index", unique = true, def = "{'uri': 1}"),
        @CompoundIndex(name = "file_index", unique = true, def = "{'file': 1}")}
)
public class Ontology extends BasicOntology {

    @Id
    private String id;

    private String description;

    private Set<OntologyProperty> properties = new HashSet<>();

    private Set<OntologyProperty> partOfProperties = new HashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Set<OntologyProperty> getProperties() {
        return properties;
    }

    public void setProperties(Set<OntologyProperty> properties) {
        this.properties = properties;
    }

    public Set<OntologyProperty> getPartOfProperties() {
        return partOfProperties;
    }

    public void setPartOfProperties(Set<OntologyProperty> partOfProperties) {
        this.partOfProperties = partOfProperties;
    }
}
