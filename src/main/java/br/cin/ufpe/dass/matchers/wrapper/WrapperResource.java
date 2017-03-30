package br.cin.ufpe.dass.matchers.wrapper;

import br.cin.ufpe.dass.matchers.core.Alignment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Created by diego on 29/03/17.
 */
public interface WrapperResource {

    public ResponseEntity<Alignment> match(MatcherParameters matcherParameters);

}
