package br.cin.ufpe.dass.matchers.service;

import br.cin.ufpe.dass.matchers.config.ApplicationProperties;
import br.cin.ufpe.dass.matchers.core.Ontology;
import br.cin.ufpe.dass.matchers.exception.InvalidOntologyFileException;
import br.cin.ufpe.dass.matchers.repository.OntologyRepository;
import br.cin.ufpe.dass.matchers.util.FormatHelper;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.RDFS;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Created by diego on 07/03/17.
 */
@Service
public class OntologyService {

    private final OntologyRepository repository;

    private final ApplicationProperties configProperties;

    public OntologyService(OntologyRepository repository, ApplicationProperties configProperties) {
        this.repository = repository;
        this.configProperties = configProperties;
    }

    /**
     * Loads a local ontology (from file) to database
     * @param ontology Ontology
     * @throws InvalidOntologyFileException
     */
    public Ontology loadOntology(Ontology ontology) throws InvalidOntologyFileException {

        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        IRI iri = IRI.create(FormatHelper.formatFilePathToApi(ontology.getFile().toString(), true));

        Ontology localOntology = repository.findByUri(iri.toURI().toString());
        if (localOntology == null) {
            localOntology = new Ontology();
            localOntology.setDomain(ontology.getDomain());
            localOntology.setFormalism(ontology.getFormalism());
            localOntology.setUri(iri.toURI());
            localOntology.setDescription(ontology.getDescription());
            localOntology.setFile(IRI.create(ontology.getFile()).toURI());
            repository.save(localOntology);
        }

        return localOntology;

    }

    public OntModel getJenaOntologyModel(String path) {
        OntModelSpec spec = new OntModelSpec(OntModelSpec.OWL_DL_MEM_TRANS_INF);
        Model model = createOntologyFromDirectory(path);

        StmtIterator subIt = model.listStatements((Resource)null, RDFS.subClassOf, (RDFNode)null);
        Resource object;

        while(subIt.hasNext()) {
            Statement subIt2 = (Statement)subIt.next();
            if(subIt2.getObject() != null && subIt2.getSubject() != null) {
                String owlApi = subIt2.getObject().toString();
                String subject = configProperties.getDefaultURI() + "superClassOf";
                String predicate = subIt2.getSubject().getURI();
                object = model.createResource(owlApi);
                Property predicate1 = model.createProperty(subject);
                Resource object1 = model.createResource(predicate);
                if(!model.contains(object, predicate1, object1)) {
                    model.add(object, predicate1, object1);
                }
            }
        }

        StmtIterator subIt21 = model.listStatements((Resource)null, RDFS.subPropertyOf, (RDFNode)null);
        while(subIt21.hasNext()) {
            Statement owlApi1 = (Statement)subIt21.next();
            if(owlApi1.getObject() != null && owlApi1.getSubject() != null) {
                Resource subject1 = model.createResource(owlApi1.getObject().toString());
                Property predicate2 = model.createProperty(configProperties.getDefaultURI() + "superPropertyOf");
                object = model.createResource(owlApi1.getSubject().getURI());
                if(!model.contains(subject1, predicate2, object)) {
                    model.add(subject1, predicate2, object);
                }
            }
        }
        OntModel ontModel = ModelFactory.createOntologyModel(spec, model);

        return ontModel;
    }

    public static Model createOntologyFromDirectory(String path) {
        return FileManager.get().loadModel(FormatHelper.formatFilePathToApi(path, false));
    }




}
