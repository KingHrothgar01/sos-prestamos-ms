package com.sosa.event;

import org.springframework.context.ApplicationEvent;

import com.sosa.model.dto.PrestamoDTO;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
public class PrestamoServiceEvent extends ApplicationEvent {

	private static final long serialVersionUID = -6121624308782503383L;
	
	private PrestamoDTO prestamo;
	private String eventType;

	public PrestamoServiceEvent(Object source, String eventType, PrestamoDTO eventPrestamo) {
		super(source);
		this.eventType = eventType;
		this.prestamo = eventPrestamo;
	}
	
	@Override
	public String toString() {
		return String.format("PrestamoServiceEvent [evento = {}, eventType = {}]", prestamo.getIdPrestamo(), eventType);
	}
}
