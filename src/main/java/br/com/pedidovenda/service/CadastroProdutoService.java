/*Classe Responsável pelas regras de Negócio*/
package br.com.pedidovenda.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.pedidovenda.model.Produto;
import br.com.pedidovenda.repository.ProdutoRepository;
import br.com.pedidovenda.util.jpa.Transactional;

public class CadastroProdutoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ProdutoRepository produtoRepository;

	@Transactional
	public Produto salvar(Produto produto) {
		Produto produtoExistente = produtoRepository.porSku(produto.getSku());
		if (produtoExistente != null && !produtoExistente.equals(produto)) {
			throw new NegocioException("Já existe produto com o sku informado");
		}
		return produtoRepository.guardar(produto);
	}

}
