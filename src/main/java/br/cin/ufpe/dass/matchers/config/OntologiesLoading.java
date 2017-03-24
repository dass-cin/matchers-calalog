package br.cin.ufpe.dass.matchers.config;

import br.cin.ufpe.dass.matchers.core.Ontology;
import br.cin.ufpe.dass.matchers.exception.InvalidOntologyFileException;
import br.cin.ufpe.dass.matchers.repository.OntologyRepository;
import br.cin.ufpe.dass.matchers.service.OntologyProfileService;
import br.cin.ufpe.dass.matchers.service.OntologyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Created by diego on 20/03/17.
 */
@Component
public class OntologiesLoading implements ApplicationListener<ApplicationReadyEvent> {

    private Logger logger = LoggerFactory.getLogger(OntologiesLoading.class);

    private final OntologyService ontologyService;

    private final OntologyRepository ontologyRepository;

    private final OntologyProfileService ontologyProfileService;

    public OntologiesLoading(OntologyService ontologyService, OntologyRepository ontologyRepository, OntologyProfileService ontologyProfileService) {
        this.ontologyService = ontologyService;
        this.ontologyRepository = ontologyRepository;
        this.ontologyProfileService = ontologyProfileService;
    }

    private void importOntology(Path file, String domain, String formalism) {
        Ontology ontology = ontologyRepository.findByFile(file.toUri());

        if (ontology == null) {
            ontology = new Ontology();
            ontology.setFile(file.toUri());
            ontology.setDescription(new File(file.getFileName().toUri()).getName().replaceAll(".rdf", "").replaceAll(".owl", ""));
            ontology.setFormalism(formalism);
            ontology.setDomain(domain);
            try {
                ontology = ontologyService.loadOntology(ontology);
                ontology = ontologyProfileService.generateOntologyProfile(ontology.getId());
            } catch (InvalidOntologyFileException e) {
                logger.error(String.format("Fail to load ontology %s", ontology.getFile()), e);
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {

        try {
            //loading ontologies of conference domain
            Stream<Path> files = Files.list(Paths.get(this.getClass().getResource("/ontologies/oaei2016/conference").getPath()));
            files.forEach( (file) -> { if (!file.getFileName().toString().contains("reference")) this.importOntology(file, "conference", "OWL");});

            //loading ontologies of anatomy domain
            files = Files.list(Paths.get(this.getClass().getResource("/ontologies/oaei2016/anatomy").getPath()));
            files.forEach( (file) -> { if (!file.getFileName().toString().contains("reference")) this.importOntology(file, "anatomy", "OWL");});

            //loading ontologies of phenotype domain
            files = Files.list(Paths.get(this.getClass().getResource("/ontologies/oaei2016/phenotype").getPath()));
            files.forEach( (file) -> { if (!file.getFileName().toString().contains("reference")) this.importOntology(file, "phenotype", "RDF");});

        } catch (IOException e) {
            logger.error("Fail to load ontologies during initialization", e);
            e.printStackTrace();
        }

    }

}
