package controller;

import com.example.perfulandia.controller.PagoController;
import com.example.perfulandia.model.ClienteModel;
import com.example.perfulandia.model.EnvioPOJO;
import com.example.perfulandia.model.Pago;
import com.example.perfulandia.service.PagoServices;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PagoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PagoServices pagoServices;

    @InjectMocks
    private PagoController pagoController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Pago pago;
    private ClienteModel clienteModel;
    private EnvioPOJO envioPOJO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(pagoController).build();

        pago = new Pago(1L, "11.111.111-1", 5000, new Date(), "tarjeta", "BancoPrueba");
        clienteModel = new ClienteModel(1L, "11.111.111-1", "Juan", "Pérez", "Calle Falsa 123");
        envioPOJO = new EnvioPOJO(1L, "Envio1", "Destino1", "Entregado", "Ruta1", "Origen1");
    }

    @Test
    void testListarPagos() throws Exception {
        when(pagoServices.findAll()).thenReturn(Arrays.asList(pago));

        mockMvc.perform(get("/perfulandia/api/pago"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].runCliente").value("11.111.111-1"));
    }

    @Test
    void testListarPagosVacio() throws Exception {
        when(pagoServices.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/perfulandia/api/pago"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGuardarPago() throws Exception {
        when(pagoServices.save(any(Pago.class))).thenReturn(pago);

        mockMvc.perform(post("/perfulandia/api/pago")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pago)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.runCliente").value("11.111.111-1"));
    }

    @Test
    void testObtenerPagoPorId() throws Exception {
        when(pagoServices.findById(1L)).thenReturn(pago);

        mockMvc.perform(get("/perfulandia/api/pago/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.runCliente").value("11.111.111-1"));
    }

    @Test
    void testObtenerPagoPorIdNoEncontrado() throws Exception {
        when(pagoServices.findById(99L)).thenThrow(new RuntimeException("No encontrado"));

        mockMvc.perform(get("/perfulandia/api/pago/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testActualizarPago() throws Exception {
        when(pagoServices.findById(1L)).thenReturn(pago);
        when(pagoServices.save(any(Pago.class))).thenReturn(pago);

        mockMvc.perform(put("/perfulandia/api/pago/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pago)))
                .andExpect(status().isOk());
    }

    @Test
    void testActualizarPagoNoEncontrado() throws Exception {
        when(pagoServices.findById(99L)).thenThrow(new RuntimeException("No encontrado"));

        mockMvc.perform(put("/perfulandia/api/pago/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pago)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testEliminarPago() throws Exception {
        doNothing().when(pagoServices).deleteById(1L);

        mockMvc.perform(delete("/perfulandia/api/pago/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testEliminarPagoNoEncontrado() throws Exception {
        doThrow(new RuntimeException("No encontrado")).when(pagoServices).deleteById(99L);

        mockMvc.perform(delete("/perfulandia/api/pago/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGenerarBoleta() throws Exception {
        when(pagoServices.obtenerCliente(1L)).thenReturn(clienteModel);
        when(pagoServices.obtenerEnvio(1L)).thenReturn(envioPOJO);

        mockMvc.perform(post("/perfulandia/api/pago/boleta/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pago)))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/pdf"));
    }

    @Test
    void testGenerarBoletaSinCliente() throws Exception {
        when(pagoServices.obtenerCliente(1L)).thenReturn(null);

        mockMvc.perform(post("/perfulandia/api/pago/boleta/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pago)))
                .andExpect(status().isNoContent());
    }
}