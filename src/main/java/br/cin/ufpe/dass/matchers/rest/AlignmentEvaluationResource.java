package br.cin.ufpe.dass.matchers.rest;

import br.cin.ufpe.dass.matchers.core.Alignment;
import br.cin.ufpe.dass.matchers.core.AlignmentEvaluation;
import br.cin.ufpe.dass.matchers.exception.AlignmentNotFoundException;
import br.cin.ufpe.dass.matchers.repository.AlignmentEvaluationRepository;
import br.cin.ufpe.dass.matchers.repository.AlignmentRepository;
import br.cin.ufpe.dass.matchers.service.AlignmentService;
import br.cin.ufpe.dass.matchers.service.OntologyService;
import br.cin.ufpe.dass.matchers.util.HeaderUtil;
import org.semanticweb.owl.align.AlignmentException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by diego on 08/03/17.
 */
@RestController
@RequestMapping("/api/")
public class AlignmentEvaluationResource {

    private final OntologyService ontologyService;

    private final AlignmentService alignmentService;

    private final AlignmentRepository alignmentRepository;

    private final AlignmentEvaluationRepository alignmentEvaluationRepository;

    public AlignmentEvaluationResource(OntologyService ontologyService, AlignmentService alignmentService, AlignmentRepository alignmentRepository, AlignmentEvaluationRepository alignmentEvaluationRepository) {
        this.ontologyService = ontologyService;
        this.alignmentService = alignmentService;
        this.alignmentRepository = alignmentRepository;
        this.alignmentEvaluationRepository = alignmentEvaluationRepository;
    }

    @PostMapping("/alignment/evaluations")
    public ResponseEntity<AlignmentEvaluation> evaluate(@RequestParam("alignmentId") String alignmentId, @RequestParam("referenceAlignment") String referenceAlignmentPath) {
        AlignmentEvaluation alignmentEvaluation = null;
        try {
            alignmentEvaluation = alignmentService.evaluate(alignmentId, referenceAlignmentPath);
        } catch (AlignmentNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("alignment", "alignment-not-found", "Alignment not found")).body(null);
        } catch (AlignmentException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("alignment", "alignment-exception", "Alignment exception")).body(null);
        }
        return ResponseEntity.ok().body(alignmentEvaluation);
    }

    @GetMapping("/alignment/evaluations/{alignmentId}")
    public ResponseEntity<AlignmentEvaluation> findEvaluationByAlignment(String id) {
        Alignment alignment = alignmentRepository.findOne(id);
        if (alignment == null) return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("alignment-evaluation", "alignment-not-found", String.format("Alignment of id %s not found", id))).body(null);
        return ResponseEntity.ok(alignmentEvaluationRepository.findByEvaluatedAlignment(alignment));
    }

}
