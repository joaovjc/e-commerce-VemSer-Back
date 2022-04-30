package com.dbc.vemserback.ecommerce.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dbc.vemserback.ecommerce.dto.Item.ItemCreateDTO;
import com.dbc.vemserback.ecommerce.dto.Item.ItemDTO;
import com.dbc.vemserback.ecommerce.dto.Item.ItemFullDTO;
import com.dbc.vemserback.ecommerce.entity.ItemEntity;
import com.dbc.vemserback.ecommerce.entity.TopicEntity;
import com.dbc.vemserback.ecommerce.enums.StatusEnum;
import com.dbc.vemserback.ecommerce.exception.BusinessRuleException;
import com.dbc.vemserback.ecommerce.repository.post.ItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemService {
	private final ItemRepository itemRepository;
	private final FileService fileService;
	private final TopicService topicService;
	
	
	public ItemFullDTO createPurchase(ItemCreateDTO purchaseDTO, MultipartFile file, int idUser, Integer idTopic) throws BusinessRuleException {
		//checa para ver se a imagem está nula ou não
		if(file==null)throw new BusinessRuleException("a imagem do item não pode ser nula");
		String originalFilename = file.getOriginalFilename();
		
		//checa para ver o status do topico
		TopicEntity topicEntity = topicService.topicById(idTopic);
		if(topicEntity.getStatus()!=StatusEnum.CREATING)throw new BusinessRuleException("o topico não esta mais aberto a mudanças");
		
		ItemEntity build = ItemEntity.builder().itemName(purchaseDTO.getName()).description(purchaseDTO.getDescription())
				.value(purchaseDTO.getPrice()).fileName(originalFilename).file(fileService.convertToByte(file)).topicId(idTopic).topicEntity(topicEntity).build();
		//persiste um item
		itemRepository.save(build);
		
		//converte um item para dto
		return ItemFullDTO.builder()
				.description(build.getDescription())
				.file(new String(build.getFile()))
				.itemName(build.getItemName())
				.itemId(build.getItemId())
				.value(build.getValue())
				.build();
	}

	public List<ItemDTO> listPurchasesByTopicId(Integer topicId) throws BusinessRuleException {
		//checa para ver se o topico existe e traz todos os item convertido para item dto
		TopicEntity topicById = topicService.topicById(topicId);
		return topicById.getItems().stream().map(ent->{
            return ItemDTO.builder()
                    .description(ent.getDescription())
                    .itemName(ent.getItemName())
                    .file(ent.getFile()!=null?new String(ent.getFile()):null)
                    .value(ent.getValue()).build();
        }).collect(Collectors.toList());
	}

	public void deleteById(int idItem, int userId) throws BusinessRuleException {
		//checa se o item existe
		ItemEntity purchase = this.getById(idItem);
		//checa se o item é do usuario
		if(purchase.getTopicEntity().getUserId()!=userId)throw new BusinessRuleException("esse item não pertence a seu usuário");
		//checa se o status do topico permite alterações
		if(purchase.getTopicEntity().getStatus()!=StatusEnum.CREATING)throw new BusinessRuleException("o topico já não pode ser mais alterado");
		//delata o item
		this.itemRepository.delete(purchase);
	}
	
	private ItemEntity getById(int idItem) throws BusinessRuleException {
		return this.itemRepository.findById(idItem).orElseThrow(()-> new BusinessRuleException("item não encontrado, por favor atualize a pagina"));
	}

}
