package com.spring.mallapi.service;

import java.lang.StackWalker.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.mallapi.domain.Product;
import com.spring.mallapi.domain.ProductImage;
import com.spring.mallapi.dto.PageRequestDTO;
import com.spring.mallapi.dto.PageResponseDTO;
import com.spring.mallapi.dto.ProductDTO;
import com.spring.mallapi.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService{

	private final ProductRepository productRepository;
	
	@Override
	public PageResponseDTO<ProductDTO> getList(PageRequestDTO pageRequestDTO){
		
		log.info("getList..............");
		
		Pageable pageable = PageRequest.of(
				pageRequestDTO.getPage() - 1, //페이지 시작번호가 0부터
				pageRequestDTO.getSize(),
				Sort.by("pno").descending()
				);
		
		Page<Object[]> result = productRepository.selectList(pageable);
		
		List<ProductDTO> dtoList = result.get().map(arr -> {
			Product product = (Product) arr[0];
			ProductImage productImage = (ProductImage) arr[1];
			
			ProductDTO productDTO = ProductDTO.builder()
					.pno(product.getPno())
					.pname(product.getPname())
					.pdesc(product.getPdesc())
					.price(product.getPrice())
					.build();
			
			String imageStr = productImage.getFileName();
			productDTO.setUploadFiledNames(List.of(imageStr));
			
			return productDTO;
		}).collect(Collectors.toList());
		
		long totalCount = result.getTotalElements();
		
		return PageResponseDTO.<ProductDTO>withAll()
				.dtoList(dtoList)
				.totalCount(totalCount)
				.pageRequestDTO(pageRequestDTO)
				.build();
	}
	
	@Override
	public Long register(ProductDTO productDTO) {
		Product product = dtoToEntity(productDTO);
		Product result = productRepository.save(product);
		return result.getPno();
	}
	
	private Product dtoToEntity(ProductDTO productDTO) {
		Product product = Product.builder()
				.pno(productDTO.getPno())
				.pname(productDTO.getPname())
				.pdesc(productDTO.getPdesc())
				.price(productDTO.getPrice())
				.build();
				
		//업로드 처리가 끝난 파일들의 이름 리스트
		List<String> uploadFileNames = productDTO.getUploadFiledNames();
		
		if(uploadFileNames == null) {
			return product;
		}
		
		uploadFileNames.stream().forEach(uploadName -> {
			product.addImageString(uploadName);
		});
		return product;
	}
	
	@Override
	public ProductDTO get(Long pno) {
		Optional<Product> result = productRepository.selectOne(pno);
		
		Product product = result.orElseThrow();
		
		ProductDTO productDTO = entityToDTO(product);
		
		return productDTO;
	}
	
	private ProductDTO entityToDTO(Product product) {
		ProductDTO productDTO = ProductDTO.builder()
				.pno(product.getPno())
				.pname(product.getPname())
				.pdesc(product.getPdesc())
				.price(product.getPrice())
				.build();
		
		List<ProductImage> imageList = product.getImageList();
		if(imageList == null || imageList.size() == 0) {
			return productDTO;
		}
		
		List<String> fileNameList = imageList.stream().map(productImage -> productImage.getFileName()).toList();
		
		productDTO.setUploadFiledNames(fileNameList);
		
		return productDTO;
	}
	
	@Override
	public void modify(ProductDTO productDTO) {
		//step1 read
		Optional<Product> result = productRepository.findById(productDTO.getPno());
		
		Product product = result.orElseThrow();
		
		//change pname, pdesc, price
		product.changeName(productDTO.getPname());
		product.changeDesc(productDTO.getPdesc());
		product.changePrice(productDTO.getPrice());
		
		//upload file -- clear first
		product.clearList();
		
		List<String> uploadFileNames = productDTO.getUploadFiledNames();
		
		if(uploadFileNames != null & uploadFileNames.size() > 0) {
			uploadFileNames.stream().forEach(uploadName -> {
				product.addImageString(uploadName);
			});
		}
		productRepository.save(product);
	}
	
	@Override
	public void remove(Long pno) {
		productRepository.updateToDelete(pno, true);
	}
}
