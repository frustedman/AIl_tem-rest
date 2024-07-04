package com.example.demo.product;

import com.example.demo.user.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductDao dao;

    // 추가, 수정
    public ProductDto save(ProductDto dto) {
        Product p = dao.save(Product.create(dto));
        return ProductDto.create(p);
    }

    // 번호로 삭제
    public void delProd(int num) {
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
