package com.sosa.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import io.micrometer.core.instrument.Metrics;

@Aspect
@Configuration
public class ControllerLayerAspects {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ControllerLayerAspects.class);

	@Before("execution (public * com.sosa.controller.*Controller.*(..))")
	public void logBeforeRestCall(JoinPoint join) throws Throwable {
		LOGGER.info("+---- AOP Before REST call ----+" + join);
	}

	@AfterReturning("execution(public * com.sosa.controller.PrestamoController.createPrestamo(..))")
	public void afterCallingCreateTransaction(JoinPoint join) {
		LOGGER.info("+---- AOP @AfterReturning Create REST call ----+" + join);
		Metrics.counter("create_prestamo").increment();
	}

	@AfterReturning("execution(public * com.sosa.controller.PrestamoController.getAllPrestamos(..))")
	public void afterCallingGetAllTransaction(JoinPoint join) {
		LOGGER.info("+---- AOP @AfterReturning get prestamos REST call ----+" + join);
		Metrics.counter("get_prestamos").increment();
	}

	@AfterReturning("execution(public * com.sosa.controller.PrestamoController.getPrestamo(..))")
	public void afterCallingGetTransaction(JoinPoint join) {
		LOGGER.info("+---- AOP @AfterReturning get prestamo REST call ----+" + join);
		Metrics.counter("get_prestamo").increment();
	}

	@AfterReturning("execution(public * com.sosa.controller.PrestamoController.updatePrestamo(..))")
	public void afterCallingUpdateTransaction(JoinPoint join) {
		LOGGER.info("+---- AOP @AfterReturning update prestamo REST call ----+" + join);
		Metrics.counter("update_prestamo").increment();
	}
	
	@AfterReturning("execution(public * com.sosa.controller.PrestamoController.deletePrestamo(..))")
	public void afterCallingDeleteTransaction(JoinPoint join) {
		LOGGER.info("+---- AOP @AfterReturning delete prestamo REST call ----+" + join);
		Metrics.counter("delete_prestamo").increment();
	}

	@AfterThrowing(pointcut = "execution(public * com.sosa.controller.PrestamoController.*(..))", throwing = "e")
	public void afterGetGreetingThrowsException(Exception e) {
		Metrics.counter("prestamo_errors").increment();
	}
}
