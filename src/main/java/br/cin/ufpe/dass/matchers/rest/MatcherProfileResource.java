package br.cin.ufpe.dass.matchers.rest;

import br.cin.ufpe.dass.matchers.core.AlignmentEvaluation;
import br.cin.ufpe.dass.matchers.core.Matcher;
import br.cin.ufpe.dass.matchers.core.MatcherProfile;
import br.cin.ufpe.dass.matchers.repository.AlignmentEvaluationRepository;
import br.cin.ufpe.dass.matchers.repository.MatcherProfileRepository;
import br.cin.ufpe.dass.matchers.repository.MatcherRepository;
import br.cin.ufpe.dass.matchers.util.HeaderUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * Created by diego on 08/03/17.
 */
@RestController
@RequestMapping("/api/")
public class MatcherProfileResource {

    private final MatcherRepository matcherRepository;

    private final AlignmentEvaluationRepository alignmentEvaluationRepository;

    private final MatcherProfileRepository matcherProfileRepository;

    public MatcherProfileResource(MatcherRepository matcherRepository, AlignmentEvaluationRepository alignmentEvaluationRepository, MatcherProfileRepository matcherProfileRepository) {
        this.matcherRepository = matcherRepository;
        this.alignmentEvaluationRepository = alignmentEvaluationRepository;
        this.matcherProfileRepository = matcherProfileRepository;
    }


    @PostMapping("/matcher-profile/{name}")
    public ResponseEntity<MatcherProfile> generateMatcherProfile(@PathVariable("name") String name) {
        Matcher matcher = matcherRepository.findByName(name);
        if (matcher == null) return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("matcher-profile", "matcher-not-found", "Matcher not found")).body(null);

        Set<AlignmentEvaluation> evaluationSet = alignmentEvaluationRepository.findByMatcher(matcher);
        MatcherProfile matcherProfile  = matcherProfileRepository.findByMatcher(matcher);
        if (matcherProfile == null) {
            matcherProfile = new MatcherProfile();
            matcherProfile.setMatcher(matcher);

            int truePositives = 0;
            float precision = 0;
            float recall = 0;
            float fmeasure = 0;
            float overall = 0;
            int cFound = 0;
            int cExpected = 0;
            int totalEvaluations = 0;
            int executionTime = 0;

            for (AlignmentEvaluation evaluation : evaluationSet) {
                executionTime += evaluation.getEvaluatedAlignment().getExecutionTimeInMillis();
                truePositives += evaluation.getMetrics().getTruePositives();
                precision += evaluation.getMetrics().getPrecision();
                recall += evaluation.getMetrics().getRecall();
                fmeasure += evaluation.getMetrics().getFmeasure();
                overall += evaluation.getMetrics().getOverall();
                cFound += evaluation.getMetrics().getCorrespondencesFound();
                cExpected += evaluation.getMetrics().getCorrespondencesExpected();
                totalEvaluations++;
            }

            matcherProfile.getPerformanceMetrics().setExecutionTimeInMillis(executionTime/totalEvaluations);
            matcherProfile.setGeneratedAlignments(totalEvaluations);
            matcherProfile.getComplianceMetrics().setTruePositives(truePositives / totalEvaluations);
            matcherProfile.getComplianceMetrics().setPrecision(precision / totalEvaluations);
            matcherProfile.getComplianceMetrics().setRecall(recall / totalEvaluations);
            matcherProfile.getComplianceMetrics().setFmeasure(fmeasure / totalEvaluations);
            matcherProfile.getComplianceMetrics().setOverall(overall / totalEvaluations);
            matcherProfile.getComplianceMetrics().setCorrespondencesFound(cFound);
            matcherProfile.getComplianceMetrics().setCorrespondencesExpected(cExpected);

            matcherProfileRepository.save(matcherProfile);

        }

        return ResponseEntity.ok(matcherProfile);
    }



}
