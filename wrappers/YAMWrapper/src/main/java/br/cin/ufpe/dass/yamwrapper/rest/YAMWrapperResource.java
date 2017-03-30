package br.cin.ufpe.dass.yamwrapper.rest;

import br.cin.ufpe.dass.yamwrapper.MatcherParameters;
import br.cin.ufpe.dass.yamwrapper.service.YAMWrapperService;
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
    public ResponseEntity<Void> match(@RequestBody MatcherParameters matcherParameters) {

        try {
            yamWrapperService.match(matcherParameters.getSource(), matcherParameters.getTarget());
        } catch (IOException e) {
            logger.error("ioexception", e);
            e.printStackTrace();
        }


        return null;
    }
}
