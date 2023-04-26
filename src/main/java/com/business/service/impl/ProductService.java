package com.business.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.business.converter.ProductConverter;
import com.business.dto.ProductDTO;
import com.business.dto.TypeDTO;
import com.business.entity.CategoryEntity;
import com.business.entity.ColorEntity;
import com.business.entity.MemoryEntity;
import com.business.entity.ProductEntity;
import com.business.entity.ProductMemoryEntity;
import com.business.repository.CategoryRepository;
import com.business.repository.ColorRepository;
import com.business.repository.MemoryRepository;
import com.business.repository.ProductMemoryRepository;
import com.business.repository.ProductRepository;
import com.business.service.IProductService;

@Service
public class ProductService implements IProductService {
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ProductConverter productConverter;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	
	@Autowired
	private ColorRepository colorRepository;
	
	@Autowired
	private MemoryRepository memoryRepository;
	
	@Autowired
	private ProductMemoryRepository productMemoryRepository;
	
	@Override
	public ProductDTO save(ProductDTO productDTO) {
		ProductEntity productEntity = new ProductEntity();
		if(productDTO.getId() != null) {
			ProductEntity existingProduct = productRepository.findOne(productDTO.getId());
			productEntity = productConverter.toEntity(productDTO,existingProduct);
		} else {
			productEntity = productConverter.toEntity(productDTO);
		}
		
		//Category
		CategoryEntity categoryEntity = categoryRepository.findByCode(productDTO.getCategoryCode());
		productEntity.setCategory(categoryEntity);
		
		// Color
		List<Long> colors = productDTO.getColors();
		List<ColorEntity> colorEntities= new ArrayList<>();
		
		for(int i = 0; i < colors.size(); i++) {
			Long id = colors.get(i);
			 ColorEntity colorEntity = colorRepository.findOne(id);
		      colorEntities.add(colorEntity);
		}
		productEntity.setColors(colorEntities);
		
		
		//typeDTO
		productRepository.save(productEntity);
		List<TypeDTO> list = productDTO.getList();
		if(productDTO.getId() != null) {
			productMemoryRepository.delete(productDTO.getId());
		} 
		for(TypeDTO item : list) {
			String type = item.getType();
			MemoryEntity memoryEntity = memoryRepository.findByType(type);
			ProductMemoryEntity productMemoryEntity = new ProductMemoryEntity();
			productMemoryEntity.setMemory(memoryEntity);
			productMemoryEntity.setPrice(item.getPrice());
			productMemoryEntity.setProduct(productEntity);
			productMemoryRepository.save(productMemoryEntity);
		}
		
		return productConverter.toDTO(productEntity);
	}

	@Override
	public void delete(long id) {
		productRepository.delete(id);
	}

	@Override
	public List<ProductDTO> getAllProduct() {
		List<ProductEntity> productEntities = productRepository.findAll();
		List<ProductDTO> productDTOs = new ArrayList<>();
		for(ProductEntity productEntity : productEntities) {
			ProductDTO productDTO = new ProductDTO();
			productDTO = productConverter.toDTO(productEntity);
			productDTOs.add(productDTO);
		}
		return productDTOs;
	}
}
