import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import br.com.pedidovenda.model.Pedido;
import br.com.pedidovenda.model.Usuario;
import br.com.pedidovenda.model.vo.DataValor;

public class TesteGrafico {

	public static void main(String[] args) {
		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("PedidoPU");
		EntityManager entityManager = factory.createEntityManager();
		Session session = entityManager.unwrap(Session.class);

		entityManager.close();
	}

	@SuppressWarnings("unchecked")
	public static Map<Date, BigDecimal> valoresTotaisPorData(
			Integer numeroDias, Usuario criadoPor, Session session) {
		// pegar numero de dias correto, pois o numeroDias aparece sempre com um
		// dia a mais.
		numeroDias -= 1;

		// Data Inicial = Hoje
		Calendar dataInicial = Calendar.getInstance();
		// Truncado a data para não pegar HH:mm:ss
		dataInicial = DateUtils.truncate(dataInicial, Calendar.DAY_OF_MONTH);
		/*
		 * adicionando um valor negativo para pegar data retroativa para
		 * montagem do gráfico. Transformando a numeroDias em valor negativo Ex:
		 * Data atual - numeroDias
		 */
		dataInicial.add(Calendar.DAY_OF_MONTH, numeroDias * -1);

		// Criando um Mapa vazio
		Map<Date, BigDecimal> resultado = criarMapaVazio(numeroDias,
				dataInicial);

		Criteria criteria = session.createCriteria(Pedido.class);

		// Para utilizar o Sum ou Max - usamos Projeção - Ex: select
		// date(data_criacao) as data, sum(valor_total) as valor from pedido
		// where... group by date(data_criacao)
		criteria.setProjection(
				Projections
						.projectionList()
						.add(Projections.sqlGroupProjection(
								"date(data_criacao) as data",
								"date(data_criacao)", new String[] { "data" },
								new Type[] { StandardBasicTypes.DATE }))
						.add(Projections.sum("valorTotal").as("valor"))).add(
				Restrictions.ge("dataCriacao", dataInicial.getTime()));

		if (criadoPor != null) {
			criteria.add(Restrictions.eq("vendedor", criadoPor));
		}
		List<DataValor> valoresPorData = criteria.setResultTransformer(
				Transformers.aliasToBean(DataValor.class)).list();

		for (DataValor dataValor : valoresPorData) {
			resultado.put(dataValor.getData(), dataValor.getValor());

		}

		return resultado;

	}

	// Se não houve pedido no determinado dia, a linha do gráfico precisa
	// descer no zero
	private static Map<Date, BigDecimal> criarMapaVazio(Integer numeroDias,
			Calendar dataInicial) {

		// Clonando, senão a dataInciail.add não retorna nada
		dataInicial = (Calendar) dataInicial.clone();
		Map<Date, BigDecimal> mapaInicial = new TreeMap<>();

		for (int i = 0; i <= numeroDias; i++) {
			mapaInicial.put(dataInicial.getTime(), BigDecimal.ZERO);
			dataInicial.add(Calendar.DAY_OF_MONTH, 1);
		}

		return mapaInicial;
	}
}
