package com.business.converter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.business.dto.OrderDTO;
import com.business.dto.OrderItemDTO;
import com.business.entity.OrderEntity;
import com.business.entity.OrderItemEntity;
import com.business.entity.UserEntity;
import com.business.repository.OrderItemRepository;
import com.business.repository.UserRepository;

@Component
public class OrderConverter {
	public String generateSku() {
        int randomSku = (int) (Math.random() * 900000) + 100000;
        return String.valueOf(randomSku);
    }
	public Date getCurrentTime() {
	    return new Date();
	}
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private OrderItemConverter orderItemConverter;
	
	
	public OrderEntity toEntity(OrderDTO dto) {
		OrderEntity entity = new OrderEntity();
		entity.setSku(generateSku());
		entity.setFullName(dto.getFullName());
		entity.setSex(dto.getSex());
		entity.setOrderPhone(dto.getOrderPhone());
		entity.setEmail(dto.getEmail());
		entity.setOrderTime(getCurrentTime());
		entity.setOrderAddress(dto.getOrderAddress());
		entity.setTotalPrice(dto.getTotalPrice());
		entity.setStatus("Chờ xác nhận");
		entity.setCheckCmt(0);
		UserEntity userEntity = userRepository.findOne(dto.getUserId());
		entity.setUser(userEntity);
		return entity;
	}
	public OrderDTO toDTO(OrderEntity entity) {
		OrderDTO dto = new OrderDTO();
		dto.setId(entity.getId());
		dto.setSku(entity.getSku());
		dto.setFullName(entity.getFullName());
		dto.setSex(entity.getSex());
		dto.setOrderPhone(entity.getOrderPhone());
		dto.setEmail(entity.getEmail());
		dto.setOrderTime(entity.getOrderTime());
		dto.setOrderAddress(entity.getOrderAddress());
		dto.setTotalPrice(entity.getTotalPrice());
		dto.setStatus(entity.getStatus());
		dto.setCheckCmt(entity.getCheckCmt());
		
		List<OrderItemDTO> orderItemDTOs = new ArrayList<>();
		List<OrderItemEntity> orderItemEntities = orderItemRepository.findByOrderId(entity.getId());
		for(OrderItemEntity orderItemEntity : orderItemEntities) {
			OrderItemDTO orderItemDTO = orderItemConverter.toDTO(orderItemEntity);
			orderItemDTOs.add(orderItemDTO);
		}
		
		dto.setOrderItemDTOs(orderItemDTOs);
		return dto;
	}
}
