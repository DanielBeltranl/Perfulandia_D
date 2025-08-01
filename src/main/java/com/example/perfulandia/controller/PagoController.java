package com.example.perfulandia.controller;

import com.example.perfulandia.model.*;
import com.example.perfulandia.service.PagoServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/perfulandia/api/pago")
@Tag(name = "Pagos", description = "Operaciones relacionadas con los pagos")
public class PagoController {

    @Autowired
    private PagoServices pagoServices;

    @GetMapping
    @Operation(summary = "Obtener pagos", description = "Trae todo los pagos y los datos asociadios a cada uno de ellos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista desplegada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Pago.class))),
            @ApiResponse(responseCode = "404", description = "Pagos no encontrados")
    })
    public ResponseEntity<CollectionModel<EntityModel<Pago>>> listar() {
        List<Pago> pagos = pagoServices.findAll();
        if (pagos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<EntityModel<Pago>> pagoResources = new ArrayList<>();
        for (Pago pago : pagos) {
            EntityModel<Pago> resource = EntityModel.of(pago);
            resource.add(WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(PagoController.class).obtener(pago.getId())).withSelfRel());
            resource.add(WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(PagoController.class).listar()).withRel("pagos"));
            pagoResources.add(resource);
        }
        CollectionModel<EntityModel<Pago>> collectionModel = CollectionModel.of(pagoResources);
        collectionModel.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(PagoController.class).listar()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    @PostMapping
    @Operation(summary = "Ingresar Pago", description = "Ingresa un pago con productos y datos del cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago guardado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Pago.class))),
            @ApiResponse(responseCode = "404", description = "Pagos no guardado")
    })
    public ResponseEntity<EntityModel<Pago>> guardar(@RequestBody Pago pago) {
        Pago nuevoPago = pagoServices.save(pago);
        EntityModel<Pago> resource = EntityModel.of(nuevoPago);
        resource.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(PagoController.class).obtener(nuevoPago.getId())).withSelfRel());
        resource.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(PagoController.class).listar()).withRel("pagos"));
        return ResponseEntity.status(HttpStatus.CREATED).body(resource);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pago por id", description = "Busca un pago con productos asociado a un cleinte a traves del ID propio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Pago.class))),
            @ApiResponse(responseCode = "404", description = "Pagos no encontrado")
    })
    public ResponseEntity<EntityModel<Pago>> obtener(@PathVariable Long id) {
        try {
            Pago npago = pagoServices.findById(id);
            EntityModel<Pago> resource = EntityModel.of(npago);
            resource.add(WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(PagoController.class).obtener(id)).withSelfRel());
            resource.add(WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(PagoController.class).listar()).withRel("pagos"));
            resource.add(WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(PagoController.class).eliminar(id)).withRel("eliminar"));
            return ResponseEntity.ok(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modificar pago", description = "Modifica un pago a traves del ID del mismo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago modificado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Pago.class))),
            @ApiResponse(responseCode = "404", description = "Pagos no modificado")
    })
    public ResponseEntity<EntityModel<Pago>> actualizar(@PathVariable Long id, @RequestBody Pago pago) {
        try {
            Pago pag = pagoServices.findById(id);
            pag.setMonto(pago.getMonto());
            pag.setBanco(pago.getBanco());
            pag.setFecha(pago.getFecha());
            pag.setMetodo(pago.getMetodo());
            Pago actualizado = pagoServices.save(pag);

            EntityModel<Pago> resource = EntityModel.of(actualizado);
            resource.add(WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(PagoController.class).obtener(id)).withSelfRel());
            resource.add(WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(PagoController.class).listar()).withRel("pagos"));
            return ResponseEntity.ok(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Borrar pago", description = "Borra un pago buscando un pago por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago borrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Pago.class))),
            @ApiResponse(responseCode = "404", description = "Pagos no encontrado")
    })
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            pagoServices.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/boleta/{id}")
    @Operation(summary = "Generar boleta", description = "Genera una boleta de un pago a trave de ingresar datos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Boleta creada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Pago.class))),
            @ApiResponse(responseCode = "404", description = "No se pudo generar la boleta")
    })
    public ResponseEntity<byte[]> generarBoleta(@PathVariable long id, @RequestBody infoPagoDTO infoPagoDTO) throws Exception {
        ClienteModel cli = pagoServices.obtenerCliente(id);
        Pago pag = infoPagoDTO.getPago();
        EnvioPOJO envio = pagoServices.obtenerEnvio(id);
        List<Long> productos = infoPagoDTO.getLista_productos();

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
        document.add(new Paragraph("DETALLES DE LA COMPRA"));
        document.add(new Paragraph("--------------------------------"));

        for (int i = 0; i < productos.size(); i++) {
            ProductoPOJO producto = pagoServices.obtenerProducto(productos.get(i));
            document.add(new Paragraph("Nombre: " + producto.getNombre()));
            document.add(new Paragraph("Precio: " + producto.getPrecio()));
            document.add(new Paragraph("--------------------------------"));
        }

        document.close();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "boleta.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(out.toByteArray());
    }
}