package br.cin.ufpe.dass.matchers.rest;

import br.cin.ufpe.dass.matchers.core.Ontology;
import br.cin.ufpe.dass.matchers.core.OntologyProfile;
import br.cin.ufpe.dass.matchers.exception.InvalidOntologyFileException;
import br.cin.ufpe.dass.matchers.repository.OntologyRepository;
import br.cin.ufpe.dass.matchers.service.OntologyProfileService;
import br.cin.ufpe.dass.matchers.service.OntologyService;
import br.cin.ufpe.dass.matchers.util.HeaderUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Created by diego on 08/03/17.
 */
@RestController
@RequestMapping("/api/")
public class OntologyResource {

    private final OntologyService ontologyService;

    private final OntologyRepository ontologyRepository;

    private final OntologyProfileService ontologyProfileService;

    public OntologyResource(OntologyService ontologyService, OntologyRepository ontologyRepository, OntologyProfileService ontologyProfileService) {
        this.ontologyService = ontologyService;
        this.ontologyRepository = ontologyRepository;
        this.ontologyProfileService = ontologyProfileService;
    }

    @GetMapping("/ontologies")
    public List<Ontology> listOntologies() {
        return ontologyRepository.findAll();
    }

    @GetMapping("/ontologies/{id}")
    public ResponseEntity<Ontology> findOntology(@PathVariable("id") String id) {
        return ResponseEntity.ok().body(ontologyRepository.findOne(id));
    }

    @PostMapping("/ontologies/profile/{id}")
    public ResponseEntity<OntologyProfile> generateProfile(@PathVariable("id") String id) {
        return ResponseEntity.ok().body(ontologyProfileService.generateOntologyProfile(id));
    }

    @PostMapping("/ontologies")
    public ResponseEntity<Ontology> importOntology(@RequestBody String path) {
        Ontology ontology = null;
        try {
            ontology = ontologyService.loadOntology(path);
        } catch (InvalidOntologyFileException e) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("ontology", "ontology-import-failed", String.format("Invalid ontology file path [%s]", path))).body(null);
        }
        return ResponseEntity.ok().body(ontology);
    }

}
