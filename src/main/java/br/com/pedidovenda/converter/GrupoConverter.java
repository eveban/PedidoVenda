package br.com.pedidovenda.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.pedidovenda.model.Grupo;
import br.com.pedidovenda.repository.GrupoRepository;
import br.com.pedidovenda.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = Grupo.class)
public class GrupoConverter implements Converter {

	private GrupoRepository grupoRepository;

	public GrupoConverter() {
		grupoRepository = CDIServiceLocator.getBean(GrupoRepository.class);
	}

	@Override
	public Object getAsObject(FacesContext faces, UIComponent component,
			String value) {
		Grupo retorno = null;
		if (value != null) {
			Long id = new Long(value);
			retorno = grupoRepository.buscaPorId(id);
		}
		return retorno;

	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if (value != null) {
			Grupo Grupo = (Grupo) value;
			return Grupo.getId() == null ? null : Grupo.getId().toString();

		}

		return "";
	}

}
