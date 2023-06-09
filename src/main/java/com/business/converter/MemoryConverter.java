package com.business.converter;

import org.springframework.stereotype.Component;

import com.business.dto.MemoryDTO;
import com.business.entity.MemoryEntity;

@Component
public class MemoryConverter {
	public MemoryEntity toEntity(MemoryDTO dto) {
		MemoryEntity entity = new MemoryEntity();
		entity.setId(dto.getId());
		entity.setType(dto.getType());
		return entity;
	}
	public MemoryDTO toDTO(MemoryEntity entity) {
		MemoryDTO dto = new MemoryDTO();
		dto.setId(entity.getId());
		dto.setType(entity.getType());
		return dto;
	}
	public MemoryEntity toEntity(MemoryDTO dto, MemoryEntity entity) {
		entity.setId(dto.getId());
		entity.setType(dto.getType());
		return entity;
	}
}
