package com.banco.application.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.banco.application.dto.ClienteDTO;
import com.banco.application.dto.ClienteDtoResponse;
import com.banco.application.dto.CrearFacturaDTO;
import com.banco.application.dto.FacturaDtoResponse;
import com.banco.application.dto.ItemFacturaDTO;
import com.banco.application.dto.SolicitarTarjetaDTO;
import com.banco.application.dto.TarjetaCreditoDtoResponse;
import com.banco.application.port.in.ClienteUseCase;
import com.banco.application.port.out.ClienteOutPort;
import com.banco.domain.exception.ClienteNoEncontradoException;
import com.banco.domain.model.Cliente;
import com.banco.domain.model.Factura;
import com.banco.domain.model.ItemFactura;
import com.banco.domain.model.TarjetaCredito;

/**
 * Servicio de aplicacion: implementa el puerto de entrada ClienteUseCase.
 *
 * Regla hexagonal: SOLO conoce el dominio y sus propios puertos.
 * Inyecta la interfaz ClienteOutPort (puerto de salida), nunca clases
 * concretas de infraestructura (JPA, web, seguridad).
 *
 * Coordina: carga el AGREGADO, invoca los METODOS DE NEGOCIO del dominio
 * (la regla del tope y el calculo del total viven en Cliente) y guarda
 * a traves del puerto de salida.
 */
@Service
public class ClienteService implements ClienteUseCase {

    private final ClienteOutPort clienteOutPort;

    public ClienteService(ClienteOutPort clienteOutPort) {
        this.clienteOutPort = clienteOutPort;
    }

    @Override
    public ClienteDtoResponse crearCliente(ClienteDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNombre(dto.getNombre().trim());
        cliente.setCuit(dto.getCuit().trim());
        Cliente guardado = clienteOutPort.guardar(cliente);
        return aResponse(guardado);
    }

    @Override
    public ClienteDtoResponse obtenerPorId(Long id) {
        return aResponse(buscarCliente(id));
    }

    @Override
    public List<ClienteDtoResponse> listarTodos() {
        return clienteOutPort.listarTodos().stream()
                .map(this::aResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ClienteDtoResponse actualizar(Long id, ClienteDTO dto) {
        Cliente existente = buscarCliente(id);
        existente.setNombre(dto.getNombre().trim());
        existente.setCuit(dto.getCuit().trim());
        // tarjetas y facturas se conservan: solo cambian datos del cliente
        return aResponse(clienteOutPort.guardar(existente));
    }

    @Override
    public void eliminar(Long id) {
        Cliente existente = buscarCliente(id);
        clienteOutPort.eliminar(existente);
    }

    @Override
    public ClienteDtoResponse agregarTarjeta(Long id, SolicitarTarjetaDTO dto) {
        Cliente cliente = buscarCliente(id);
        // La regla de negocio (tope de limite acumulado) vive en el AGREGADO
        cliente.solicitarTarjeta(dto.getMarca().trim(), dto.getLimiteSolicitado(), dto.getUltimoCuatro().trim());
        return aResponse(clienteOutPort.guardar(cliente));
    }

    @Override
    public ClienteDtoResponse agregarFactura(Long id, CrearFacturaDTO dto) {
        Cliente cliente = buscarCliente(id);
        List<ItemFactura> items = dto.getItems().stream()
                .map(this::aItemDominio)
                .collect(Collectors.toList());
        // El calculo del monto total vive en el AGREGADO (emitirFactura -> calcularTotal)
        cliente.emitirFactura(dto.getNumeroFactura().trim(), items);
        return aResponse(clienteOutPort.guardar(cliente));
    }

    // ------------------------------------------------------------------
    // Helpers privados
    // ------------------------------------------------------------------

    private Cliente buscarCliente(Long id) {
        return clienteOutPort.buscarPorId(id)
                .orElseThrow(() -> new ClienteNoEncontradoException(
                        "No existe un cliente con el id: " + id));
    }

    private ItemFactura aItemDominio(ItemFacturaDTO dto) {
        ItemFactura item = new ItemFactura();
        item.setDescripcion(dto.getDescripcion().trim());
        item.setCantidad(dto.getCantidad());
        item.setPrecioUnitario(dto.getPrecioUnitario());
        return item;
    }

    /** Convierte el AGREGADO completo a DTO de salida (nunca se exponen las entidades). */
    private ClienteDtoResponse aResponse(Cliente cliente) {
        ClienteDtoResponse r = new ClienteDtoResponse();
        r.setId(cliente.getId());
        r.setNombre(cliente.getNombre());
        r.setCuit(cliente.getCuit());

        for (TarjetaCredito t : cliente.getTarjetas()) {
            TarjetaCreditoDtoResponse tr = new TarjetaCreditoDtoResponse();
            tr.setId(t.getId());
            tr.setNumeroMascara(t.getNumeroMascara());
            tr.setMarca(t.getMarca());
            tr.setLimiteCredito(t.getLimiteCredito());
            tr.setSaldoUtilizado(t.getSaldoUtilizado());
            tr.setCreditoDisponible(t.getCreditoDisponible());
            tr.setFechaVencimiento(t.getFechaVencimiento());
            tr.setEstado(t.getEstado() != null ? t.getEstado().name() : TarjetaCredito.Estado.ACTIVA.name());
            r.getTarjetas().add(tr);
        }

        for (Factura f : cliente.getFacturas()) {
            FacturaDtoResponse fr = new FacturaDtoResponse();
            fr.setId(f.getId());
            fr.setNumeroFactura(f.getNumeroFactura());
            fr.setFechaEmision(f.getFechaEmision());
            fr.setMontoTotal(f.getMontoTotal());
            fr.setEstado(f.getEstado() != null ? f.getEstado().name() : Factura.Estado.PENDIENTE.name());
            for (ItemFactura i : f.getItems()) {
                ItemFacturaDTO ir = new ItemFacturaDTO();
                ir.setDescripcion(i.getDescripcion());
                ir.setCantidad(i.getCantidad());
                ir.setPrecioUnitario(i.getPrecioUnitario());
                ir.setSubtotal(i.getSubtotal());
                fr.getItems().add(ir);
            }
            r.getFacturas().add(fr);
        }
        return r;
    }
}