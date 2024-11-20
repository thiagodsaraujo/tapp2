package com.example.boleto.BoletoApi.repository;

import com.example.boleto.BoletoApi.model.Boleto;
import com.example.boleto.BoletoApi.model.EnumStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoletoRepository extends JpaRepository<Boleto, Long> {
    List<Boleto> findByStatus(EnumStatus enumStatus);

    Boleto findByValor(Double valorBoleto);
}
