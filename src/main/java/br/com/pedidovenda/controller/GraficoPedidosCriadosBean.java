package br.com.pedidovenda.controller;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

import br.com.pedidovenda.model.Usuario;
import br.com.pedidovenda.repository.PedidoRepository;
import br.com.pedidovenda.security.UsuarioLogado;
import br.com.pedidovenda.security.UsuarioSistema;

@Named
@RequestScoped
public class GraficoPedidosCriadosBean {

	private static DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM");

	private CartesianChartModel model;

	@Inject
	private PedidoRepository pedidoRepository;

	@Inject
	@UsuarioLogado
	private UsuarioSistema usuarioLogado;
	

	public void preRender() {
		this.model = new CartesianChartModel();

		adicionarSerie("Todos os pedidos", null);
		adicionarSerie("Meus pedidos", usuarioLogado.getUsuario());
	}

	private void adicionarSerie(String rotulo, Usuario criadoPor) {

		Map<Date, BigDecimal> valoresPorData = this.pedidoRepository
				.valoresTotaisPorData(15, criadoPor);
		ChartSeries series = new ChartSeries(rotulo);

		for (Date data : valoresPorData.keySet()) {
			series.set(DATE_FORMAT.format(data), valoresPorData.get(data));
		}

		this.model.addSeries(series);
	}

	public CartesianChartModel getModel() {
		return model;
	}

}
