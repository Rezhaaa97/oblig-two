package no.oslomet.obligtwo.service;

import no.oslomet.obligtwo.model.Shipping;

import java.util.List;

public interface ShippingService {

    Shipping save(Shipping shipping);
    List<Shipping> findAll();
    Shipping findById(long shipping_id);

}
