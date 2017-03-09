package br.cin.ufpe.dass.matchers.service;

import br.cin.ufpe.dass.matchers.config.ApplicationProperties;
import br.cin.ufpe.dass.matchers.core.*;
import br.cin.ufpe.dass.matchers.core.Alignment;
import br.cin.ufpe.dass.matchers.exception.AlignmentNotFoundException;
import br.cin.ufpe.dass.matchers.exception.InvalidOntologyFileException;
import br.cin.ufpe.dass.matchers.exception.MatcherNotFoundException;
import br.cin.ufpe.dass.matchers.repository.AlignmentRepository;
import br.cin.ufpe.dass.matchers.repository.MatcherRepository;
import br.cin.ufpe.dass.matchers.repository.OntologyRepository;
import br.cin.ufpe.dass.matchers.util.FormatHelper;
import br.cin.ufpe.dass.matchers.util.HeaderUtil;
import br.cin.ufpe.dass.matchers.util.OntologyUtils;
import fr.inrialpes.exmo.align.impl.eval.PRecEvaluator;
import fr.inrialpes.exmo.align.parser.AlignmentParser;
import javafx.application.Application;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.RDFS;
import org.semanticweb.owl.align.*;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Created by diego on 07/03/17.
 */
@Service
public class AlignmentService {

    private MatcherRepository matcherRepository;

    private OntologyService ontologyService;

    private RestTemplate restTemplate;

    private AlignmentRepository alignmentRepository;

    private ApplicationProperties properties;

    public AlignmentService(MatcherRepository matcherRepository, OntologyService ontologyService, RestTemplate restTemplate, AlignmentRepository alignmentRepository, ApplicationProperties properties) {
        this.matcherRepository = matcherRepository;
        this.ontologyService = ontologyService;
        this.restTemplate = restTemplate;
        this.alignmentRepository = alignmentRepository;
        this.properties = properties;
    }

    public Alignment align(String ontology1Path, String ontology2Path, String matcherName) throws InvalidOntologyFileException, MatcherNotFoundException, AlignmentException {

        Ontology ontology1 = ontologyService.loadOntology(ontology1Path);
        Ontology ontology2 = ontologyService.loadOntology(ontology2Path);

        Matcher matcher = matcherRepository.findByName(matcherName);

        if (matcher == null) {
            throw new MatcherNotFoundException(String.format("Matcher with name %s not found", matcherName));
        }

        MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
        parameters.add("source", ontology1Path);
        parameters.add("target", ontology2Path);

        for(Map.Entry<String, Object> configParameter : matcher.getConfigurationParameters().entrySet() ) {
            parameters.add(configParameter.getKey(), configParameter.getValue());
        }

        final HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(parameters,null);

        Alignment alignment = restTemplate.exchange(matcher.getEndPoint() + "/match", HttpMethod.POST, entity, Alignment.class).getBody();

        alignment.setOntology1(ontology1);
        alignment.setOntology2(ontology2);
        alignment.setMatcher(matcher);

        alignmentRepository.save(alignment);

        return alignment;

    }

    public Properties evaluate(String alignmentId, String referenceAlignmentFile) throws AlignmentNotFoundException, AlignmentException {
        Alignment alignmentToEvaluate = alignmentRepository.findOne(alignmentId);
        if (alignmentToEvaluate == null) {
            throw new AlignmentNotFoundException(String.format("Alignment with id %s not found"));
        }

        AlignmentParser aparser = new AlignmentParser(0);
        org.semanticweb.owl.align.Alignment reference = aparser.parse( new File( referenceAlignmentFile ).toURI() );

        Evaluator evaluator = new PRecEvaluator( reference, alignmentToEvaluate.toBasicAlignment() );
        evaluator.eval(null);

        return evaluator.getResults();

    }

}
