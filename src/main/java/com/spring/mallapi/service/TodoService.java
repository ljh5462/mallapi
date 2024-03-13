package com.spring.mallapi.service;

import com.spring.mallapi.dto.PageRequestDTO;
import com.spring.mallapi.dto.PageResponseDTO;
import com.spring.mallapi.dto.TodoDTO;

public interface TodoService {
	Long register(TodoDTO todoDTO);
	
	TodoDTO get(Long tno);
	
	void modify(TodoDTO todoDTO);
	
	void remove(Long tno);
	
	PageResponseDTO<TodoDTO> list(PageRequestDTO pageRequestDTO);
}
