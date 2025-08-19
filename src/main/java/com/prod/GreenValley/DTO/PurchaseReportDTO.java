package com.prod.GreenValley.DTO;

import java.math.BigDecimal;

public record PurchaseReportDTO (
    String productName,
    BigDecimal totalQuantity,
    BigDecimal totalSalePrice,
    BigDecimal totalVolume) {
}


