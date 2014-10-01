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

import br.com.pedidovenda.model.Cliente;
import br.com.pedidovenda.repository.filter.ClienteFilter;
import br.com.pedidovenda.service.NegocioException;
import br.com.pedidovenda.util.jpa.Transactional;

public class ClienteRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	EntityManager entityManager;

	public Cliente guardar(Cliente cliente) {
		return entityManager.merge(cliente);
	}

	@Transactional
	public void remover(Cliente cliente) {
		try {
			cliente = buscaPorId(cliente.getId());
			entityManager.remove(cliente);
			entityManager.flush();

		} catch (PersistenceException e) {
			throw new NegocioException("Não foi possível remover o produto.");
		}
	}

	public Cliente porCNPJ(String documentoReceitaFederal) {
		try {
			return entityManager
					.createQuery(
							"from Cliente where documentoReceitaFederal = :documentoReceitaFederal",
							Cliente.class)
					.setParameter("documentoReceitaFederal",
							documentoReceitaFederal).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<Cliente> porNome(String nome) {
		return this.entityManager
				.createQuery("from Cliente " + "where upper(nome) like :nome",
						Cliente.class)
				.setParameter("nome", nome.toUpperCase() + "%").getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Cliente> filtrado(ClienteFilter filtro) {
		Session session = entityManager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Cliente.class);
		if (StringUtils.isNotBlank(filtro.getDocumentoReceitaFederal())) {
			criteria.add(Restrictions.eq("documentoReceitaFederal",
					filtro.getDocumentoReceitaFederal()));
		}
		if (StringUtils.isNotBlank(filtro.getNome())) {
			criteria.add(Restrictions.ilike("nome", filtro.getNome(),
					MatchMode.ANYWHERE));
		}
		return criteria.addOrder(Order.asc("nome")).list();
	}

	public Cliente buscaPorId(Long id) {
		return entityManager.find(Cliente.class, id);
	}
}
