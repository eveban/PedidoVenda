package br.com.pedidovenda.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.hibernate.Session;

import br.com.pedidovenda.util.jsf.FacesUtil;
import br.com.pedidovenda.util.report.ExecutorRelatorio;

@Named
@ViewScoped
public class RelatorioProdutosBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private BigDecimal valorInicial;
	private BigDecimal valorFinal;

	@Inject
	private FacesContext facesContext;

	@Inject
	private HttpServletResponse response;

	@Inject
	private EntityManager entityManager;

	public void emitir() {
		Map<String, Object> parametros = new HashMap<>();
		parametros.put("valor_inicial", this.valorInicial);
		parametros.put("valor_final", this.valorFinal);
		ExecutorRelatorio executor = new ExecutorRelatorio(
				"/relatorios/produtos.jasper", this.response, parametros,
				"Produtos.pdf");
		Session session = entityManager.unwrap(Session.class);

		session.doWork(executor);
		if (executor.isRelatorioGerado()) {
			facesContext.responseComplete();
		} else {
			FacesUtil
					.addErrorMessage("A execução do relatório não retornou dados.");
		}

	}

	@NotNull
	public BigDecimal getValorInicial() {
		return valorInicial;
	}

	public void setValorInicial(BigDecimal valorInicial) {
		this.valorInicial = valorInicial;
	}

	@NotNull
	public BigDecimal getValorFinal() {
		return valorFinal;
	}

	public void setValorFinal(BigDecimal valorFinal) {
		this.valorFinal = valorFinal;
	}

}
