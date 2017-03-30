package br.cin.ufpe.dass.matchers.repository;

import br.cin.ufpe.dass.matchers.core.Alignment;
import br.cin.ufpe.dass.matchers.core.Matcher;
import br.cin.ufpe.dass.matchers.core.Ontology;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Set;

/**
 * Created by diego on 07/03/17.
 */
public interface AlignmentRepository extends MongoRepository<Alignment, String> {

    public Alignment findByOntology1AndOntology2AndMatcher(Ontology ontology1, Ontology ontology2, Matcher matcher);

    public Set<Alignment> findByMatcher(Matcher matcher);

}
