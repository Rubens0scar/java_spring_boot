package com.mumanal.siver.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.time.*;
@Table("usuario")
public record Usuario(
        @Id String codigo,
        String password,
        String nombreCompleto,
        Integer paginaInicio,
        String email,
        Integer oficina,
        Integer unidad,
        Integer permisos,
        String cargo,
        String mosca,
        Integer nivel,
        Integer genero,
        Integer habilitado,
        Instant fechaCreacion,
        String creadoPor,
        OffsetTime fechaModificacion,
        String modificadoPor,
        String direccionIp,
        String accion,
        Integer validaPassword
) {}
