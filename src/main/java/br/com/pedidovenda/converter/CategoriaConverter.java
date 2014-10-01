package br.com.pedidovenda.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.pedidovenda.model.Categoria;
import br.com.pedidovenda.repository.CategoriaRepository;
import br.com.pedidovenda.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = Categoria.class)
public class CategoriaConverter implements Converter {

	private CategoriaRepository categoriaRepository;

	public CategoriaConverter() {
		categoriaRepository = CDIServiceLocator
				.getBean(CategoriaRepository.class);
	}

	@Override
	public Object getAsObject(FacesContext faces, UIComponent component,
			String value) {
		Categoria retorno = null;
		if (value != null) {
			Long id = new Long(value);
			retorno = categoriaRepository.buscaPorId(id);
		}
		return retorno;

	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if (value != null) {
			return ((Categoria) value).getId().toString();
		}

		return "";
	}

}
