package com.sosa.util;

import org.modelmapper.Converter;
import org.modelmapper.PropertyMap;
import org.modelmapper.spi.MappingContext;

import com.sosa.model.Cliente;
import com.sosa.model.Prestamo;
import com.sosa.model.dto.PrestamoDTO;

public class CustomConverter {
	
	public static Converter<String, Cliente> clienteConverter = new Converter<String, Cliente>() {
        public Cliente convert(MappingContext<String, Cliente> context) {
        	Cliente cliente = new Cliente();
        	cliente.setIdCliente(context.getSource());
            return cliente;
        }
    };
    
    public static PropertyMap<PrestamoDTO, Prestamo> movimientoMap = new PropertyMap<PrestamoDTO, Prestamo>() {
	    protected void configure() {
	        using(clienteConverter).map(source.getIdCliente()).setCliente(null);
	    }
    };
    
    public static Converter<Cliente, String> clienteToStringConverter = new Converter<Cliente, String>() {
        public String convert(MappingContext<Cliente, String> context) {
            return context.getSource().getIdCliente();
        }
    };
    
    public static PropertyMap<Prestamo, PrestamoDTO> movimientoDTOMap = new PropertyMap<Prestamo, PrestamoDTO>() {
	    protected void configure() {
	        using(clienteToStringConverter).map(source.getCliente()).setIdCliente(null);
	    }
    };
}