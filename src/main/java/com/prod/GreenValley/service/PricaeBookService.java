package com.prod.GreenValley.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prod.GreenValley.DTO.PriceBookDTO;
import com.prod.GreenValley.DTO.PriceBookRecordDTO;
import com.prod.GreenValley.Entities.PriceBook;
import com.prod.GreenValley.Entities.Product;
import com.prod.GreenValley.repository.PriceBookRepo;
import com.prod.GreenValley.repository.ProductRepo;

@Service
public class PricaeBookService {

    @Autowired
    private PriceBookRepo priceBookRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ProductStockService stockService;

    private final Map<String, PriceBookDTO> barcodeCache = new ConcurrentHashMap<>();

    public String generateUniqueBarcode() {
        long candidate = System.currentTimeMillis();
        while (priceBookRepo.existsByProductBarCode(String.valueOf(candidate))) {
            candidate++;
        }
        return String.valueOf(candidate);
    }

    public Map<Long, Double> findLatestPriceByProduct() {
        Map<Long, Double> latestPrices = new java.util.HashMap<>();
        for (PriceBook priceBook : priceBookRepo.findAllByOrderByModifiedDateDescIdDesc()) {
            latestPrices.putIfAbsent(priceBook.getProduct().getId(), priceBook.getProductPrice());
        }
        return latestPrices;
    }

    public String savePriceBook(PriceBookDTO priceBookDTO) {
        String message = "success";
        
        try {
            PriceBook pb = new PriceBook();
            PriceBook oldPB = priceBookRepo.findByProductBarCode(priceBookDTO.getProductBarCode());
            if(oldPB!=null && oldPB.getId() !=null){
                pb.setId(oldPB.getId());
                pb = oldPB;
            }
            pb.setProductBarCode(priceBookDTO.getProductBarCode());
            pb.setProductPrice(priceBookDTO.getProductPrice());
            Product product = productRepo.findById(priceBookDTO.getProductId()).orElse(null);

            if (product != null) {
                pb.setProduct(product);

                priceBookRepo.save(pb);
                barcodeCache.remove(priceBookDTO.getProductBarCode()); // Invalidate cache on save
            }else{
                message="product not found";
            }
        } catch (Exception e) {
            // TODO: handle exception
            message = e.getMessage();
        }
        return message;
    }

    public PriceBookDTO getProductInfoByBarcode(String barcode){  
        if (barcodeCache.containsKey(barcode)) {
            PriceBookDTO cachedPb = barcodeCache.get(barcode);
            // Stock might have changed, so we should ideally update it or not cache it for too long.
            // For now, let's refresh stock even if cached, or just accept cached stock.
            // User requested "use cache technique for less db call", so I'll keep it simple.
            // However, stock is dynamic. Let's refresh stock from DB but keep other info.
            cachedPb.setAvailableStock(stockService.getAvailableStockByProductId(cachedPb.getProductId()));
            return cachedPb;
        }
         
        PriceBookDTO pbObj = new PriceBookDTO();
        PriceBookRecordDTO pb = priceBookRepo.getPriceBookByBarcode(barcode);
        if(pb != null && pb.productId() != null){
            pbObj.setProductId(pb.productId());
            pbObj.setProductBarCode(pb.productBarCode());
            pbObj.setProductName(pb.productName());
            pbObj.setProductPrice(pb.productPrice());
            pbObj.setId(pb.id());
            pbObj.setAvailableStock(stockService.getAvailableStockByProductId(pb.productId()));
            barcodeCache.put(barcode, pbObj);
        }
        return pbObj;
    }


    public List<PriceBookDTO> getPriceBooksByProductId(Long productId){
        List<PriceBook> priceBooks = priceBookRepo.findByProduct_Id(productId);
        List<PriceBookDTO> priceBookDTOs = new ArrayList<>();
        for (PriceBook pb : priceBooks) {
            PriceBookDTO bookDTO = new PriceBookDTO();
            bookDTO.setId(pb.getId());
            bookDTO.setProductName(pb.getProduct().getName());
            bookDTO.setProductPrice(pb.getProductPrice());
            bookDTO.setProductBarCode(pb.getProductBarCode());
            priceBookDTOs.add(bookDTO);
            
        }
        return priceBookDTOs;
    }
}
