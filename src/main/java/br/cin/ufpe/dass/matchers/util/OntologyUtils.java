package br.cin.ufpe.dass.matchers.util;

import br.cin.ufpe.dass.matchers.core.OntologyProperty;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.util.FileManager;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLLogicalAxiom;
import org.semanticweb.owlapi.model.OWLOntology;

import java.net.URI;
import java.util.*;
import java.util.stream.Stream;

/**
 * Created by diego on 13/12/16.
 */
public class OntologyUtils {

    private static Optional<OntologyProperty> findPropertyByName(String propName, Set<OntologyProperty> properties) {
        return properties.stream().filter(property -> property.getName().equals(propName)).findAny();
    }

    private static boolean isDomainType(AxiomType type){
        return (type.equals(AxiomType.DATA_PROPERTY_DOMAIN) || type.equals(AxiomType.OBJECT_PROPERTY_DOMAIN));
    }

    private static OntologyProperties getOntologyProperties(OWLOntology ontology, URI uri, AxiomType type) {
        Stream<OWLLogicalAxiom> objectPropertyAxioms = ontology.axioms(type);
        String partOfUri = uri + "#" + "PartOf";

        Set<OntologyProperty> properties = new HashSet<OntologyProperty>();
        Set<OntologyProperty> partOfProperties = new HashSet<OntologyProperty>();

        objectPropertyAxioms.forEach(propertyAxiom -> {
            List<String> range = new ArrayList();
            propertyAxiom.signature().forEach(entity -> {
                boolean isNotPartOf = false;
                if(!entity.isOWLObjectProperty()) {
                    range.add(entity.getIRI().toString());
                } else  if(!entity.getIRI().toString().equalsIgnoreCase(partOfUri)) {
                    isNotPartOf = true;
                } else if(entity.getIRI().toString().equalsIgnoreCase(partOfUri)) {
                    isNotPartOf = false;
                }

                if (isNotPartOf) {
                    String property = entity.getIRI().toString();
                    Optional<OntologyProperty> spProp = Optional.empty();
                    spProp = findPropertyByName(property, properties);

                    if(!spProp.isPresent()) {
                        OntologyProperty ontProperty = new OntologyProperty();
                        ontProperty.setName(property);
                        if (isDomainType(type)) {
                            ontProperty.getDomain().addAll(range);
                        } else {
                            ontProperty.getRange().addAll(range);
                        }

                        properties.add(ontProperty);
                    } else {
                        if (isDomainType(type)) {
                            spProp.get().getDomain().addAll(range);
                        } else {
                            spProp.get().getRange().addAll(range);
                        }
                    }
                } else {
                    String property = entity.getIRI().toString();
                    Optional<OntologyProperty> spProp =  findPropertyByName(property, partOfProperties);
                    if(!spProp.isPresent()) {
                        OntologyProperty ontProperty = new OntologyProperty();
                        ontProperty.setName(property);
                        ontProperty.getDomain().addAll(range);
                        partOfProperties.add(ontProperty);
                    } else {
                        spProp.get().getDomain().addAll(range);
                    }
                }
            });
        });

        return new OntologyProperties(properties, partOfProperties);

    }


    public static Set<OntologyProperty> createProperties(OWLOntology ontology, URI uri, OntologyProperty.OntologyPropertyType type) {
        Set<OntologyProperty> properties = new HashSet<>();

        OntologyProperties objectPropertyDomain = getOntologyProperties(ontology, uri, AxiomType.OBJECT_PROPERTY_DOMAIN);
        OntologyProperties objectPropertyRange = getOntologyProperties(ontology, uri, AxiomType.OBJECT_PROPERTY_RANGE);
        OntologyProperties dataPropertyDomain = getOntologyProperties(ontology, uri, AxiomType.DATA_PROPERTY_DOMAIN);
        OntologyProperties dataPropertyRange = getOntologyProperties(ontology, uri, AxiomType.DATA_PROPERTY_RANGE);

        if (type.equals(OntologyProperty.OntologyPropertyType.PROPERTY)) {
            properties.addAll(objectPropertyDomain.getProperties());
            properties.addAll(objectPropertyRange.getProperties());
            properties.addAll(dataPropertyDomain.getProperties());
            properties.addAll(dataPropertyRange.getProperties());
        } else if (type.equals(OntologyProperty.OntologyPropertyType.PART_OF_PROPERTY)) {
            properties.addAll(objectPropertyDomain.getPartOfProperties());
            properties.addAll(objectPropertyRange.getPartOfProperties());
            properties.addAll(dataPropertyDomain.getPartOfProperties());
            properties.addAll(dataPropertyRange.getPartOfProperties());
        }

        return properties;

    }

    public static Model createOntologyFromDirectory(String path) {
        return FileManager.get().loadModel(FormatHelper.formatFilePathToApi(path, false));
    }


    private static class OntologyProperties {

        public OntologyProperties() {
        }

        public OntologyProperties(Set<OntologyProperty> properties, Set<OntologyProperty> partOfProperties) {
            this.properties = properties;
            this.partOfProperties = partOfProperties;
        }

        private Set<OntologyProperty> properties = new HashSet<OntologyProperty>();
        private Set<OntologyProperty> partOfProperties = new HashSet<OntologyProperty>();

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

}
