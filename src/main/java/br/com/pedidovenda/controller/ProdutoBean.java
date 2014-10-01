package br.com.pedidovenda.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import br.com.pedidovenda.model.Categoria;
import br.com.pedidovenda.model.Produto;
import br.com.pedidovenda.repository.CategoriaRepository;
import br.com.pedidovenda.repository.ProdutoRepository;
import br.com.pedidovenda.repository.filter.ProdutoFilter;
import br.com.pedidovenda.service.CadastroProdutoService;
import br.com.pedidovenda.util.jsf.FacesUtil;

@Named("produtoBean")
@ViewScoped
public class ProdutoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private CategoriaRepository categoriaRepository;

	@Inject
	private CadastroProdutoService cadastroProdutoService;

	@NotNull
	private Categoria categoria;

	private Produto produto;

	private List<Categoria> categoriasRaizes;
	private List<Categoria> subCategorias;
	private List<Produto> produtosFiltrados;

	@Inject
	private ProdutoRepository produtoRepository;
	private ProdutoFilter filtro;

	public boolean isEditando() {
		return this.produto.getId() != null;
	}

	public ProdutoBean() {
		limpar();
		filtro = new ProdutoFilter();
	}

	public void inicializar() {
		System.out.println("Inicializando...");
		/*
		 * Classe FacesUtil - isNotPostBack - apenas para carregar o Combobox
		 * somente 1 vez, não fica refazendo consulta toda vez que clicamos em
		 * salvar
		 */
		if (FacesUtil.isNotPostback()) {
			categoriasRaizes = categoriaRepository.buscarCategoriasRaizes();
			// preenchendo o combo de subcategorias
			if (this.categoria != null) {
				carregarSubCategorias();
			}
		}
	}

	private void limpar() {
		produto = new Produto();
		categoria = null;
		subCategorias = new ArrayList<>();
	}

	public void salvar() {
		this.produto = cadastroProdutoService.salvar(this.produto);
		FacesUtil.addInfoMessage("Produto salvo com sucesso!");
		limpar();
	}

	public void excluir() {
		produtoRepository.remover(produto);
		produtosFiltrados.remove(produto);
		FacesUtil.addInfoMessage("Produto "+ produto.getNome() + " excluído com sucesso!");	
	}

	public void carregarSubCategorias() {
		subCategorias = categoriaRepository.buscarSubCategorias(categoria);
	}

	public void pesquisarProdutos() {
		produtosFiltrados = produtoRepository.filtrado(filtro);
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
		// preencher o selectOneMenu na Edição
		if (this.produto != null) {
			this.categoria = this.produto.getCategoria().getCategoriaPai();
		}

	}

	public List<Categoria> getCategoriasRaizes() {
		return categoriasRaizes;
	}

	public List<Categoria> getSubCategorias() {
		return subCategorias;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public List<Produto> getProdutosFiltrados() {
		return produtosFiltrados;
	}

	public ProdutoFilter getFiltro() {
		return filtro;
	}

}
