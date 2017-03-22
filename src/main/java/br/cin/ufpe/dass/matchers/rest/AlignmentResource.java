package br.cin.ufpe.dass.matchers.rest;

import br.cin.ufpe.dass.matchers.core.Alignment;
import br.cin.ufpe.dass.matchers.exception.InvalidOntologyFileException;
import br.cin.ufpe.dass.matchers.exception.MatcherNotFoundException;
import br.cin.ufpe.dass.matchers.repository.AlignmentRepository;
import br.cin.ufpe.dass.matchers.service.AlignmentService;
import br.cin.ufpe.dass.matchers.service.OntologyService;
import br.cin.ufpe.dass.matchers.util.HeaderUtil;
import org.semanticweb.owl.align.AlignmentException;
import org.semanticweb.owlapi.model.IRI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by diego on 08/03/17.
 */
@RestController
@RequestMapping("/api/")
public class AlignmentResource {

    private final OntologyService ontologyService;

    private final AlignmentService alignmentService;

    private final AlignmentRepository alignmentRepository;

    public AlignmentResource(OntologyService ontologyService, AlignmentService alignmentService, AlignmentRepository alignmentRepository) {
        this.ontologyService = ontologyService;
        this.alignmentService = alignmentService;
        this.alignmentRepository = alignmentRepository;
    }

    @PostMapping("/alignment")
    public ResponseEntity<Alignment> align(@RequestParam("ontology1") String ontology1Path, @RequestParam("ontology2") String ontology2Path, @RequestParam("matcher") String matcher) {

        Alignment alignment = null;

        try {
            alignment = alignmentService.align(IRI.create(ontology1Path).toURI(), IRI.create(ontology2Path).toURI(), matcher);
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

    @GetMapping("/alignment")
    public ResponseEntity<java.util.List<Alignment>> listAlignments() {
        return ResponseEntity.ok().body(alignmentRepository.findAll());
    }

    @GetMapping("/alignment/{id}")
    public ResponseEntity<Alignment> findAlignment(@PathVariable("id") String id) {
        return ResponseEntity.ok().body(alignmentRepository.findOne(id));
    }



}
