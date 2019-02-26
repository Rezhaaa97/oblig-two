package no.oslomet.obligtwo.service;


import no.oslomet.obligtwo.model.Shipping;
import no.oslomet.obligtwo.repository.ShippingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShippingServiceImpl implements ShippingService {

    @Autowired
    private ShippingRepository shippingRepository;

    @Override
    public Shipping save(Shipping shipping) {
        return shippingRepository.save(shipping);
    }

    @Override
    public List<Shipping> findAll() {
        return shippingRepository.findAll();
    }

    @Override
    public Shipping findById(long shipping_id) {
        return shippingRepository.findById(shipping_id).get();
    }


}
