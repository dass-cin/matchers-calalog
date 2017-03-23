package br.cin.ufpe.dass.matchers.repository;

import br.cin.ufpe.dass.matchers.core.Matcher;
import br.cin.ufpe.dass.matchers.core.MatcherProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by diego on 07/03/17.
 */
public interface MatcherProfileRepository extends MongoRepository<MatcherProfile, String> {

    public MatcherProfile findByMatcher(Matcher matcher);

}
