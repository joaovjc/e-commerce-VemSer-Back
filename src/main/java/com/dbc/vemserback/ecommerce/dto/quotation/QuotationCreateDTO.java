package com.dbc.vemserback.ecommerce.dto.quotation;

import com.dbc.vemserback.ecommerce.enums.StatusEnum;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class QuotationCreateDTO {

    private Integer topicId;

    private BigDecimal quotationPrice;

}
