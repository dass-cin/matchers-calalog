package br.cin.ufpe.dass.matchers.rest;

import br.cin.ufpe.dass.matchers.core.Alignment;
import br.cin.ufpe.dass.matchers.exception.AlignmentNotFoundException;
import br.cin.ufpe.dass.matchers.exception.InvalidOntologyFileException;
import br.cin.ufpe.dass.matchers.exception.MatcherNotFoundException;
import br.cin.ufpe.dass.matchers.repository.AlignmentRepository;
import br.cin.ufpe.dass.matchers.service.AlignmentService;
import br.cin.ufpe.dass.matchers.service.OntologyService;
import br.cin.ufpe.dass.matchers.util.HeaderUtil;
import fr.inrialpes.exmo.align.impl.BasicAlignment;
import org.semanticweb.owl.align.AlignmentException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Properties;

/**
 * Created by diego on 08/03/17.
 */
@RestController
@RequestMapping("/api/")
public class AlignmentEvaluationResource {

    private final OntologyService ontologyService;

    private final AlignmentService alignmentService;

    public AlignmentEvaluationResource(OntologyService ontologyService, AlignmentService alignmentService) {
        this.ontologyService = ontologyService;
        this.alignmentService = alignmentService;
    }

    @PostMapping("/evaluation")
    public ResponseEntity<Properties> evaluate(@RequestParam("alignmentId") String alignmentId, @RequestParam("referenceAlignment") String referenceAlignmentPath) {
        Properties alignment = null;
        try {
            alignment = alignmentService.evaluate(alignmentId, referenceAlignmentPath);
        } catch (AlignmentNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("alignment", "alignment-not-found", "Alignment not found")).body(null);
        } catch (AlignmentException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("alignment", "alignment-exception", "Alignment exception")).body(null);
        }
        return ResponseEntity.ok().body(alignment);
    }

}
