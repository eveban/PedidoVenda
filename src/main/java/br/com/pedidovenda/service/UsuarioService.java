/*Classe Responsável pelas regras de Negócio*/
package br.com.pedidovenda.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.pedidovenda.model.Usuario;
import br.com.pedidovenda.repository.UsuarioRepository;
import br.com.pedidovenda.util.jpa.Transactional;

public class UsuarioService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private UsuarioRepository usuarioRepository;

	@Transactional
	public Usuario salvar(Usuario usuario) {
		Usuario emailExistente = usuarioRepository.porEmail(usuario.getEmail());
		if (emailExistente != null && !emailExistente.equals(usuario)) {
			throw new NegocioException(
					"Já existe um usuário com o e-mail informado");
		}
		return usuarioRepository.guardar(usuario);
	}

}
