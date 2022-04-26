package com.dbc.vemserback.ecommerce.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.dbc.vemserback.ecommerce.dto.quotation.QuotationByTopicDTO;
import com.dbc.vemserback.ecommerce.dto.quotation.QuotationDTO;
import com.dbc.vemserback.ecommerce.entity.QuotationEntity;
import com.dbc.vemserback.ecommerce.entity.TopicEntity;
import com.dbc.vemserback.ecommerce.entity.UserEntity;
import com.dbc.vemserback.ecommerce.enums.StatusEnum;
import com.dbc.vemserback.ecommerce.exception.BusinessRuleException;
import com.dbc.vemserback.ecommerce.repository.post.QuotationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import static com.dbc.vemserback.ecommerce.enums.StatusEnum.MANAGER_APPROVED;

@Service
@RequiredArgsConstructor
public class QuotationService {

    private final QuotationRepository quotationRepository;
    private final TopicService topicService;
    private final ObjectMapper objectMapper;
    private final UserService userService;


    public List<QuotationDTO> quotationList() {
        return quotationRepository.findAll().stream().map(quotation -> objectMapper.convertValue(quotation, QuotationDTO.class)).collect(java.util.stream.Collectors.toList());
    }

    public boolean createQuotation(Integer topicId, Double preco, int userId) throws BusinessRuleException {

        TopicEntity topicEntity = topicService.topicById(topicId);
        if(topicEntity.getStatus()!=StatusEnum.OPEN)throw new BusinessRuleException("the topic isnt open to cotations");

        UserEntity userEntity = userService.findById(userId);

        QuotationEntity build = QuotationEntity.builder()
                .quotationPrice(new BigDecimal(preco))
                .quotationStatus(StatusEnum.OPEN)
                .topic(topicEntity)
                .userEntity(userEntity)
                .build();

        QuotationEntity save = quotationRepository.save(build);
        
        save.setTopicId(topicId);
        save.setUserId(userId);
        
        return true;
    }

    public List<QuotationEntity> managerAproveOrReproveTopic(Integer topicId, Integer quotationId, Boolean flag) throws BusinessRuleException {
        TopicEntity topic = topicService.topicById(topicId);

        if(topic.getStatus()!=StatusEnum.OPEN){throw new BusinessRuleException("Topic not open");}
        if(topic.getQuotations().size()<2){throw new BusinessRuleException("Topic does not have two quotations");}
            List<QuotationEntity> quotationEntities = topic.getQuotations();

        quotationEntities.forEach(quotationEntity -> {
            quotationEntity.setQuotationStatus(StatusEnum.MANAGER_REPROVED);}
        );
            if (flag){
                quotationEntities.stream().filter(quotationEntity -> quotationEntity.getQuotationId().equals(quotationId)
                ).findFirst().orElseThrow((() -> new BusinessRuleException("Quotation not found"))).setQuotationStatus(MANAGER_APPROVED);
                topic.setStatus(MANAGER_APPROVED);
            } else {topic.setStatus(StatusEnum.MANAGER_REPROVED);}
            topic.setQuotations(quotationEntities);
            this.topicService.save(topic);
        return quotationEntities;
    }

    public List<QuotationByTopicDTO> quotationsByTopic(Integer topicId) throws BusinessRuleException {
        TopicEntity topic = topicService.topicById(topicId);
        return topic.getQuotations().stream().map(quotation -> objectMapper.convertValue(quotation, QuotationByTopicDTO.class)).collect(Collectors.toList());
    }

    public QuotationEntity findQuotationById(Integer quotationId) throws BusinessRuleException {
        return quotationRepository.findById(quotationId).orElseThrow((() -> new BusinessRuleException("Quotation not found")));
    }

}
