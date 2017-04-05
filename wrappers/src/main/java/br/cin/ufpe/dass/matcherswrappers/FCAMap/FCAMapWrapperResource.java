package br.cin.ufpe.dass.matcherswrappers.FCAMap;

import br.cin.ufpe.dass.matchers.core.Alignment;
import br.cin.ufpe.dass.matchers.wrapper.MatcherParameters;
import br.cin.ufpe.dass.matchers.wrapper.exception.AlignmentException;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Created by diego on 07/03/17.
 */
@RestController
@RequestMapping("/api/fcamap/")
public class FCAMapWrapperResource {

    private Logger logger = LoggerFactory.getLogger(FCAMapWrapperResource.class);

    private FCAMapWrapperService fcaMapWrapperService;

    public FCAMapWrapperResource(FCAMapWrapperService fcaMapWrapperService) {
        this.fcaMapWrapperService = fcaMapWrapperService;
    }

    @PostMapping("/match")
    public ResponseEntity<Alignment> match(@RequestBody MatcherParameters matcherParameters) {

        Alignment alignment = null;

        try {
            try {
                alignment = fcaMapWrapperService.match(matcherParameters);
            } catch (OWLOntologyCreationException e) {
                logger.error("OWLOntologyCreationException", e);
                ResponseEntity.badRequest().body(null);
            } catch (AlignmentException e) {
                logger.error("AlignmentException", e);
                ResponseEntity.badRequest().body(null);
            }
        } catch (IOException e) {
            logger.error("IOException", e);
            ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok().body(alignment);
    }
}
