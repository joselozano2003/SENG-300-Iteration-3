package com.autovend.software.GUI;

import com.autovend.devices.SelfCheckoutStation;

import javax.swing.*;
import java.awt.*;


public class Controller {
    private SelfCheckoutStation station = null;



    public Controller(SelfCheckoutStation station){
        if(station == null) throw new NullPointerException("No argument can be null");
        this.station = station;

    }




}