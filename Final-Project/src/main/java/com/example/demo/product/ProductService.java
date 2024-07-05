package com.example.demo.product;

import com.example.demo.user.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class ProductService {

	@Autowired
	private ProductDao dao;
	@Value("${spring.servlet.multipart.location}")
	private String path;

	// 추가, 수정
	public ProductDto save(ProductDto dto) {
		Product p = dao.save(Product.create(dto));
		if (!dto.getF1().isEmpty()) {
			String oname1 = dto.getF1().getOriginalFilename();
			String img1 = p.getNum() + oname1;
			File f1 = new File(path + img1);
			try {
				dto.getF1().transferTo(f1); // 업로드 파일 복사
				p.setImg1(f1.getName());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		if (!dto.getF2().isEmpty()) {
			String oname2 = dto.getF2().getOriginalFilename();
			String img2 = p.getNum() + oname2;
			File f2 = new File(path + img2);
			try {
				dto.getF2().transferTo(f2); // 업로드 파일 복사
				p.setImg2(f2.getName());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		if (!dto.getF3().isEmpty()) {
			String oname3 = dto.getF3().getOriginalFilename();
			String img3 = p.getNum() + oname3;
			File f3 = new File(path + img3);
			try {
				dto.getF3().transferTo(f3); // 업로드 파일 복사
				p.setImg3(f3.getName());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		if (!dto.getF4().isEmpty()) {
			String oname4 = dto.getF4().getOriginalFilename();
			String img4 = p.getNum() + oname4;
			File f4 = new File(path + img4);
			try {
				dto.getF4().transferTo(f4); // 업로드 파일 복사
				p.setImg4(f4.getName());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		if (!dto.getF5().isEmpty()) {
			String oname5 = dto.getF5().getOriginalFilename();
			String img5 = p.getNum() + oname5;
			File f5 = new File(path + img5);
			try {
				dto.getF5().transferTo(f5); // 업로드 파일 복사
				p.setImg5(f5.getName());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return ProductDto.create(p);
	}

	// 번호로 삭제
	public void delProd(int num) {
		ProductDto dto=ProductDto.create(dao.findById(num).orElse(null));
		//파일 이미지 삭제
		if (!dto.getF1().isEmpty()) {
			File f1 = new File(dto.getImg1());
			f1.delete();
		}
		if (!dto.getF2().isEmpty()) {
			File f2 = new File(dto.getImg2());
			f2.delete();
		}
		if (!dto.getF3().isEmpty()) {
			File f3 = new File(dto.getImg3());
			f3.delete();
		}
		if (!dto.getF4().isEmpty()) {
			File f4 = new File(dto.getImg4());
			f4.delete();
		}
		if (!dto.getF5().isEmpty()) {
			File f5 = new File(dto.getImg5());
			f5.delete();
		}
		dao.deleteById(num);
	}

	// 번호로 검색
	public ProductDto getProd(int num) {
		Product p = dao.findById(num).orElse(null);
		if (p == null) {
			return null;
		}
		return ProductDto.create(p);

	}

	// 카테고리로 검색
	public ArrayList<ProductDto> getByCategories(String categories) {
		List<Product> l = dao.findByCategories(categories);
		ArrayList<ProductDto> list = new ArrayList<>();
		for (Product p : l) {

			list.add(ProductDto.create(p));
		}
		return list;
	}

	// 이름으로 검색
	public ArrayList<ProductDto> getByName(String name) {
		List<Product> l = dao.findByNameLike(name);
		ArrayList<ProductDto> list = new ArrayList<>();
		for (Product p : l) {
			System.out.println(p.toString());
			list.add(ProductDto.create(p));
		}
		return list;
	}

	// 판매자로 검색
	public ArrayList<ProductDto> getBySeller(String seller) {
		List<Product> l = dao.findBySeller(new Member(seller, "", "", "", null, 0, "", 0, ""));
		ArrayList<ProductDto> list = new ArrayList<>();
		for (Product p : l) {
			list.add(ProductDto.create(p));
		}
		return list;
	}

	
	
}
