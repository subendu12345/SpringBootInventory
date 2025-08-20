package com.prod.GreenValley.DTO;

import java.util.Date;

public record PurchaseEntryRecordDTO (
    Long id,
    Date dateOfPurchase,
    String supplierInfo
){}
    

