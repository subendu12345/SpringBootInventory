package com.prod.GreenValley.DTO;

import java.math.BigDecimal;

public record SaleReportDTO (
    String productName,
    BigDecimal totalQuantity,
    BigDecimal totalSalePrice,
    BigDecimal totalVolume) {
}
