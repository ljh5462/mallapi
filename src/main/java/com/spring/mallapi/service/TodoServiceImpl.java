package com.spring.mallapi.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.mallapi.domain.Todo;
import com.spring.mallapi.dto.PageRequestDTO;
import com.spring.mallapi.dto.PageResponseDTO;
import com.spring.mallapi.dto.TodoDTO;
import com.spring.mallapi.repository.TodoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor // 생성자 자동 주입
public class TodoServiceImpl implements TodoService{

	//자동주입 대상은 final로
	private final ModelMapper modelMapper;
	
	private final TodoRepository todoRepository;
	
	@Override
	public Long register(TodoDTO todoDTO) {
		Todo todo = modelMapper.map(todoDTO, Todo.class);
		
		Todo savedTodo = todoRepository.save(todo);
		
		return savedTodo.getTno();
	}
	
	@Override
	public TodoDTO get(Long tno) {
		Optional<Todo> result = todoRepository.findById(tno);
		Todo todo = result.orElseThrow();
		TodoDTO dto = modelMapper.map(todo, TodoDTO.class);
		return dto;
	}

	@Override
	public void modify(TodoDTO todoDTO) {
		Optional<Todo> result = todoRepository.findById(todoDTO.getTno());
		Todo todo = result.orElseThrow();
		todo.changeTitle(todoDTO.getTitle());
		todo.changeComplete(todoDTO.isComplete());
		todo.changeDueDate(todoDTO.getDueDate());
		
		todoRepository.save(todo);
	}

	@Override
	public void remove(Long tno) {
		todoRepository.deleteById(tno);
	}

	@Override
	public PageResponseDTO<TodoDTO> list(PageRequestDTO pageRequestDTO) {
		
		Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(), Sort.by("tno").descending());
		
		Page<Todo> result = todoRepository.findAll(pageable);
		
		List<TodoDTO> dtoList = result.getContent().stream()
				.map(todo -> modelMapper.map(todo, TodoDTO.class))
				.collect(Collectors.toList());
		
		long totalCount = result.getTotalElements();
		
		PageResponseDTO<TodoDTO> responseDTO = PageResponseDTO.<TodoDTO>withAll()
				.dtoList(dtoList)
				.pageRequestDTO(pageRequestDTO)
				.totalCount(totalCount)
				.build();
		return responseDTO;
	}
}
