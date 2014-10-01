/*Classe Responsável pelas regras de Negócio*/
package br.com.pedidovenda.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.pedidovenda.model.Cliente;
import br.com.pedidovenda.repository.ClienteRepository;
import br.com.pedidovenda.util.jpa.Transactional;

public class ClienteService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ClienteRepository clienteRepository;

	@Transactional
	public Cliente salvar(Cliente cliente) {
		Cliente cnpjExistente = clienteRepository.porCNPJ(cliente
				.getDocumentoReceitaFederal());
		if (cnpjExistente != null && !cnpjExistente.equals(cliente)) {
			throw new NegocioException(
					"Já existe um cliente com o CPF/CNPJ cadastrado");
		}
		return clienteRepository.guardar(cliente);
	}

}
