package com.uass.chacomer.config;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.uass.chacomer.entidad.Categoria;
import com.uass.chacomer.entidad.Moto;
import com.uass.chacomer.repositorio.CategoriaRepository;
import com.uass.chacomer.repositorio.MotoRepository;

/**
 * Carga el catálogo de ejemplo la primera vez que arranca la aplicación
 * (cuando las tablas están vacías).
 */
@Component
public class CargaInicial implements CommandLineRunner {

	private final CategoriaRepository categoriaRepository;
	private final MotoRepository motoRepository;

	public CargaInicial(CategoriaRepository categoriaRepository, MotoRepository motoRepository) {
		this.categoriaRepository = categoriaRepository;
		this.motoRepository = motoRepository;
	}

	@Override
	public void run(String... args) {
		if (categoriaRepository.count() > 0) {
			return;
		}

		Categoria media = categoriaRepository.save(new Categoria(
				"Media cilindrada",
				"media",
				"Motos de 250 a 499 cc: ágiles para ciudad y ruta corta."));
		Categoria alta = categoriaRepository.save(new Categoria(
				"Alta cilindrada",
				"alta",
				"Motos de 500 cc en adelante: potencia para ruta y estilo deportivo."));

		List<Moto> motos = List.of(
				crear("Honda", "CB 300F Twister", 293, "28500000", "Rojo racing",
						"Naked liviana, ideal para ciudad y trayectos diarios. Arranque eléctrico y tablero digital.",
						"https://images.unsplash.com/photo-1558981806-ec527fa84c39?auto=format&fit=crop&w=1200&q=80",
						media),
				crear("Yamaha", "MT-03", 321, "32800000", "Azul night",
						"La más vendida del segmento medio: postura erguida, frenos ABS y un motor suave.",
						"https://images.unsplash.com/photo-1568772585407-9361f9bf3a87?auto=format&fit=crop&w=1200&q=80",
						media),
				crear("Kawasaki", "Ninja 400", 399, "42900000", "Verde lima",
						"Carenado deportivo y chasis liviano. Perfecta para quien busca estilo de pista sin saltar a alta cilindrada.",
						"https://images.unsplash.com/photo-1449426468159-d32ab8755629?auto=format&fit=crop&w=1200&q=80",
						media),
				crear("KTM", "Duke 390", 373, "35500000", "Naranja factory",
						"Posición agresiva, suspensión de calidad y un carácter que se siente en cada aceleración.",
						"https://images.unsplash.com/photo-1609630875171-b1321377ee65?auto=format&fit=crop&w=1200&q=80",
						media),
				crear("Honda", "CBR 650R", 649, "58900000", "Negro perla",
						"Cuatro cilindros en línea, carenado deportivo y un sonido que enamora en ruta.",
						"https://images.unsplash.com/photo-1558981852-426c6c22a060?auto=format&fit=crop&w=1200&q=80",
						alta),
				crear("Yamaha", "MT-07", 689, "62400000", "Gris storm",
						"Bicilíndrico con torque desde bajo régimen. Naked de alta cilindrada para uso diario y finde.",
						"https://images.unsplash.com/photo-1615177346119-85840b848b2b?auto=format&fit=crop&w=1200&q=80",
						alta),
				crear("Kawasaki", "Z900", 948, "71200000", "Negro / verde",
						"Naked de alta cilindrada con presencia de concesionaria. Potencia controlable y ayudas electrónicas.",
						"https://images.unsplash.com/photo-1591637337425-9d229d2f9013?auto=format&fit=crop&w=1200&q=80",
						alta),
				crear("Honda", "Africa Twin 1100", 1084, "98500000", "Blanco adventure",
						"Trail de gran cilindrada para viajar. Maletas opcionales, modo lluvia y autonomía de ruta.",
						"https://images.unsplash.com/photo-1511994298241-608b155d348f?auto=format&fit=crop&w=1200&q=80",
						alta));

		motoRepository.saveAll(motos);
	}

	private static Moto crear(String marca, String modelo, int cc, String precio, String color,
			String descripcion, String imagen, Categoria categoria) {
		Moto moto = new Moto();
		moto.setMarca(marca);
		moto.setModelo(modelo);
		moto.setCilindrada(cc);
		moto.setPrecio(new BigDecimal(precio));
		moto.setColor(color);
		moto.setDescripcion(descripcion);
		moto.setImagenUrl(imagen);
		moto.setDisponible(true);
		moto.setCategoria(categoria);
		return moto;
	}
}
