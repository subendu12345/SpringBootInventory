package com.prod.GreenValley.DTO;

import java.math.BigDecimal;
import java.util.Date;

public record SaleReportDTO (
    String productName,
    BigDecimal totalQuantity,
    BigDecimal totalSalePrice,
    BigDecimal totalVolume,
    Date saleDate,
    BigDecimal unitPrice,
    String categoryName) {
}
