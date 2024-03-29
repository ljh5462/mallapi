package com.spring.mallapi.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.spring.mallapi.dto.PageRequestDTO;
import com.spring.mallapi.dto.PageResponseDTO;
import com.spring.mallapi.dto.ProductDTO;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class ProductServiceTests {

	@Autowired
	ProductService productService;
	
	@Test
	public void testList() {
		//1page, 10size
		PageRequestDTO pageRequestDTO = PageRequestDTO.builder().build();
		
		PageResponseDTO<ProductDTO>  result = productService.getList(pageRequestDTO);
		
		result.getDtoList().forEach(dto -> log.info(dto));
	}
}
