package com.sosa.controller;

import static com.sosa.util.Constants.BUSINESS_MSG_ERR_P_005;
import static com.sosa.util.Constants.BUSINESS_MSG_ERR_P_012;
import static com.sosa.util.Constants.BUSINESS_MSG_ERR_P_013;
import static com.sosa.util.Constants.BUSINESS_MSG_ERR_P_014;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sosa.event.AbstractRestHandler;
import com.sosa.event.PrestamoServiceEvent;
import com.sosa.exception.HTTP400Exception;
import com.sosa.exception.HTTP500Exception;
import com.sosa.model.PrestamoModelAssembler;
import com.sosa.model.dto.PagingDTO;
import com.sosa.model.dto.PrestamoDTO;
import com.sosa.service.PrestamoService;

@RestController
public class PrestamoController extends AbstractRestHandler {

	@Autowired
	private PrestamoService prestamoService;
	
	@Autowired
	private PrestamoModelAssembler prestamoModelAssembler;
	
	@Autowired
	private PagedResourcesAssembler<PrestamoDTO> pagedResourcesAssembler;

	@PostMapping(value = "/prestamos/v1", consumes = { "application/json", "application/xml" }, produces = { "application/json", "application/xml" })
	@ResponseStatus(HttpStatus.CREATED)
	public PrestamoDTO createPrestamo(@Valid @RequestBody PrestamoDTO dto, HttpServletRequest request, HttpServletResponse response) {
		dto = (this.prestamoService.savePrestamo(dto)).orElseThrow(
				() -> new HTTP500Exception(BUSINESS_MSG_ERR_P_012));
		eventPublisher.publishEvent(new PrestamoServiceEvent(this, "PrestamoCreado", dto));
		response.setHeader("Location", request.getRequestURL().append("/").append(dto.getIdPrestamo()).toString());
		return dto;
	}

	@GetMapping(value = "/prestamos/v1", produces = { "application/hal+json" })
	@ResponseStatus(HttpStatus.OK)
	public PagedModel<PrestamoDTO> getAllPrestamos(
			@RequestParam(value = "page", required = true, defaultValue = DEFAULT_PAGE_NUM) Integer page,
			@RequestParam(value = "size", required = true, defaultValue = DEFAULT_PAGE_SIZE) Integer size,
			@RequestParam(value = "property", required = true, defaultValue = DEFAULT_PAGE_PROPERTY) String prop,
			@RequestParam(value = "sortOrder", required = true, defaultValue = DEFAULT_PAGE_ORDER) String sortOrder,
			HttpServletRequest request, HttpServletResponse response) {
		Page<PrestamoDTO> prestamoPage =  this.prestamoService.findAllPrestamos(
				new PagingDTO(page, size, Direction.fromString(sortOrder.toUpperCase()), prop));
		return pagedResourcesAssembler.toModel(prestamoPage, prestamoModelAssembler);
	}

	@GetMapping(value = "/prestamos/v1/{id}", produces = { "application/hal+json" })
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody Optional<PrestamoDTO> getPrestamo(@PathVariable("id") @NotBlank String id, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Optional <PrestamoDTO> prestamo = this.prestamoService.findPrestamo(id);
		return checkResourceFound(prestamo);
	}

	@PutMapping(value = "/prestamos/v1/{id}", consumes = { "application/json", "application/xml" }, produces = { "application/json", "application/xml" })
	@ResponseStatus(HttpStatus.OK)
	public PrestamoDTO updatePrestamo(@PathVariable("id") String id, @Valid @RequestBody PrestamoDTO dto, HttpServletRequest request,
			HttpServletResponse response) {
		if (!id.equals(dto.getIdPrestamo()))
			throw new HTTP400Exception(BUSINESS_MSG_ERR_P_005);
		PrestamoDTO objetoActualizado = this.prestamoService.updatePrestamo(dto).orElseThrow(
				() -> new HTTP500Exception(BUSINESS_MSG_ERR_P_013));
		eventPublisher.publishEvent(new PrestamoServiceEvent(this, "PrestamoActualizado", objetoActualizado));
		return objetoActualizado;
	}

	@DeleteMapping(value = "/prestamos/v1/{id}", produces = { "application/json", "application/xml" })
	@ResponseStatus(HttpStatus.OK)
	public PrestamoDTO deletePrestamo(@PathVariable("id") @NotBlank String id, HttpServletRequest request, HttpServletResponse response) {
		PrestamoDTO objetoEliminado = this.prestamoService.deletePrestamo(id).orElseThrow(
				() -> new HTTP500Exception(BUSINESS_MSG_ERR_P_014));
		eventPublisher.publishEvent(new PrestamoServiceEvent(this, "PrestamoEliminado", objetoEliminado));
		return objetoEliminado;
	}
}
