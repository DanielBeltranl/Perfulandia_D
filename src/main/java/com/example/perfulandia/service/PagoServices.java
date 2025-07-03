package com.example.perfulandia.service;

import com.example.perfulandia.model.ClienteModel;
import com.example.perfulandia.model.EnvioPOJO;
import com.example.perfulandia.model.Pago;
import com.example.perfulandia.model.ProductoPOJO;
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

        String url = "https://perfulandia-cliente-1.onrender.com/perfulandia/api/clientes/" + id;

        ClienteModel cliente = restTemplate.getForObject(url, ClienteModel.class);

        return cliente;

    }

    public EnvioPOJO obtenerEnvio (long id){

        String url = "https://perfulandia-envio.onrender.com/perfulandia/api/envios/" + id;

        EnvioPOJO envio = restTemplate.getForObject(url, EnvioPOJO.class);

        return envio;
    }

    public ProductoPOJO obtenerProducto (long id){

        String url = "https://perfulandia-producto-3.onrender.com/perfulandia/api/producto/" + id;

        ProductoPOJO producto = restTemplate.getForObject(url, ProductoPOJO.class);

        return producto;

    }







}
