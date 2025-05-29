package com.example.perfulandia.controller;


import com.example.perfulandia.model.ClienteModel;
import com.example.perfulandia.model.EnvioPOJO;
import com.example.perfulandia.model.Pago;
import com.example.perfulandia.model.ProductoPOJO;
import com.example.perfulandia.service.PagoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@RestController
@RequestMapping("/perfulandia/api/pago")

public class PagoController {

    @Autowired
    private PagoServices pagoServices;

    @GetMapping
    public ResponseEntity<List<Pago>> listar() {

        List<Pago> pagos = pagoServices.findAll();

        if (pagos.isEmpty()) {

            return ResponseEntity.noContent().build();

        }

        return ResponseEntity.ok(pagos);


    }

    @PostMapping
    public ResponseEntity<Pago> guardar(@RequestBody Pago pago) {

        Pago NuevoPago = pagoServices.save(pago);

        return ResponseEntity.status(HttpStatus.CREATED).body(NuevoPago);

    }

    @GetMapping("/{id}")

    public ResponseEntity<Pago> obtener(@PathVariable Long id) {

        try {

            Pago npago = pagoServices.findById(id);

            return ResponseEntity.ok(npago);

        } catch (Exception e) {

            return ResponseEntity.notFound().build();

        }


    }

    @PutMapping("/{id}")

    public ResponseEntity<Pago> actualizar(@PathVariable Long id, @RequestBody Pago pago) {

        Pago pag = pagoServices.findById(id);

        try {

            pag.setMonto(pago.getMonto());
            pag.setBanco(pago.getBanco());
            pag.setFecha(pago.getFecha());
            pag.setMetodo(pago.getMetodo());

            pagoServices.save(pag);

            return ResponseEntity.ok(pago);

        } catch (Exception e) {

            return ResponseEntity.notFound().build();
        }


    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {

        try {

            pagoServices.deleteById(id);

            return ResponseEntity.noContent().build();

        }catch (Exception e) {

            return ResponseEntity.notFound().build();
        }


    }

    @GetMapping("/cliente/{id}")
    public ResponseEntity<ClienteModel> infoCliente(@PathVariable Long id) {

        ClienteModel cli = pagoServices.obtenerCliente(id);

        if (cli == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cli);

    }

    @PostMapping("/crearventa/{id}")
    public ResponseEntity<String> crearVenta (@PathVariable Long id, @RequestBody Pago pago) {

        ClienteModel cli = pagoServices.obtenerCliente(id);

        Pago pag = pago;

        String boleta = "";

        if (cli == null) {
            return ResponseEntity.notFound().build();
        }

        boleta += "Nombre cliente: " + cli.getNombre_cliente() + "\n"
                + "Apellido cliente: " + cli.getApellido_cliente() + "\n"
                + "RUN cliente: " + cli.getRun_cliente() + "\n"
                + "Monto pago: " + pag.getMonto() + "\n"
                + "Fecha pago: " + pag.getFecha() + "\n"
                + "Metodo pago: " + pag.getMetodo() + "\n"
                + "Banco pago: " + pag.getBanco() + "\n";
        ;

        return ResponseEntity.ok(boleta);


    }

    @PostMapping("/boleta/{id}")
    public ResponseEntity<byte[]> generarBoleta(@PathVariable long id, @RequestBody Pago pago) throws Exception {

        Pago pag = pago;

        ClienteModel cli= pagoServices.obtenerCliente(id);
        EnvioPOJO envio = pagoServices.obtenerEnvio(id);



        if (cli == null) {
            return ResponseEntity.noContent().build();
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Document document = new Document();

        PdfWriter.getInstance(document, out);

        document.open();

        document.add(new Paragraph("PERFULANDIA SA"));

        document.add(new Paragraph("--------------------------------"));

        document.add(new Paragraph("DETALLES BOLETA"));

        document.add(new Paragraph("--------------------------------"));

        document.add(new Paragraph("NOMBRE CLIENTE: " + cli.getNombre_cliente()));

        document.add(new Paragraph("APELLIDO :" + cli.getApellido_cliente()));

        document.add(new Paragraph("RUN CLIENTE :" + cli.getRun_cliente()));

        document.add(new Paragraph("MONTO PAGO : " + pag.getMonto()));

        document.add(new Paragraph("FECHA PAGO : " + pag.getFecha()));

        document.add(new Paragraph("METODO PAGO : " + pag.getMetodo()));

        document.add(new Paragraph("BANCO PAGO : " + pag.getBanco()));

        document.add(new Paragraph("DETALLES ENVIO: " + envio.getDestino()));

        document.close();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "boleta.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(out.toByteArray());


    }





}