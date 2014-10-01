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

import br.com.pedidovenda.model.Usuario;
import br.com.pedidovenda.repository.filter.UsuarioFilter;
import br.com.pedidovenda.service.NegocioException;
import br.com.pedidovenda.util.jpa.Transactional;

public class UsuarioRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager entityManager;

	public Usuario guardar(Usuario usuario) {
		return entityManager.merge(usuario);
	}

	@Transactional
	public void remover(Usuario usuario) {
		try {
			usuario = buscaPorId(usuario.getId());
			entityManager.remove(usuario);
			entityManager.flush();

		} catch (PersistenceException e) {
			throw new NegocioException("Não foi possível remover o usuário!");
		}
	}

	@SuppressWarnings("unchecked")
	public List<Usuario> filtrado(UsuarioFilter filtro) {
		Session session = entityManager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Usuario.class);

		if (StringUtils.isNotBlank(filtro.getNome())) {
			criteria.add(Restrictions.ilike("nome", filtro.getNome(),
					MatchMode.ANYWHERE));
		}
		return criteria.addOrder(Order.asc("nome")).list();

	}

	public Usuario buscaPorId(Long id) {
		return entityManager.find(Usuario.class, id);
	}

	public Usuario porEmail(String email) {
		try {

			return entityManager
					.createQuery("from Usuario where email = :email",
							Usuario.class).setParameter("email", email)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<Usuario> vendedores() {
		// TODO filtrar apenas vendedores (por um grupo específico)
		return this.entityManager.createQuery("from Usuario", Usuario.class)
				.getResultList();
	}

	/*
	 * public Usuario porId(Long id) { return this.manager.find(Usuario.class,
	 * id); }
	 * 
	 * public List<Usuario> vendedores() { // TODO filtrar apenas vendedores
	 * (por um grupo específico) return this.manager.createQuery("from Usuario",
	 * Usuario.class) .getResultList(); }
	 * 
	 * 
	 * public Usuario porEmail(String email) { Usuario usuario = null;
	 * 
	 * try { usuario =
	 * this.manager.createQuery("from Usuario where lower(email) = :email",
	 * Usuario.class) .setParameter("email",
	 * email.toLowerCase()).getSingleResult(); } catch (NoResultException e) {
	 * // nenhum usuário encontrado com o e-mail informado }
	 * 
	 * return usuario; }
	 */

}
