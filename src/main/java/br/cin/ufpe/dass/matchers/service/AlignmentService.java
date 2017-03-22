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
import fr.inrialpes.exmo.align.impl.eval.PRecEvaluator;
import fr.inrialpes.exmo.align.parser.AlignmentParser;
import org.semanticweb.owl.align.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;
import java.util.Properties;

/**
 * Created by diego on 07/03/17.
 */
@Service
public class AlignmentService {

    private final MatcherRepository matcherRepository;

    private final OntologyService ontologyService;

    private final OntologyProfileService ontologyProfileService;

    private final OntologyRepository ontologyRepository;

    private final RestTemplate restTemplate;

    private final AlignmentRepository alignmentRepository;

    private final ApplicationProperties properties;

    public AlignmentService(MatcherRepository matcherRepository, OntologyService ontologyService, OntologyProfileService ontologyProfileService, OntologyRepository ontologyRepository, RestTemplate restTemplate, AlignmentRepository alignmentRepository, ApplicationProperties properties) {
        this.matcherRepository = matcherRepository;
        this.ontologyService = ontologyService;
        this.ontologyProfileService = ontologyProfileService;
        this.ontologyRepository = ontologyRepository;
        this.restTemplate = restTemplate;
        this.alignmentRepository = alignmentRepository;
        this.properties = properties;
    }

    public Alignment align(URI ontology1Path, URI ontology2Path, String matcherName) throws InvalidOntologyFileException, MatcherNotFoundException, AlignmentException {

        Ontology ontology1 = ontologyRepository.findByFile(ontology1Path);
        if (ontology1 == null) ontology1 = ontologyService.loadOntology(ontology1Path.toString());
        if (ontology1 != null && ontology1.getProfile() == null) ontology1 = ontologyProfileService.generateOntologyProfile(ontology1.getId());

        Ontology ontology2 = ontologyRepository.findByFile(ontology2Path);
        if(ontology2 == null) ontology2 = ontologyService.loadOntology(ontology2Path.toString());
        if (ontology2 != null && ontology2.getProfile() == null) ontology2 = ontologyProfileService.generateOntologyProfile(ontology2.getId());

        Matcher matcher = matcherRepository.findByName(matcherName);

        if (matcher == null) {
            throw new MatcherNotFoundException(String.format("Matcher with name %s not found", matcherName));
        }

        MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
        parameters.add("source", ontology1Path.toString());
        parameters.add("target", ontology2Path.toString());

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
            throw new AlignmentNotFoundException(String.format("Alignment with id %s not found", alignmentId));
        }

        Properties p = new Properties();

        AlignmentParser aparser = new AlignmentParser(0);

        org.semanticweb.owl.align.Alignment referenceAlignment = aparser.parse(URI.create(referenceAlignmentFile) );

        Evaluator evaluator = null;
        evaluator = new PRecEvaluator(referenceAlignment, alignmentToEvaluate.toURIAlignment() );
        evaluator.eval(p);

        return evaluator.getResults();

    }

}
