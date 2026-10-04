package com.sosa.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.sosa.model.Cliente;
import com.sosa.model.Prestamo;

@DataJpaTest
class PrestamosRepositoryTests {
	
	@Autowired
	PrestamoRepository prestamoRepository;
	
	@Autowired
	ClienteRepository clienteRepository; 
	
	private Prestamo prestamoCero;
	
	private Cliente clienteCero;

	@BeforeEach
	void setUp() throws Exception {
		clienteCero = Cliente.builder()
				.idCliente("0000000001")
				.nombre("Jose Juan")
				.apellidoPaterno("Sosa")
				.apellidoMaterno("Romero")
				.rfc("SORJ8204085P0")
				.curp("SORJ820408HDF")
				.fechaRegistro(new Date())
				.fechaActualizacion(null)
				.usuarioRegistra("s1209465")
				.usuarioActualiza(null)
				.build();
				
		prestamoCero = Prestamo.builder()
				.idPrestamo("SL-001-001")
				.monto(120000D)
				.saldoInsoluto(100000D)
				.cliente(clienteCero)
				.fechaRegistro(new Date())
				.fechaActualizacion(null)
				.usuarioRegistra("s1209465")
				.usuarioActualiza(null)
				.build();
	}

	@Test
	public void contextLoads() {
	}

	@Test
	@DisplayName("Test para guardar un prestamo.")
	void testGuardarPrestamo() {
		// given
		Cliente clienteUno = Cliente.builder()
				.idCliente("0000000002")
				.nombre("Alexis Joselyn")
				.apellidoPaterno("Vazquez")
				.apellidoMaterno("Espinosa")
				.rfc("VAEA840401")
				.curp("VAEA840401")
				.fechaRegistro(new Date())
				.fechaActualizacion(null)
				.usuarioRegistra("s1209466")
				.usuarioActualiza(null)
				.build();
				
		Prestamo prestamoUno = Prestamo.builder()
				.idPrestamo("SL-001-002")
				.monto(150000D)
				.saldoInsoluto(90000D)
				.cliente(clienteUno)
				.fechaRegistro(new Date())
				.fechaActualizacion(null)
				.usuarioRegistra("s1209466")
				.usuarioActualiza(null)
				.build();
		
		// when
		Prestamo prestamoGuardado = prestamoRepository.save(prestamoUno);
		
		// then
		assertThat(prestamoGuardado).isNotNull();
		assertThat(prestamoGuardado.getIdPrestamo()).isEqualTo("SL-001-002");
	}

	@Test
	@DisplayName("Test para actualizar un prestamo.")
	void testActualizarPrestamo() {
		//given
		prestamoRepository.save(prestamoCero);
		
		//when
		Prestamo prestamoGuardado = prestamoRepository.findById(prestamoCero.getIdPrestamo()).get();
		prestamoGuardado.setSaldoInsoluto(90000D);
		prestamoGuardado.setFechaActualizacion(new Date());
		prestamoGuardado.setUsuarioActualiza("s1308654");
		Prestamo prestamoActualizado = prestamoRepository.save(prestamoGuardado);
		
		//then
		assertThat(prestamoActualizado.getSaldoInsoluto()).isEqualTo(90000D);
	}
	
	@Test
	@DisplayName("Test para eliminar un prestamo.")
	void testEliminarPrestamo() {
		//given
		prestamoRepository.save(prestamoCero);
		//when
		prestamoRepository.deleteById(prestamoCero.getIdPrestamo());
		Optional<Prestamo> prestamoGuardado = prestamoRepository.findById(prestamoCero.getIdPrestamo());
		//then
		assertThat(prestamoGuardado).isEmpty();
	}
	
	@Test
	@DisplayName("Test para listar los prestamos.")
	void testListarPrestamos() {
		//given
		Cliente clienteUno = Cliente.builder()
				.idCliente("0000000003")
				.nombre("Maria Fernanda")
				.apellidoPaterno("Sosa")
				.apellidoMaterno("Vazquez")
				.rfc("SOVF081221")
				.curp("SOVF081221MEM")
				.fechaRegistro(new Date())
				.fechaActualizacion(null)
				.usuarioRegistra("s1209466")
				.usuarioActualiza(null)
				.build();
				
		Prestamo prestamoUno = Prestamo.builder()
				.idPrestamo("SL-001-003")
				.monto(150000D)
				.saldoInsoluto(90000D)
				.cliente(clienteUno)
				.fechaRegistro(new Date())
				.fechaActualizacion(null)
				.usuarioRegistra("s1209466")
				.usuarioActualiza(null)
				.build();
		
		prestamoRepository.save(prestamoUno);
		prestamoRepository.save(prestamoUno);
		//when
		Page<Prestamo> listaPrestamos = prestamoRepository.findAll(PageRequest.of(1, 2, Sort.by("idPrestamo").ascending()));
		//then
		assertThat(listaPrestamos).isNotNull();
		assertThat(listaPrestamos.getSize()).isEqualTo(2);
	}
	
	@Test
	@DisplayName("Test para obtener un prestamo.")
	void testObtenerPrestamoPorId() {
		//given
		prestamoRepository.save(prestamoCero);
		//when
		Prestamo prestamoGuardado = prestamoRepository.findById(prestamoCero.getIdPrestamo()).get();
		//then
		assertThat(prestamoGuardado).isNotNull();
		assertThat(prestamoGuardado.getIdPrestamo()).isEqualTo("SL-001-001");
	}
}
