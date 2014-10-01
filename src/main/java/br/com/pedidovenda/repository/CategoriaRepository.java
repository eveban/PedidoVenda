package br.com.pedidovenda.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.pedidovenda.model.Categoria;

public class CategoriaRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager entityManager;

	public List<Categoria> buscarCategoriasRaizes() {
		return entityManager.createQuery(
				"from Categoria as c where c.categoriaPai is null",
				Categoria.class).getResultList();
	}

	public Categoria buscaPorId(Long id) {
		return entityManager.find(Categoria.class, id);
	}

	public List<Categoria> buscarSubCategorias(Categoria categoriaPai) {
		return entityManager
				.createQuery(
						"from Categoria where categoriaPai = :categoriaPai",
						Categoria.class)
				.setParameter("categoriaPai", categoriaPai).getResultList();
	}

}
