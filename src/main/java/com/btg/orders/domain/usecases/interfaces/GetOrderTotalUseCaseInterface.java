package com.btg.orders.domain.usecases.interfaces;

import java.math.BigDecimal;

public interface GetOrderTotalUseCaseInterface {
    BigDecimal execute(Long orderCode);
} 