package com.sosa.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class PrestamoEventListener implements ApplicationListener<PrestamoServiceEvent> {
	private final Logger LOGGER = LoggerFactory.getLogger(PrestamoEventListener.class);

	public void onApplicationEvent(PrestamoServiceEvent event) {
		PrestamoServiceEvent prestamoEvent = event;
		LOGGER.info("Prestamo {} with details : {}", event.getEventType(), prestamoEvent.getPrestamo());
	}
}