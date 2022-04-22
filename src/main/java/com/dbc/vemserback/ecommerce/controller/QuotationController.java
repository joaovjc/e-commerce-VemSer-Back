package com.dbc.vemserback.ecommerce.controller;

import com.dbc.vemserback.ecommerce.dto.quotation.QuotationCreateDTO;
import com.dbc.vemserback.ecommerce.dto.quotation.QuotationDTO;
import com.dbc.vemserback.ecommerce.service.QuotationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/quotation")
@RequiredArgsConstructor
public class QuotationController {

    private final QuotationService quotationService;


    @GetMapping("/getAll")
    public List<QuotationDTO> getAll() {
        return quotationService.quotationList();
    }

    @PostMapping("/create")
    public QuotationDTO create(QuotationCreateDTO quotationDTO) {
        return quotationService.createQuotation(quotationDTO);
    }


}