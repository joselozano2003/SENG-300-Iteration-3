package com.autovend.software.bagging;

import com.autovend.software.AbstractFacadeListener;

public interface BaggingListener extends AbstractFacadeListener {

	void onWeightChanged(double weightInGrams);
	

}
