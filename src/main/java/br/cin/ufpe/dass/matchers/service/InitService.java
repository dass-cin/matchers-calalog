package br.cin.ufpe.dass.matchers.service;

import br.cin.ufpe.dass.matchers.MatchersCatalogApplication;
import br.cin.ufpe.dass.matchers.core.Alignment;
import br.cin.ufpe.dass.matchers.core.Matcher;
import br.cin.ufpe.dass.matchers.core.Ontology;
import br.cin.ufpe.dass.matchers.exception.AlignmentNotFoundException;
import br.cin.ufpe.dass.matchers.exception.InvalidOntologyFileException;
import br.cin.ufpe.dass.matchers.exception.MatcherNotFoundException;
import br.cin.ufpe.dass.matchers.repository.AlignmentRepository;
import br.cin.ufpe.dass.matchers.repository.MatcherRepository;
import br.cin.ufpe.dass.matchers.repository.OntologyRepository;
import org.semanticweb.owl.align.AlignmentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by diego on 28/03/17.
 */
@Service
public class InitService {

    private static Logger log = LoggerFactory.getLogger(MatchersCatalogApplication.class);

    private final OntologyService ontologyService;

    private final OntologyRepository ontologyRepository;

    private final OntologyProfileService ontologyProfileService;

    private final MatcherRepository matcherRepository;

    private final AlignmentService alignmentService;

    private final AlignmentRepository alignmentRepository;

    public InitService(OntologyService ontologyService, OntologyRepository ontologyRepository, OntologyProfileService ontologyProfileService, MatcherRepository matcherRepository, AlignmentService alignmentService, AlignmentRepository alignmentRepository) {
        this.ontologyService = ontologyService;
        this.ontologyRepository = ontologyRepository;
        this.ontologyProfileService = ontologyProfileService;
        this.matcherRepository = matcherRepository;
        this.alignmentService = alignmentService;
        this.alignmentRepository = alignmentRepository;
    }

    public void loadMatchers() {
        if (matcherRepository.findByName("COMA") == null) {
            Matcher matcher = new Matcher();
            matcher.setName("COMA");
            matcher.setVersion("3.0");
            matcher.setEndPoint("http://localhost:8081/api/coma");

            matcher.getConfigurationParameters().put("resolution", "RES2_SELFNODE"); //
            matcher.getConfigurationParameters().put("measure", "SIM_STR_EDITDIST");

            matcherRepository.save(matcher);
        }

        if (matcherRepository.findByName("AML") == null) {
            Matcher matcher = new Matcher();
            matcher.setName("AML");
            matcher.setVersion("2.1-SNAPSHOT");
            matcher.setEndPoint("http://localhost:8081/api/aml");
            matcher.getConfigurationParameters().put("config", "aml/config.ini");
            matcher.getConfigurationParameters().put("bkPath", "aml/knowledge/");
            matcher.getConfigurationParameters().put("mode", "manual");
            matcherRepository.save(matcher);
        }

        if (matcherRepository.findByName("LogMap") == null) {
            Matcher matcher = new Matcher();
            matcher.setName("LogMap");
            matcher.setVersion("0.0.1-SNAPSHOT");
            matcher.setEndPoint("http://localhost:8081/api/logmap");
            matcher.getConfigurationParameters().put("outputPath", null);
            matcher.getConfigurationParameters().put("evalImpact", false);
            matcherRepository.save(matcher);
        }

    }


