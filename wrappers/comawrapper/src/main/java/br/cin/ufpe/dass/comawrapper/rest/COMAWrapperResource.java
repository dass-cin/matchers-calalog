package br.cin.ufpe.dass.comawrapper.rest;

import br.cin.ufpe.dass.comawrapper.service.COMAWrapperService;
import br.cin.ufpe.dass.matchers.core.Alignment;
import br.cin.ufpe.dass.matchers.wrapper.MatcherParameters;
import br.cin.ufpe.dass.matchers.wrapper.WrapperResource;
import de.wdilab.coma.matching.Resolution;
import de.wdilab.coma.matching.SimilarityMeasure;
import org.semanticweb.owl.align.AlignmentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by diego on 07/03/17.
 */
@RestController
@RequestMapping("/api/coma/")
public class COMAWrapperResource implements WrapperResource {

    private COMAWrapperService comaWrapperService;

    public COMAWrapperResource(COMAWrapperService comaWrapperService) {
        this.comaWrapperService = comaWrapperService;
    }

    @PostMapping("/match")
    public ResponseEntity<Alignment> match(@RequestBody MatcherParameters parameters) {


        int resolutionInt = 0;
        int measureInt = 0;

        String resolution = (String) parameters.getConfigParams().get("resolution");
        String measure = (String) parameters.getConfigParams().get("measure");


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
            alignment = comaWrapperService.match(parameters.getSource(), parameters.getTarget(), resolutionInt, measureInt);
        } catch (AlignmentException | UnsupportedEncodingException e) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error while building alignment");
        }

        if (alignment == null) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error while executing COMA");
        }

        return ResponseEntity.ok().body(alignment);
    }

}
