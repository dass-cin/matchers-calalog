package br.cin.ufpe.dass.matchers.rest;

import br.cin.ufpe.dass.matchers.core.Alignment;
import br.cin.ufpe.dass.matchers.core.Matcher;
import br.cin.ufpe.dass.matchers.core.Ontology;
import br.cin.ufpe.dass.matchers.exception.AlignmentNotFoundException;
import br.cin.ufpe.dass.matchers.exception.InvalidOntologyFileException;
import br.cin.ufpe.dass.matchers.exception.MatcherNotFoundException;
import br.cin.ufpe.dass.matchers.repository.AlignmentRepository;
import br.cin.ufpe.dass.matchers.service.AlignmentService;
import br.cin.ufpe.dass.matchers.service.OntologyService;
import br.cin.ufpe.dass.matchers.util.HeaderUtil;
import org.apache.catalina.connector.Response;
import org.semanticweb.owl.align.AlignmentException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.OnOpen;

/**
 * Created by diego on 08/03/17.
 */
@RestController
@RequestMapping("/api/")
public class AlignmentResource {

    private OntologyService ontologyService;

    private AlignmentService alignmentService;

    public OntologyService getOntologyService() {
        return ontologyService;
    }

    public void setOntologyService(OntologyService ontologyService) {
        this.ontologyService = ontologyService;
    }

    public AlignmentService getAlignmentService() {
        return alignmentService;
    }

    public void setAlignmentService(AlignmentService alignmentService) {
        this.alignmentService = alignmentService;
    }

    public AlignmentResource(OntologyService ontologyService, AlignmentService alignmentService) {
        this.ontologyService = ontologyService;
        this.alignmentService = alignmentService;
    }

    @PostMapping("/alignment")
    public ResponseEntity<Alignment> align(@RequestParam("ontology1") String ontology1Path, @RequestParam("ontology2") String ontology2Path, @RequestParam("matcher") String matcher) {

        Alignment alignment = null;

        try {
            alignment = alignmentService.align(ontology1Path, ontology2Path, matcher);
        } catch (InvalidOntologyFileException e) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("ontology", "invalid-ontology", "Invalid ontology to align")).body(null);
        } catch (MatcherNotFoundException e) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("matcher", "matcher-not-found", "Matcher not found")).body(null);
        } catch (AlignmentException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("matcher", "matcher-exception", "Matcher Exception")).body(null);

        }

        return ResponseEntity.ok().body(alignment);
    }

}
