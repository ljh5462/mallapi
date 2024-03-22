package com.spring.mallapi.repository;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import com.spring.mallapi.domain.Product;
import com.spring.mallapi.dto.ProductDTO;
import com.spring.mallapi.service.ProductService;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class ProductRepositoryTests {

	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	ProductService productService;
	
//	@Test
//	public void testInsert() {
//		for(int i = 0; i < 10; i++) {
//			Product product = Product.builder()
//					.pname("상품"+i)
//					.price(100*i)
//					.pdesc("상품설명 " + i)
//					.build();
//			
//			product.addImageString(UUID.randomUUID().toString() + "_" + "IMAGE1.jpg");
//			product.addImageString(UUID.randomUUID().toString() + "_" + "IMAGE2.jpg");
//			
//			productRepository.save(product);
//			
//			log.info("----------------------------");
//		}
//	}
	
//	@Transactional
//	@Test
//	public void testRead() {
//		Long pno = 1L;
//		
//		Optional<Product> result = productRepository.findById(pno);
//		
//		Product product = result.orElseThrow();
//		
//		log.info(product);
//		log.info(product.getImageList());
//	}
	
//	@Test
//	public void testRead2() {
//		Long pno = 1L;
//		
//		Optional<Product> result = productRepository.selectOne(pno);
//		
//		Product product = result.orElseThrow();
//		
//		log.info(product);
//		log.info(product.getImageList());
//	}

//	@Commit
//	@Transactional
//	@Test
//	public void testDelete() {
//		Long pno = 2L;
//		productRepository.updateToDelete(pno, true);
//	}
	
//	@Test
//	public void testUpdate() {
//		Long pno = 10L;
//		
//		Product product = productRepository.selectOne(pno).get();
//		
//		product.changeName("10번 상품");
//		product.changeDesc("10번상품 살명");
//		product.changePrice(5000);
//		
//		//첨부파일수정
//		product.clearList();
//		
//		product.addImageString(UUID.randomUUID().toString() + "-" + "NEWIMAGE1.jpg");
//		product.addImageString(UUID.randomUUID().toString() + "-" + "NEWIMAGE2.jpg");
//		product.addImageString(UUID.randomUUID().toString() + "-" + "NEWIMAGE3.jpg");
//		
//		productRepository.save(product);
//	}
	
//	@Test
//	public void testList() {
//		
//		Pageable pageable = PageRequest.of(0, 10, Sort.by("pno").descending());
//		
//		Page<Object[]> result = productRepository.selectList(pageable);
//		
//		result.getContent().forEach(arr -> log.info(Arrays.toString(arr)));
//		
//	}
	
//	@Test
//	public void testRegister() {
//		
//		ProductDTO productDTO = ProductDTO.builder()
//				.pname("새로운 상품")
//				.pdesc("신규추가상품")
//				.price(1000)
//				.build();
//		
//		//UUID 필요
//		productDTO.setUploadFiledNames(
//				java.util.List.of(
//						UUID.randomUUID()+"_"+"TEST1.jpg",
//						UUID.randomUUID()+"_"+"TEST2.jpg"
//						)
//				);
//		productService.register(productDTO);
//	}
	
	@Test
	public void testRead() {
		//실제 존재하는 번호로 테스트
		Long pno = 12L;
		
		ProductDTO productDTO = productService.get(pno);
		
		log.info(productDTO);
		log.info(productDTO.getUploadFiledNames());
	}
	
}
