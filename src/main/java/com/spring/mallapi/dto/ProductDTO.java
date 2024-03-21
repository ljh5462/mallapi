package com.spring.mallapi.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
	private Long pno;
	private String pname;
	private int price;
	private String pdesc;
	private boolean delFlag;
	

	private List<MultipartFile> files = new ArrayList<>();
	
	private List<String> uploadFiledNames = new ArrayList<>();
}
