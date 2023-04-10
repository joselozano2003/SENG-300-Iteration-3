package com.autovend.software.customer;

import com.autovend.products.Product;
import com.autovend.software.AbstractEventListener;

public interface CustomerStationListener extends AbstractEventListener{

    void reactToRemoveItemRequest(Product product, double quantity,CustomerStationLogic stationLogic);

    void lowInkAlert(CustomerStationLogic stationLogic);

    void lowPaperAlert(CustomerStationLogic stationLogic);
}

