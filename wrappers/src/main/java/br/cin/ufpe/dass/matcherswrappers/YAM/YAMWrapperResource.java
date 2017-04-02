package br.cin.ufpe.dass.matcherswrappers.YAM;

import br.cin.ufpe.dass.matchers.core.Alignment;
import br.cin.ufpe.dass.matchers.wrapper.MatcherParameters;
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
@RequestMapping("/api/yam/")
public class YAMWrapperResource {

    private Logger logger = LoggerFactory.getLogger(YAMWrapperResource.class);

    private YAMWrapperService yamWrapperService;

    public YAMWrapperResource(YAMWrapperService comaWrapperService) {
        this.yamWrapperService = comaWrapperService;
    }

    @PostMapping("/match")
    public ResponseEntity<Alignment> match(@RequestBody MatcherParameters matcherParameters) {

        Alignment alignment = null;

        try {
            alignment = yamWrapperService.match(matcherParameters.getSource(), matcherParameters.getTarget());
        } catch (IOException e) {
            logger.error("ioexception", e);
            ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok().body(alignment);
    }
}
