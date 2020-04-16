package api.rest.repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import api.rest.model.Telefone;
import api.rest.model.Usuario;

@Repository
public interface TelefoneRepository extends CrudRepository<Telefone, Long>{

	@Query("select t from Telefone t where t.id = ?1")
	Usuario findByTelefone(Telefone telefone);
}
