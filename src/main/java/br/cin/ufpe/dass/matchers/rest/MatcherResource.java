package br.cin.ufpe.dass.matchers.rest;

import br.cin.ufpe.dass.matchers.core.Matcher;
import br.cin.ufpe.dass.matchers.repository.MatcherRepository;
import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by diego on 08/03/17.
 */
@RestController
@RequestMapping("/api/")
public class MatcherResource {

    private final MatcherRepository matcherRepository;

    public MatcherResource(MatcherRepository matcherRepository) {
        this.matcherRepository = matcherRepository;
    }

    @PostMapping("/matcher")
    public ResponseEntity<Matcher> addMatcher(@RequestBody Matcher matcher) {
        matcher = matcherRepository.save(matcher);
        return ResponseEntity.ok(matcher);
    }

    @PutMapping("/matcher")
    public ResponseEntity<Matcher> updateMatcher(@RequestBody Matcher matcher) {
        matcher = matcherRepository.save(matcher);
        return ResponseEntity.ok(matcher);
    }

    @DeleteMapping("/matcher/{name}")
    public ResponseEntity<Matcher> deleteMatcher(@PathVariable("name") String name) {
        Matcher matcher = matcherRepository.findByName(name);
        matcherRepository.delete(matcher);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/matcher")
    public ResponseEntity<java.util.List<Matcher>> listMatchers() {
        return ResponseEntity.ok().body(matcherRepository.findAll());
    }

    @GetMapping("/matcher/{name}")
    public ResponseEntity<Matcher> findMatcher(@PathVariable("name") String name) {
        return ResponseEntity.ok().body(matcherRepository.findByName(name));
    }

}
