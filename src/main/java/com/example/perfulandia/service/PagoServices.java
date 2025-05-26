package com.example.perfulandia.service;

import com.example.perfulandia.model.ClienteModel;
import com.example.perfulandia.model.Pago;
import com.example.perfulandia.repository.PagoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Service
@Transactional
public class PagoServices {


    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private RestTemplate restTemplate;


    public List<Pago> findAll() {

        return pagoRepository.findAll();

    }

    public Pago findById(Long id) {

        return pagoRepository.findById(id).get();

    }

    public Pago save(Pago pago) {

        return pagoRepository.save(pago);
    }

    public void deleteById(Long Id) {

        pagoRepository.deleteById(Id);

    }

    public ClienteModel obtenerCliente (Long id){

        //url de la API cliente

        String url = "http://localhost:8081/perfulandia/api/clientes/" + id;

        ClienteModel cliente = restTemplate.getForObject(url, ClienteModel.class);

        return cliente;

    }



}
