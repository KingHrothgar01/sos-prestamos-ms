package com.sosa.service;

import static com.sosa.util.Constants.APPLICATION_MESSAGE_002;
import static com.sosa.util.Constants.APPLICATION_PARAMETER_LARGE_PAGE;
import static com.sosa.util.Constants.BUSINESS_MSG_ERR_P_001;
import static com.sosa.util.Constants.BUSINESS_MSG_ERR_P_003;
import static com.sosa.util.Constants.BUSINESS_MSG_ERR_P_006;
import static com.sosa.util.Constants.BUSINESS_MSG_ERR_P_009;
import static com.sosa.util.Constants.CUSTOM_METRICS_LARGE_PAYLOAD;
import static com.sosa.util.Constants.REGISTRO_ACTIVO;
import static com.sosa.util.Constants.REGISTRO_INACTIVO;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.sosa.exception.HTTP400Exception;
import com.sosa.exception.HTTP404Exception;
import com.sosa.model.Prestamo;
import com.sosa.model.dto.PagingDTO;
import com.sosa.model.dto.PrestamoDTO;
import com.sosa.repository.PrestamoRepository;

import io.micrometer.core.instrument.Metrics;

@Service
public class PrestamoService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PrestamoService.class);
	
	@Autowired
	private PrestamoRepository prestamoRepository;

	public Optional<PrestamoDTO> savePrestamo(PrestamoDTO prestamo) {
		Optional<Prestamo> saved = prestamoRepository.findById(prestamo.getIdPrestamo());
	
		if (saved.isPresent())
			throw new HTTP400Exception(BUSINESS_MSG_ERR_P_001);
		
		ModelMapper mapper = new ModelMapper();
		Prestamo model = mapper.map(prestamo, Prestamo.class);
		model.setFechaRegistro(new Date());
		model.setUsuarioRegistra("admin");
		model.setActivo(REGISTRO_ACTIVO);
		return Optional.ofNullable(mapper.map(prestamoRepository.save(model), PrestamoDTO.class));
	}

	public Optional<PrestamoDTO> findPrestamo(String id) {
		Optional<Prestamo> model = prestamoRepository.findById(id);
		
		if (model.isPresent()) {
			ModelMapper mapper = new ModelMapper();
			return Optional.ofNullable(mapper.map(model.get(), PrestamoDTO.class));
		}else
			throw new HTTP404Exception(BUSINESS_MSG_ERR_P_003);
	}

	public Optional<PrestamoDTO> updatePrestamo(PrestamoDTO prestamo) {
		Optional<Prestamo> saved = prestamoRepository.findById(prestamo.getIdPrestamo());
		
		if (saved.isEmpty())
			throw new HTTP404Exception(BUSINESS_MSG_ERR_P_006);
		
		ModelMapper mapper = new ModelMapper();
		Prestamo model = mapper.map(prestamo, Prestamo.class);
		model.setFechaRegistro(saved.get().getFechaRegistro());
		model.setUsuarioRegistra(saved.get().getUsuarioRegistra());
		model.setFechaActualizacion(new Date());
		model.setUsuarioActualiza("admin");
		model.setActivo(saved.get().getActivo());
		return Optional.of(mapper.map(prestamoRepository.save(model), PrestamoDTO.class));
	}

	public Optional<PrestamoDTO> deletePrestamo(String idPrestamo) {
		Optional<Prestamo> saved = prestamoRepository.findById(idPrestamo);
		
		if (saved.isEmpty())
			throw new HTTP404Exception(BUSINESS_MSG_ERR_P_009);
		
		ModelMapper mapper = new ModelMapper();
		saved.get().setActivo(REGISTRO_INACTIVO);
		saved.get().setFechaActualizacion(new Date());
		saved.get().setUsuarioActualiza("admin");
		Prestamo model = mapper.map(saved, Prestamo.class);
		return Optional.of(mapper.map(prestamoRepository.save(model), PrestamoDTO.class));
	}

	public Page<PrestamoDTO> findAllPrestamos(PagingDTO dto) {		
		Page<Prestamo> pageOfLoans = null;
		
		if (dto.getOrder().isAscending())
			pageOfLoans = prestamoRepository.findAll(PageRequest.of(
					dto.getPage(), dto.getSize(), Sort.by(dto.getProperty()).ascending()));
		else
			pageOfLoans = prestamoRepository.findAll(PageRequest.of(
					dto.getPage(), dto.getSize(), Sort.by(dto.getProperty()).descending()));
		
		if (dto.getSize() > APPLICATION_PARAMETER_LARGE_PAGE) {
			LOGGER.info(APPLICATION_MESSAGE_002);
			Metrics.counter(CUSTOM_METRICS_LARGE_PAYLOAD).increment();
		}
		return transform(pageOfLoans);
	}
	
	private Page<PrestamoDTO> transform (Page<Prestamo> loans) {
		return new PageImpl<PrestamoDTO>(
				getListPrestamos(loans), loans.getPageable(), loans.getTotalElements());
	}
	
	private List<PrestamoDTO> getListPrestamos(Page<Prestamo> page){
		ModelMapper mapper = new ModelMapper();
		return page.getContent().stream().map(
				n -> mapper.map(n, PrestamoDTO.class)).collect(Collectors.toList());
	}
}
