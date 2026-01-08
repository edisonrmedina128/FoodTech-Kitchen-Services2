package com.foodtech.kitchen.domain.services;

import com.foodtech.kitchen.domain.model.ProductType;
import com.foodtech.kitchen.domain.model.Station;

//HUMAN REVIEW: Extraje el mapeo ProductType->Station a su propia clase.
//Cumple SRP: solo mapea tipos de productos a estaciones.
public class ProductStationMapper {

    public Station mapToStation(ProductType productType) {
        return switch (productType) {
            case DRINK -> Station.BAR;
            case HOT_DISH -> Station.HOT_KITCHEN;
            case COLD_DISH -> Station.COLD_KITCHEN;
        };
    }
}
