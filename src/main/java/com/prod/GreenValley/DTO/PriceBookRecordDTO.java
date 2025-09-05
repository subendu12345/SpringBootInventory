package com.prod.GreenValley.DTO;

public record PriceBookRecordDTO (
    Long id,
    String productBarCode,
    Double productPrice,
    Long productId,
    String productName
){
    
}
