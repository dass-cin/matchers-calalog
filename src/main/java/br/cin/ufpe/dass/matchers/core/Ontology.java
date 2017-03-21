package br.cin.ufpe.dass.matchers.core;

import fr.inrialpes.exmo.ontowrap.BasicOntology;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdfxml.xmloutput.impl.Basic;
import org.semanticweb.owlapi.model.IRI;
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
public class Ontology {

    @Id
    private String id;

    private String description;

    private String formalism;

    @Indexed(unique = true)
    private URI file;

    @Indexed(unique = true)
    private URI uri;

    public BasicOntology toBasicOntology() {
        BasicOntology basicOntology = new BasicOntology();
        basicOntology.setFile(IRI.create(file).toURI());
        basicOntology.setFormalism(formalism);
        basicOntology.setURI(uri);
        return basicOntology;
    }

    public String getId() {
        return id;
    }

    public String getFormalism() {
        return formalism;
    }

    public void setFormalism(String formalism) {
        this.formalism = formalism;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public URI getFile() {
        return file;
    }

    public void setFile(URI file) {
        this.file = file;
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

}
