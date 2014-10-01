package br.com.pedidovenda.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.pedidovenda.model.Produto;
import br.com.pedidovenda.repository.filter.ProdutoFilter;
import br.com.pedidovenda.service.NegocioException;
import br.com.pedidovenda.util.jpa.Transactional;

public class ProdutoRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager entityManager;

	public Produto guardar(Produto produto) {
		/*
		 * Usando Interceptador - configurar beans.xml EntityTransaction
		 * entityTransaction = entityManager.getTransaction();
		 * entityTransaction.begin();
		 * 
		 * produto = entityManager.merge(produto);
		 * 
		 * entityTransaction.commit();
		 */
		return entityManager.merge(produto);

	}

	@Transactional
	public void remover(Produto produto) {
		try {
			produto = buscaPorId(produto.getId());
			entityManager.remove(produto);
			entityManager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Não foi possível remover o produto.");
		}
	}

	public Produto porSku(String sku) {
		try {

			return entityManager
					.createQuery("from Produto where upper(sku) = :sku",
							Produto.class)
					.setParameter("sku", sku.toUpperCase()).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Produto> filtrado(ProdutoFilter filtro) {
		Session session = entityManager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Produto.class);

		/* pom.xml - ArtifactId = commons-lang3 */
		if (StringUtils.isNotBlank(filtro.getSku())) {
			criteria.add(Restrictions.eq("sku", filtro.getSku()));
		}

		if (StringUtils.isNotBlank(filtro.getNome())) {
			criteria.add(Restrictions.ilike("nome", filtro.getNome(),
					MatchMode.ANYWHERE));
		}
		return criteria.addOrder(Order.asc("nome")).list();

	}

	public Produto buscaPorId(Long id) {
		return entityManager.find(Produto.class, id);
	}

	public List<Produto> porNome(String nome) {
		return this.entityManager
				.createQuery("from Produto where upper(nome) like :nome",
						Produto.class)
				.setParameter("nome", nome.toUpperCase() + "%").getResultList();
	}

}
