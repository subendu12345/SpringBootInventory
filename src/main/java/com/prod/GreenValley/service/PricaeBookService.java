package com.prod.GreenValley.service;

import java.util.ArrayList;
import java.util.List;

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
            }else{
                message="product not found";
            }
        } catch (Exception e) {
            System.out.println("into exception ");
            // TODO: handle exception
            message = e.getMessage();
        }
        return message;
    }

    public PriceBookDTO getProductInfoByBarcode(String barcode){  
         
        PriceBookDTO pbObj = new PriceBookDTO();
        System.out.println("*******************************8888888  "+barcode); 
        PriceBookRecordDTO pb = priceBookRepo.getPriceBookByBarcode(barcode);
        System.out.println("get$$$$$$$$$$$$$$$4 "+ pb);
        if(pb != null && pb.productId() != null){
            pbObj.setProductId(pb.productId());
            pbObj.setProductName(pb.productName());
            pbObj.setProductPrice(pb.productPrice());
            pbObj.setId(pb.id());
        }
        return pbObj;
    }


    public List<PriceBookDTO> getPriceBooksByProductId(Long productId){
        List<PriceBook> priceBooks = priceBookRepo.findByProduct_Id(productId);
        System.out.println("size of pricee book "+priceBooks.size());
        List<PriceBookDTO> priceBookDTOs = new ArrayList<>();
        for (PriceBook pb : priceBooks) {
            PriceBookDTO bookDTO = new PriceBookDTO();
            bookDTO.setId(pb.getId());
            bookDTO.setProductName(pb.getProduct().getName());
            bookDTO.setProductPrice(pb.getProductPrice());
            bookDTO.setProductBarCode(pb.getProductBarCode());
            priceBookDTOs.add(bookDTO);
            
        }
        System.out.println("----> product Name "+priceBookDTOs.size());
        return priceBookDTOs;
    }
}
