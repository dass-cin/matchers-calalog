package br.cin.ufpe.dass.matchers.service;

import br.cin.ufpe.dass.matchers.config.ApplicationProperties;
import br.cin.ufpe.dass.matchers.core.*;
import br.cin.ufpe.dass.matchers.core.Alignment;
import br.cin.ufpe.dass.matchers.exception.AlignmentNotFoundException;
import br.cin.ufpe.dass.matchers.exception.InvalidOntologyFileException;
import br.cin.ufpe.dass.matchers.exception.MatcherNotFoundException;
import br.cin.ufpe.dass.matchers.repository.*;
import br.cin.ufpe.dass.matchers.wrapper.MatcherParameters;
import fr.inrialpes.exmo.align.impl.eval.PRecEvaluator;
import fr.inrialpes.exmo.align.parser.AlignmentParser;
import org.semanticweb.owl.align.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private final AlignmentEvaluationRepository alignmentEvaluationRepository;

    private final CorrespondenceRepository correspondenceRepository;

    public AlignmentService(MatcherRepository matcherRepository, OntologyService ontologyService, OntologyProfileService ontologyProfileService, OntologyRepository ontologyRepository, RestTemplate restTemplate, AlignmentRepository alignmentRepository, ApplicationProperties properties, AlignmentEvaluationRepository alignmentEvaluationRepository, CorrespondenceRepository correspondenceRepository) {
        this.matcherRepository = matcherRepository;
        this.ontologyService = ontologyService;
        this.ontologyProfileService = ontologyProfileService;
        this.ontologyRepository = ontologyRepository;
        this.restTemplate = restTemplate;
        this.alignmentRepository = alignmentRepository;
        this.properties = properties;
        this.alignmentEvaluationRepository = alignmentEvaluationRepository;
        this.correspondenceRepository = correspondenceRepository;
    }

    public Alignment align(URI ontology1Path, URI ontology2Path, String matcherName) throws InvalidOntologyFileException, MatcherNotFoundException, AlignmentException {

        if (!ontology1Path.toString().startsWith("file://")) ontology1Path = URI.create("file://"+ontology1Path.toString());
        if (!ontology2Path.toString().startsWith("file://")) ontology2Path = URI.create("file://"+ontology2Path.toString());

        Ontology ontology1 = ontologyRepository.findByFile(ontology1Path);
        if (ontology1 == null) ontology1 = ontologyService.loadOntology(ontology1);
        if (ontology1 != null && ontology1.getProfile() == null) ontology1 = ontologyProfileService.generateOntologyProfile(ontology1.getId());

        Ontology ontology2 = ontologyRepository.findByFile(ontology2Path);
        if(ontology2 == null) ontology2 = ontologyService.loadOntology(ontology2);
        if (ontology2 != null && ontology2.getProfile() == null) ontology2 = ontologyProfileService.generateOntologyProfile(ontology2.getId());

        Matcher matcher = matcherRepository.findByName(matcherName);

        if (matcher == null) {
            throw new MatcherNotFoundException(String.format("Matcher with name %s not found", matcherName));
        }

        MatcherParameters matcherParameters = new MatcherParameters();
        matcherParameters.setSource(ontology1Path.toString());
        matcherParameters.setTarget(ontology2Path.toString());

        for(Map.Entry<String, Object> configParameter : matcher.getConfigurationParameters().entrySet() ) {
            matcherParameters.getConfigParams().put(configParameter.getKey(), configParameter.getValue());
        }

        final HttpEntity<MatcherParameters> entity = new HttpEntity<>(matcherParameters);

        ResponseEntity<Alignment> response = restTemplate.exchange(matcher.getEndPoint() + "/match", HttpMethod.POST, entity, Alignment.class);

        if (response.getBody() != null && response.getStatusCode() == HttpStatus.OK) {
            Alignment alignment = response.getBody();
            alignment.setOntology1(ontology1);
            alignment.setOntology2(ontology2);
            alignment.setMatcher(matcher);
            alignment.getCorrespondences().forEach((correspondence -> {
                correspondenceRepository.save(correspondence);
            }));
            alignmentRepository.save(alignment);
            return alignment;
        }

        return null;

    }

    public AlignmentEvaluation evaluate(String alignmentId, String referenceAlignmentFile) throws AlignmentNotFoundException, AlignmentException {
        Alignment alignmentToEvaluate = alignmentRepository.findOne(alignmentId);
        if (alignmentToEvaluate == null) {
            throw new AlignmentNotFoundException(String.format("Alignment with id %s not found", alignmentId));
        }

        Properties p = new Properties();

        AlignmentParser aparser = new AlignmentParser(0);

        org.semanticweb.owl.align.Alignment referenceAlignment = aparser.parse(URI.create(referenceAlignmentFile));

        Evaluator evaluator = null;
        evaluator = new PRecEvaluator(referenceAlignment, alignmentToEvaluate.toURIAlignment() );
        evaluator.eval(p);

        Properties results = evaluator.getResults();

        AlignmentEvaluation evaluation = alignmentEvaluationRepository.findByEvaluatedAlignment(alignmentToEvaluate);
        if (evaluation == null) {
            evaluation = new AlignmentEvaluation();
            evaluation.getMetrics().setPrecision(Double.parseDouble(results.getProperty("precision")));
            evaluation.getMetrics().setRecall(Double.parseDouble(results.getProperty("recall")));
            evaluation.getMetrics().setOverall(Double.parseDouble(results.getProperty("overall")));
            evaluation.getMetrics().setFmeasure(Double.parseDouble(results.getProperty("fmeasure")));
            evaluation.getMetrics().setCorrespondencesExpected(Long.parseLong(results.getProperty("nbexpected")));
            evaluation.getMetrics().setCorrespondencesFound(Long.parseLong(results.getProperty("nbfound")));
            evaluation.getMetrics().setTruePositives(Long.parseLong(results.getProperty("true positive")));
            evaluation.setReferenceAlignmentFile(URI.create(referenceAlignmentFile));
            evaluation.setEvaluatedAlignment(alignmentToEvaluate);
            evaluation.setMatcher(alignmentToEvaluate.getMatcher());
            alignmentEvaluationRepository.save(evaluation);
        }

        return evaluation;

    }

}
