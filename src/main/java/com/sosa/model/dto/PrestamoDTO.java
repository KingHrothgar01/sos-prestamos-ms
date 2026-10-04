package com.sosa.model.dto;

import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrestamoDTO extends RepresentationModel<PrestamoDTO> {

	@JsonProperty("id-prestamo")
	@JsonSetter("id-prestamo")
	private String idPrestamo;
	
	@NotNull(message = "El monto es requerido.")
	@Positive(message = "El monto debe ser mayor a 0.")
	private Double monto;
	
	@NotNull(message = "El monto es requerido.")
	@Min(value = 0, message = "El monto debe ser mayor o igual a 0.")
	@JsonProperty("saldo-insoluto")
	@JsonSetter("saldo-insoluto")
	private Double saldoInsoluto;
	
	@NotBlank(message = "El numero de cliente es requerido.")
	@JsonProperty("id-cliente")
	@JsonSetter("id-cliente")
	private String idCliente;

	@JsonProperty("dia-corte")
	@JsonSetter("dia-corte")
	private int diaCorte;
	
	@JsonProperty(value = "fecha-registro", access = JsonProperty.Access.READ_ONLY)
	@JsonSetter("fecha-registro")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private Date fechaRegistro;

	@JsonProperty(value = "fecha-actualizacion", access = JsonProperty.Access.READ_ONLY)
	@JsonSetter("fecha-actualizacion")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private Date fechaActualizacion;

	@JsonProperty(value = "usuario-registra", access = JsonProperty.Access.READ_ONLY)
	@JsonSetter("usuario-registra")
	private String usuarioRegistra;

	@JsonProperty(value = "usuario-actualiza", access = JsonProperty.Access.READ_ONLY)
	@JsonSetter("usuario-actualiza")
	private String usuarioActualiza;
	
	@JsonProperty(value = "activo", access = JsonProperty.Access.READ_ONLY)
	@JsonSetter("activo")
	private Boolean activo;

}
