package br.com.pedidovenda.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.pedidovenda.model.Usuario;
import br.com.pedidovenda.repository.UsuarioRepository;
import br.com.pedidovenda.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = Usuario.class)
public class UsuarioConverter implements Converter {

	private UsuarioRepository usuarioRepository;

	public UsuarioConverter() {
		usuarioRepository = CDIServiceLocator.getBean(UsuarioRepository.class);
	}

	@Override
	public Object getAsObject(FacesContext faces, UIComponent component,
			String value) {
		Usuario retorno = null;
		if (value != null) {
			Long id = new Long(value);
			retorno = usuarioRepository.buscaPorId(id);
		}
		return retorno;

	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if (value != null) {
			Usuario usuario = (Usuario) value;
			return usuario.getId() == null ? null : usuario.getId().toString();

		}

		return "";
	}

}
