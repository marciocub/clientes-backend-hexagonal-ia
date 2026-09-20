package com.banco.application.port.in;

import java.util.List;

import com.banco.application.dto.ClienteDTO;
import com.banco.application.dto.ClienteDtoResponse;
import com.banco.application.dto.CrearFacturaDTO;
import com.banco.application.dto.SolicitarTarjetaDTO;

/**
 * Puerto de ENTRADA: casos de uso del AGREGADO Cliente
 * (cliente + tarjetas de credito + facturas con items).
 * Firman sus metodos con DTOs (entrada/salida), no con entidades de dominio.
 * El adaptador REST (ClienteController) consume esta interfaz.
 */
public interface ClienteUseCase {

    /** Crea un cliente nuevo (201). */
    ClienteDtoResponse crearCliente(ClienteDTO dto);

    /** Obtiene el agregado completo de un cliente por id (404 si no existe). */
    ClienteDtoResponse obtenerPorId(Long id);

    /** Lista todos los clientes con su agregado completo. */
    List<ClienteDtoResponse> listarTodos();

    /** Actualiza datos del cliente (nombre / cuit); tarjetas y facturas se conservan. */
    ClienteDtoResponse actualizar(Long id, ClienteDTO dto);

    /** Elimina un cliente y todo su agregado (tarjetas y facturas). */
    void eliminar(Long id);

    /**
     * Solicita una tarjeta para el cliente (409 si se excede el tope de limite).
     * Devuelve el agregado actualizado.
     */
    ClienteDtoResponse agregarTarjeta(Long id, SolicitarTarjetaDTO dto);

    /**
     * Emite una factura con items para el cliente (total calculado por el dominio).
     * Devuelve el agregado actualizado.
     */
    ClienteDtoResponse agregarFactura(Long id, CrearFacturaDTO dto);
}