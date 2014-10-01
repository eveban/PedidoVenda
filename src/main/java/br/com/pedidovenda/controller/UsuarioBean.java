package br.com.pedidovenda.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.pedidovenda.model.Grupo;
import br.com.pedidovenda.model.Usuario;
import br.com.pedidovenda.repository.GrupoRepository;
import br.com.pedidovenda.repository.UsuarioRepository;
import br.com.pedidovenda.repository.filter.UsuarioFilter;
import br.com.pedidovenda.service.UsuarioService;
import br.com.pedidovenda.util.jsf.FacesUtil;

@Named
@ViewScoped
public class UsuarioBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Usuario usuario;

	private UsuarioFilter filtro;

	@Inject
	private UsuarioRepository usuarioRepository;

	@Inject
	private UsuarioService usuarioService;

	@Inject
	private GrupoRepository grupoRepository;

	private List<Usuario> usuarioFiltrado;
	private List<Grupo> grupos;
	private Grupo novoGrupo;

	public UsuarioBean() {
		limpar();
	}

	public void pesquisarUsuarios() {
		usuarioFiltrado = usuarioRepository.filtrado(filtro);
	}

	public void salvar() {
		this.usuario = usuarioService.salvar(this.usuario);
		FacesUtil.addInfoMessage("Usuário salvo com sucesso!");
		limpar();
	}

	public void excluir() {
		usuarioRepository.remover(usuario);
		usuarioFiltrado.remove(usuario);
		FacesUtil.addInfoMessage("Usuário " + usuario.getNome()
				+ " excluído com sucesso!");
	}

	private void limpar() {
		usuario = new Usuario();
		filtro = new UsuarioFilter();
		grupos = new ArrayList<>();
	}

	public void adicionarGrupo() {
		this.usuario.getGrupos().add(novoGrupo);
	}

	public void inicializar() {
		if (FacesUtil.isNotPostback()) {
			System.out.println("Carregando combo de Grupos no preRender");
			this.grupos = grupoRepository.buscarGrupos();
		}
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Usuario> getUsuarioFiltrado() {
		return usuarioFiltrado;
	}

	public List<Grupo> getGrupos() {
		return grupos;
	}

	public Grupo getNovoGrupo() {
		return novoGrupo;
	}

	public void setNovoGrupo(Grupo novoGrupo) {
		this.novoGrupo = novoGrupo;
	}

	public UsuarioFilter getFiltro() {
		return filtro;
	}

}
