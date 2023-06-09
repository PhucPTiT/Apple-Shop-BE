package com.business.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.business.dto.ColorDTO;
import com.business.service.impl.ColorService;

@CrossOrigin
@RestController 
public class ColorAPI {

	@Autowired
	private ColorService colorService;
	
	@PostMapping(value = "/api/color")
    public ResponseEntity<ColorDTO> addProduct(@RequestBody ColorDTO model) {
		try {
			ColorDTO colorDTO = colorService.save(model);
			return ResponseEntity.ok(colorDTO);
			
		} catch(RuntimeException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
    }
	
	@PutMapping(value = "/api/color/{id}")
	public ResponseEntity<ColorDTO> updateColor(@RequestBody ColorDTO model, @PathVariable("id") long id) {
		try {	
			model.setId(id);
			return ResponseEntity.ok(colorService.save(model));
		} catch (RuntimeException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}
	@DeleteMapping(value = "/api/color/{id}")
	public void deleteColor(@PathVariable("id") long id) {
		colorService.delete(id);
	}
	
	@GetMapping(value = "/api/color")
	public List<ColorDTO> getAllColor() {
		return colorService.getAllColor();
	}
}
