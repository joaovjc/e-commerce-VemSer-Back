package com.dbc.vemserback.ecommerce.controller;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import java.util.List;

import javax.validation.Valid;

import com.dbc.vemserback.ecommerce.dto.topic.FullTopicDTO;
import com.dbc.vemserback.ecommerce.dto.topic.TopicFinancierDTO;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.dbc.vemserback.ecommerce.dto.TopicDTO;
import com.dbc.vemserback.ecommerce.dto.PurchaseList.PurchaseDTO;
import com.dbc.vemserback.ecommerce.entity.TopicEntity;
import com.dbc.vemserback.ecommerce.exception.BusinessRuleException;
import com.dbc.vemserback.ecommerce.service.PurchaseService;
import com.dbc.vemserback.ecommerce.service.TopicService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/topic")
@RequiredArgsConstructor
public class TopicController {
	private final TopicService topicService;
	private final PurchaseService purchaseService;
	
	@PostMapping("/create-topic")
	public Integer createTopic(@RequestBody TopicDTO dto) throws BusinessRuleException {
		Object userb = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return topicService.createTopic(dto,Integer.parseInt((String) userb));
	}
//	@PostMapping(path = "/create-item/{topic-id}", consumes = {MULTIPART_FORM_DATA_VALUE})
//	public boolean createItem(@PathVariable(name = "topic-id") String idTopic,@Valid @ModelAttribute(name = "data") PurchaseDTO CreateDTO,
//			@RequestPart MultipartFile file, BindingResult bindingResult)
//			throws BusinessRuleException, InterruptedException {
//		System.out.println("chegou no controller: "+ CreateDTO.getName());
//		Object userb = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		return purchaseService.createPurchase(CreateDTO, file, Integer.parseInt((String) userb), idTopic);
//	}
	@GetMapping("/get-topics")
	public List<TopicEntity> listTopics(){
		return topicService.listTopics();
	}

	@GetMapping("/get-full-topics")
	public List<FullTopicDTO> listFullTopics(){
		return topicService.listTopicsFull();
	}
}
