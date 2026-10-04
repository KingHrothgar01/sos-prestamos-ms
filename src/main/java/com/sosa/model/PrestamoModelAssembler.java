package com.sosa.model;

import org.modelmapper.ModelMapper;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.sosa.controller.PrestamoController;
import com.sosa.model.dto.PrestamoDTO;

@Component
public class PrestamoModelAssembler extends RepresentationModelAssemblerSupport<PrestamoDTO, PrestamoDTO> {
	
	public PrestamoModelAssembler() {
		super(PrestamoController.class, PrestamoDTO.class);
	}

	@Override
	public PrestamoDTO toModel(PrestamoDTO entity) {
		ModelMapper mapper = new ModelMapper();
		return mapper.map(entity, PrestamoDTO.class);
	}

}
