package com.spring.mallapi.service;

import org.springframework.transaction.annotation.Transactional;

import com.spring.mallapi.dto.PageRequestDTO;
import com.spring.mallapi.dto.PageResponseDTO;
import com.spring.mallapi.dto.ProductDTO;

@Transactional
public interface ProductService {

	PageResponseDTO<ProductDTO> getList(PageRequestDTO pageRequestDTO);
	
	Long register(ProductDTO productDTO);
	
	ProductDTO get(Long pno);
	
	void modify(ProductDTO productDTO);
	
	void remove(Long pno);
}
