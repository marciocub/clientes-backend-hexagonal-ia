package com.banco.infrastructure.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.banco.domain.model.Cliente;
import com.banco.domain.model.Factura;
import com.banco.domain.model.ItemFactura;
import com.banco.domain.model.TarjetaCredito;
import com.banco.infrastructure.out.db.ClienteJpaEntity;
import com.banco.infrastructure.out.db.FacturaJpaEntity;
import com.banco.infrastructure.out.db.ItemFacturaJpaEntity;
import com.banco.infrastructure.out.db.TarjetaCreditoJpaEntity;

/**
 * Mapper JPA &lt;-&gt; Dominio para el AGREGADO completo del cliente
 * (cliente + tarjetas + facturas + items).
 *
 * Componente Spring (@Component): lo inyecta ClienteAdapter y permite
 * mantener el dominio puro y las entidades JPA aisladas de la aplicacion.
 * Mapea bidireccionalmente todo el arbol de objetos, reconstruyendo las
 * referencias (lado propietario) para que Hibernate persista en cascada.
 */
@Component
public class ClienteJpaMapper {

    /** Dominio -&gt; JPA (para guardar el agregado completo). */
    public ClienteJpaEntity aEntity(Cliente cliente) {
        ClienteJpaEntity entity = new ClienteJpaEntity();
        entity.setId(cliente.getId());
        entity.setNombre(cliente.getNombre());
        entity.setCuit(cliente.getCuit());

        for (TarjetaCredito t : cliente.getTarjetas()) {
            TarjetaCreditoJpaEntity te = new TarjetaCreditoJpaEntity();
            te.setId(t.getId());
            te.setNumeroMascara(t.getNumeroMascara());
            te.setMarca(t.getMarca());
            te.setLimiteCredito(t.getLimiteCredito());
            te.setSaldoUtilizado(t.getSaldoUtilizado() != null ? t.getSaldoUtilizado() : java.math.BigDecimal.ZERO);
            te.setFechaVencimiento(t.getFechaVencimiento());
            te.setEstado(t.getEstado() != null ? t.getEstado().name() : TarjetaCredito.Estado.ACTIVA.name());
            te.setCliente(entity); // lado propietario de la relacion
            entity.getTarjetas().add(te);
        }

        for (Factura f : cliente.getFacturas()) {
            FacturaJpaEntity fe = new FacturaJpaEntity();
            fe.setId(f.getId());
            fe.setNumeroFactura(f.getNumeroFactura());
            fe.setFechaEmision(f.getFechaEmision());
            fe.setMontoTotal(f.getMontoTotal() != null ? f.getMontoTotal() : java.math.BigDecimal.ZERO);
            fe.setEstado(f.getEstado() != null ? f.getEstado().name() : Factura.Estado.PENDIENTE.name());
            fe.setCliente(entity); // lado propietario de la relacion
            for (ItemFactura i : f.getItems()) {
                ItemFacturaJpaEntity ie = new ItemFacturaJpaEntity();
                ie.setId(i.getId());
                ie.setDescripcion(i.getDescripcion());
                ie.setCantidad(i.getCantidad() != null ? i.getCantidad() : 1);
                ie.setPrecioUnitario(i.getPrecioUnitario() != null ? i.getPrecioUnitario() : java.math.BigDecimal.ZERO);
                ie.setSubtotal(i.getSubtotal() != null ? i.getSubtotal() : java.math.BigDecimal.ZERO);
                ie.setFactura(fe); // lado propietario de la relacion
                fe.getItems().add(ie);
            }
            entity.getFacturas().add(fe);
        }
        return entity;
    }

    /** JPA -&gt; Dominio (para leer el agregado completo). */
    public Cliente aDominio(ClienteJpaEntity entity) {
        Cliente cliente = new Cliente(entity.getId(), entity.getNombre(), entity.getCuit());

        for (TarjetaCreditoJpaEntity te : entity.getTarjetas()) {
            TarjetaCredito t = new TarjetaCredito();
            t.setId(te.getId());
            t.setNumeroMascara(te.getNumeroMascara());
            t.setMarca(te.getMarca());
            t.setLimiteCredito(te.getLimiteCredito());
            t.setSaldoUtilizado(te.getSaldoUtilizado());
            t.setFechaVencimiento(te.getFechaVencimiento());
            t.setEstado(te.getEstado() != null ? TarjetaCredito.Estado.valueOf(te.getEstado()) : TarjetaCredito.Estado.ACTIVA);
            cliente.getTarjetas().add(t);
        }

        for (FacturaJpaEntity fe : entity.getFacturas()) {
            Factura f = new Factura();
            f.setId(fe.getId());
            f.setNumeroFactura(fe.getNumeroFactura());
            f.setFechaEmision(fe.getFechaEmision());
            f.setMontoTotal(fe.getMontoTotal());
            f.setEstado(fe.getEstado() != null ? Factura.Estado.valueOf(fe.getEstado()) : Factura.Estado.PENDIENTE);
            for (ItemFacturaJpaEntity ie : fe.getItems()) {
                ItemFactura i = new ItemFactura();
                i.setId(ie.getId());
                i.setDescripcion(ie.getDescripcion());
                i.setCantidad(ie.getCantidad());
                i.setPrecioUnitario(ie.getPrecioUnitario());
                i.setSubtotal(ie.getSubtotal());
                f.getItems().add(i);
            }
            cliente.getFacturas().add(f);
        }
        return cliente;
    }

    /** Utilidad: evita listas nulas al setear colecciones del agregado. */
    public static <T> List<T> normalizar(List<T> lista) {
        return lista != null ? lista : new ArrayList<>();
    }
}