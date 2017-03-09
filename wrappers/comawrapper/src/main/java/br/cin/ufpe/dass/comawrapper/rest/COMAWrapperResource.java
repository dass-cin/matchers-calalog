package br.cin.ufpe.dass.comawrapper.rest;

import br.cin.ufpe.dass.comawrapper.service.COMAWrapperService;
import br.cin.ufpe.dass.matchers.core.Alignment;
import de.wdilab.coma.matching.Resolution;
import de.wdilab.coma.matching.SimilarityMeasure;
import org.semanticweb.owl.align.AlignmentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by diego on 07/03/17.
 */
@RestController
@RequestMapping("/api/coma/")
public class COMAWrapperResource {

    private COMAWrapperService comaWrapperService;

    public COMAWrapperResource(COMAWrapperService comaWrapperService) {
        this.comaWrapperService = comaWrapperService;
    }

    @PostMapping("/match")
    public ResponseEntity<Alignment> match(@RequestParam("source") String source,
                                           @RequestParam("target") String target,
                                           @RequestParam("resolution") String resolution,
                                           @RequestParam("measure") String measure) {

        int resolutionInt = 0;
        int measureInt = 0;

        try {
            resolutionInt = Resolution.class.getField(resolution).getInt(null);
        } catch (NoSuchFieldException e) {
            ResponseEntity.badRequest().body("Unexistent resolution field");
        } catch (IllegalAccessException e) {
            ResponseEntity.badRequest().body(String.format("Invalid resolution with name %s", resolution));
        }

        try {
            measureInt = SimilarityMeasure.class.getField(measure).getInt(null);
        } catch (NoSuchFieldException e) {
            ResponseEntity.badRequest().body("Unexistent SimilarityMeasure field");
        } catch (IllegalAccessException e) {
            ResponseEntity.badRequest().body(String.format("Invalid measure with name %s", measure));
        }

        Alignment alignment = null;
        try {
            alignment = comaWrapperService.match(source, target, resolutionInt, measureInt);
        } catch (AlignmentException e) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error while building alignment");
            e.printStackTrace();
        }

        if (alignment == null) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error while executing COMA");
        }

        return ResponseEntity.ok().body(alignment);
    }

}
