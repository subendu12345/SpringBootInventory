package com.prod.GreenValley.DTO;

import java.math.BigDecimal;
import java.util.Date;;

public record PurchaseReportDTO (
    String productName,
    BigDecimal totalQuantity,
    BigDecimal totaPurchasePrice,
    BigDecimal totalVolume,
    Date dateOfPurchase,
    BigDecimal unitPrice,
    String categoryName) {
}


