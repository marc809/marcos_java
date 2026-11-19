package com.uass.chacomer.controlador;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.uass.chacomer.entidad.Categoria;
import com.uass.chacomer.entidad.Moto;
import com.uass.chacomer.repositorio.CategoriaRepository;
import com.uass.chacomer.repositorio.MotoRepository;
/**
 * API REST que el frontend consume para armar el catálogo
 * y el enlace de compra por WhatsApp.
 */
@RestController
@RequestMapping("/api")
public class ApiControlador {
	private final MotoRepository motoRepository;
	private final CategoriaRepository categoriaRepository;
	private final String whatsapp;
	private final String nombreTienda;
	public ApiControlador(
			MotoRepository motoRepository,
			CategoriaRepository categoriaRepository,
			@Value("${chacomer.whatsapp}") String whatsapp,
			@Value("${chacomer.nombre}") String nombreTienda) {
		this.motoRepository = motoRepository;
		this.categoriaRepository = categoriaRepository;
		this.whatsapp = whatsapp;
		this.nombreTienda = nombreTienda;
	}
	@GetMapping("/config")
	public Map<String, String> config() {
		Map<String, String> datos = new LinkedHashMap<>();
		datos.put("nombre", nombreTienda);
		datos.put("whatsapp", whatsapp);
		return datos;
	}
	@GetMapping("/categorias")
	public List<Categoria> categorias() {
		return categoriaRepository.findAll();
	}
	@GetMapping("/motos")
	public List<Moto> motos(@RequestParam(required = false) String categoria) {
		if (categoria == null || categoria.isBlank() || "todas".equalsIgnoreCase(categoria)) {
			return motoRepository.findAllByOrderByPrecioAsc();
		}
		return motoRepository.findByCategoriaCodigoOrderByPrecioAsc(categoria.toLowerCase());
	}
	@GetMapping("/motos/{id}")
	public ResponseEntity<Moto> moto(@PathVariable Long id) {
		return motoRepository.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}
	/**
	 * Alta de una moto (Postman). El id se ignora: PostgreSQL lo genera.
	 * categoria.id debe existir (1 = media, 2 = alta, si no borramos datos).
	 */
	@PostMapping("/motos")
	public ResponseEntity<?> crearMoto(@RequestBody Moto cuerpo) {
		if (cuerpo.getMarca() == null || cuerpo.getMarca().isBlank()
				|| cuerpo.getModelo() == null || cuerpo.getModelo().isBlank()
				|| cuerpo.getCilindrada() == null
				|| cuerpo.getPrecio() == null
				|| cuerpo.getCategoria() == null
				|| cuerpo.getCategoria().getId() == null) {
			return ResponseEntity.badRequest().body(error(
					"Faltan datos. Mandá marca, modelo, cilindrada, precio y categoria.id"));
		}
		return categoriaRepository.findById(cuerpo.getCategoria().getId())
				.<ResponseEntity<?>>map(categoria -> {
					cuerpo.setId(null);
					cuerpo.setCategoria(categoria);
					Moto guardada = motoRepository.save(cuerpo);
					return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
				})
				.orElseGet(() -> ResponseEntity.badRequest().body(error(
						"categoria.id no existe. Primero hacé GET http://localhost:8080/api/categorias")));
	}
	private static Map<String, String> error(String mensaje) {
		Map<String, String> cuerpo = new LinkedHashMap<>();
		cuerpo.put("error", mensaje);
		return cuerpo;
	}
}