package com.mialeds.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mialeds.models.Kardex;

@Repository
public interface KardexRepository extends JpaRepository<Kardex, Integer> {

        List <Kardex> findByMovimientoOrderByFechaDesc(String movimiento);

}
