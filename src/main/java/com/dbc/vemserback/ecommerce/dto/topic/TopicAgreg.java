package com.dbc.vemserback.ecommerce.dto.topic;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.annotation.Id;

import com.dbc.vemserback.ecommerce.enums.StatusEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TopicAgreg {
	@Id
	private String topicId;
	private String title;
	private LocalDate date;
	private BigDecimal totalValue;
	private StatusEnum status;
}
