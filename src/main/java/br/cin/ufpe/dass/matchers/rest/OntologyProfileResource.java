package br.cin.ufpe.dass.matchers.rest;

import br.cin.ufpe.dass.matchers.core.Ontology;
import br.cin.ufpe.dass.matchers.core.OntologyProfile;
import br.cin.ufpe.dass.matchers.repository.OntologyRepository;
import br.cin.ufpe.dass.matchers.service.OntologyProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by diego on 08/03/17.
 */
@RestController
@RequestMapping("/api/")
public class OntologyProfileResource {

    private final OntologyProfileService ontologyProfileService;

    private final OntologyRepository ontologyRepository;

    public OntologyProfileResource(OntologyProfileService ontologyProfileService, OntologyRepository ontologyRepository) {
        this.ontologyProfileService = ontologyProfileService;
        this.ontologyRepository = ontologyRepository;
    }

    @PostMapping("/ontology-profiles/{id}")
    public ResponseEntity<OntologyProfile> generateProfile(@PathVariable("id") String id) {
        Ontology ontology = ontologyRepository.findOne(id);
        OntologyProfile profile = ontologyProfileService.generateOntologyProfile(ontology);
        return ResponseEntity.ok(profile);
    }

}
