package com.uass.chacomer.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uass.chacomer.entidad.Moto;

public interface MotoRepository extends JpaRepository<Moto, Long> {

	List<Moto> findByCategoriaCodigoOrderByPrecioAsc(String codigo);

	List<Moto> findAllByOrderByPrecioAsc();
}
