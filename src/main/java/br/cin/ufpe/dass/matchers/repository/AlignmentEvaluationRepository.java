package br.cin.ufpe.dass.matchers.repository;

import br.cin.ufpe.dass.matchers.core.Alignment;
import br.cin.ufpe.dass.matchers.core.AlignmentEvaluation;
import br.cin.ufpe.dass.matchers.core.Matcher;
import br.cin.ufpe.dass.matchers.core.Ontology;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Set;

/**
 * Created by diego on 07/03/17.
 */
public interface AlignmentEvaluationRepository extends MongoRepository<AlignmentEvaluation, String> {

    public Set<AlignmentEvaluation> findByMatcher(Matcher matcher);

}
