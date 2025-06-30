package service;

import com.example.perfulandia.model.ClienteModel;
import com.example.perfulandia.model.EnvioPOJO;
import com.example.perfulandia.model.Pago;
import com.example.perfulandia.model.ProductoPOJO;
import com.example.perfulandia.repository.PagoRepository;
import com.example.perfulandia.service.PagoServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PagoServicesTest {

    @Mock
    private PagoRepository pagoRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PagoServices pagoServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        List<Pago> pagos = Arrays.asList(
                new Pago(1L, "11.111.111-1", 10000, new Date(), "tarjeta", "BancoPrueba"),
                new Pago(2L, "22.222.222-2", 5000, new Date(), "efectivo", "BancoPrueba2")
        );
        when(pagoRepository.findAll()).thenReturn(pagos);

        List<Pago> result = pagoServices.findAll();

        assertEquals(2, result.size());
        verify(pagoRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        Pago pago = new Pago(1L, "11.111.111-1", 10000, new Date(), "tarjeta", "BancoPrueba");
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));

        Pago result = pagoServices.findById(1L);

        assertNotNull(result);
        assertEquals("11.111.111-1", result.getRunCliente());
        verify(pagoRepository, times(1)).findById(1L);
    }

    @Test
    void testSave() {
        Pago pago = new Pago(null, "33.333.333-3", 20000, new Date(), "debito", "BancoPrueba3");
        Pago savedPago = new Pago(3L, "33.333.333-3", 20000, pago.getFecha(), "debito", "BancoPrueba3");
        when(pagoRepository.save(pago)).thenReturn(savedPago);

        Pago result = pagoServices.save(pago);

        assertNotNull(result.getId());
        assertEquals("33.333.333-3", result.getRunCliente());
        verify(pagoRepository, times(1)).save(pago);
    }

    @Test
    void testDeleteById() {
        Long id = 4L;

        doNothing().when(pagoRepository).deleteById(id);

        pagoServices.deleteById(id);

        verify(pagoRepository, times(1)).deleteById(id);
    }

    @Test
    void testObtenerCliente() {
        Long idCliente = 5L;
        ClienteModel mockCliente = new ClienteModel(idCliente, "55.555.555-5", "Juan", "Pérez", "Calle Falsa 123");
        String url = "http://localhost:8081/perfulandia/api/clientes/" + idCliente;

        when(restTemplate.getForObject(url, ClienteModel.class)).thenReturn(mockCliente);

        ClienteModel result = pagoServices.obtenerCliente(idCliente);

        assertNotNull(result);
        assertEquals("Juan", result.getNombre_cliente());
        verify(restTemplate, times(1)).getForObject(url, ClienteModel.class);
    }

    @Test
    void testObtenerEnvio() {
        long idEnvio = 6L;
        EnvioPOJO mockEnvio = new EnvioPOJO(idEnvio, "Envio1", "Destino1", "Entregado", "Ruta1", "Origen1");
        String url = "http://localhost:8084/perfulandia/api/envios/" + idEnvio;

        when(restTemplate.getForObject(url, EnvioPOJO.class)).thenReturn(mockEnvio);

        EnvioPOJO result = pagoServices.obtenerEnvio(idEnvio);

        assertNotNull(result);
        assertEquals("Destino1", result.getDestino());
        verify(restTemplate, times(1)).getForObject(url, EnvioPOJO.class);
    }

    @Test
    void testObtenerProducto() {
        long idProducto = 7L;
        ProductoPOJO mockProducto = new ProductoPOJO(1, "Perfume", "Fragancia", 10, new BigDecimal("19990"));
        String url = "http://localhost:8080/perfulandia/api/producto/" + idProducto;

        when(restTemplate.getForObject(url, ProductoPOJO.class)).thenReturn(mockProducto);

        ProductoPOJO result = pagoServices.obtenerProducto(idProducto);

        assertNotNull(result);
        assertEquals("Perfume", result.getNombre());
        verify(restTemplate, times(1)).getForObject(url, ProductoPOJO.class);
    }
}
