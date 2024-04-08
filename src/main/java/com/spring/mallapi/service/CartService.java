package com.spring.mallapi.service;

import java.util.List;

import com.spring.mallapi.dto.CartItemDTO;
import com.spring.mallapi.dto.CartItemListDTO;

import jakarta.transaction.Transactional;

@Transactional
public interface CartService {

	//장바구니 아이템 추가 혹은 변경
	public List<CartItemListDTO> addOrModify(CartItemDTO cartItemDTO);
	
	//모든 장바구니 아이템 목록
	public List<CartItemListDTO> getCartItems(String email);
	
	//아이템 삭제
	public List<CartItemListDTO> remove(Long cino);
}
