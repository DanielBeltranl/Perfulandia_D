package com.example.perfulandia.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class infoPagoDTO {

    private List<Long> lista_productos;
    private Pago pago;

}
