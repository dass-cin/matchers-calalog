package br.cin.ufpe.dass.matchers.repository;

import br.cin.ufpe.dass.matchers.core.Matcher;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by diego on 07/03/17.
 */
public interface MatcherRepository extends MongoRepository<Matcher, String> {

    public Matcher findByName(String name);

}
