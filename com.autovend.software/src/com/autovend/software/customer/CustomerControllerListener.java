package com.autovend.software.customer;

import com.autovend.software.AbstractEventListener;

public interface CustomerControllerListener extends AbstractEventListener {

    void reactToLowInkAlert();

    void reactToLowPaperAlert();
}
