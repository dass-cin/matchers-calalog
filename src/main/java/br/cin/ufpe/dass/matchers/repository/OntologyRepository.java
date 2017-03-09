package br.cin.ufpe.dass.matchers.repository;

import br.cin.ufpe.dass.matchers.core.Ontology;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by diego on 07/03/17.
 */
public interface OntologyRepository extends MongoRepository<Ontology, String> {

    public Ontology findByUri(String uri);

    public Ontology findByFile(String file);

}
