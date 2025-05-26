package com.example.perfulandia.repository;

import com.example.perfulandia.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository

public interface PagoRepository extends JpaRepository<Pago, Long> {
}
