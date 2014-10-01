package br.com.pedidovenda.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.pedidovenda.model.Grupo;

public class GrupoRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager entityManager;

	public Grupo buscaPorId(Long id) {
		return entityManager.find(Grupo.class, id);
	}

	public List<Grupo> buscarGrupos() {
		return entityManager.createQuery("from Grupo as g order by g.nome",
				Grupo.class).getResultList();
	}

}
