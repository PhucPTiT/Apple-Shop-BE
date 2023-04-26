package com.business.converter;

import org.springframework.stereotype.Component;

import com.business.dto.ColorDTO;
import com.business.entity.ColorEntity;

@Component
public class ColorConverter {
	public ColorEntity toEntity(ColorDTO dto) {
		ColorEntity entity = new ColorEntity();
		entity.setColor(dto.getColor());
		return entity;
	}
	public ColorDTO toDTO(ColorEntity entity) {
		ColorDTO dto = new ColorDTO();
		dto.setId(entity.getId());
		dto.setColor(entity.getColor());
		return dto;
	}
	public ColorEntity toEntity(ColorDTO dto, ColorEntity entity) {
		entity.setColor(dto.getColor());
		return entity;
	}
}