    public void importOntology(Path file, String domain, String formalism) {
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
                log.error(String.format("Fail to load ontology %s", ontology.getFile()), e);
                e.printStackTrace();
            }
        }
    }

    public void loadOntologies() {
        try {
            //loading ontologies of conference domain
            Stream<Path> files = Files.list(Paths.get(this.getClass().getResource("/ontologies/oaei2016/conference").getPath()));
            files.forEach( (file) -> { if (!file.getFileName().toString().contains("reference") && !file.getFileName().toString().contains("excluded"))   this.importOntology(file, "conference", "OWL");});

            //loading ontologies of anatomy domain
            files = Files.list(Paths.get(this.getClass().getResource("/ontologies/oaei2016/anatomy").getPath()));
            files.forEach( (file) -> { if (!file.getFileName().toString().contains("reference") && !file.getFileName().toString().contains("excluded")) this.importOntology(file, "anatomy", "OWL");});

            //loading ontologies of phenotype domain
            files = Files.list(Paths.get(this.getClass().getResource("/ontologies/phenotype").getPath()));
            files.forEach( (file) -> { if (!file.getFileName().toString().contains("reference") && !file.getFileName().toString().contains("excluded")) this.importOntology(file, "phenotype", "RDF");});

        } catch (IOException e) {
            log.error("Fail to load ontologies during initialization", e);
            e.printStackTrace();
        }
    }

    /***
     *
     * Generate alignments for all ontologies using all matchers...
     *
     */
    public void generateAlignments() {
        matcherRepository.findAll().forEach((matcher) -> {
            String[] domains = {"conference", "anatomy" , "phenotype"};
            for (String domain : domains) {
                List<Ontology> ontologies = ontologyRepository.findByDomain(domain);
                for (int i = 0; i < ontologies.size(); i++) {
                    for (int j = 0; j < ontologies.size(); j++) {
                        Ontology source = ontologies.get(i);
                        Ontology target = ontologies.get(j);
                        Alignment existingAlignment1 = alignmentRepository.findByOntology1AndOntology2AndMatcher(source, target, matcher);
                        Alignment existingAlignment2 = alignmentRepository.findByOntology1AndOntology2AndMatcher(target, source, matcher);
                        if (domain.equals("anatomy") && source.getDescription().equals("human")) continue;
                        if (!source.equals(target) && existingAlignment1 == null && existingAlignment2 == null) {
                            try {
                                log.info(String.format("starting alignment between %s and %s with matcher %s", source.getDescription(), target.getDescription(), matcher.getName()));
                                Alignment alignment = alignmentService.align(source.getFile(), target.getFile(), matcher.getName());
                                if (alignment != null) {
                                    String sourceFile = Paths.get(alignment.getOntology1().getFile()).getFileName().toString().replaceAll(".owl", "");
                                    String targetFile = Paths.get(alignment.getOntology2().getFile()).getFileName().toString().replaceAll(".owl", "");
                                    String file = "/ontologies/oaei2016/" + domain + "/reference/" + sourceFile + "-" + targetFile + ".rdf";
                                    log.info(String.format("evaluating file %s", file));
                                    Path referenceFile = Paths.get(this.getClass().getResource(file).toURI());
                                    if (referenceFile != null && referenceFile.toFile().exists()) {
                                        alignmentService.evaluate(alignment.getId(), "file://" + referenceFile.toString());
                                    } else {
                                        log.error(String.format("reference file not found %s", file));
                                    }
                                } else {
                                    log.error(String.format("matcher %s cannot process alignment between %s and %s", matcher.getName(), source.getFile(), target.getFile()));
                                }

                            } catch (InvalidOntologyFileException e) {
                                log.error(String.format("Invalid ontology pair [%s,%s]", source.getFile().toString(), target.getFile().toString()), e);
                            } catch (MatcherNotFoundException e) {
                                log.error(String.format("Matcher not found [%s,%s]", matcher.getName()), e);
                            } catch (AlignmentException e) {
                                log.error(String.format("Alignment exception when matching pair [%s,%s] with matcher [%s]",source.getFile().toString(), target.getFile().toString(), matcher.getName()), e);
                            } catch (AlignmentNotFoundException e) {
                                log.error(String.format("Alignment not found exception", e));
                                e.printStackTrace();
                            } catch (URISyntaxException e) {
                                log.error(String.format("Invalid URI to reference file", e));
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
        log.info("initialization finished");
    }

}
