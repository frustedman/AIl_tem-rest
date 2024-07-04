package com.example.demo.product;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.product.Product.Categories;
import com.example.demo.user.Member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ProductDto {

    private int num;
    private Member seller;
    private String name;
    private String img1;
    private String img2;
    private String img3;
    private String img4;
    private String img5;
    private Categories categories;
	private MultipartFile f1;
	private MultipartFile f2;
	private MultipartFile f3;
	private MultipartFile f4;
	private MultipartFile f5;

    public static ProductDto create(Product p) {
    	return ProductDto.builder()
    			.num(p.getNum())
    			.seller(p.getSeller())
    			.name(p.getName())
    			.img1(p.getImg1())
    			.img2(p.getImg2())
    			.img3(p.getImg3())
    			.img4(p.getImg4())
    			.img5(p.getImg5())
    			.categories(p.getCategories())
    			.build();
    }
    
    @Builder
	public ProductDto(int num, Member seller, String name, String img1, String img2, String img3, String img4, String img5,
                      Categories categories, MultipartFile f1, MultipartFile f2, MultipartFile f3, MultipartFile f4, MultipartFile f5) {
		this.num = num;
		this.seller = seller;
		this.name = name;
		this.img1 = img1;
		this.img2 = img2;
		this.img3 = img3;
		this.img4 = img4;
		this.img5 = img5;
		this.categories = categories;
		this.f1=f1;
		this.f2=f2;
		this.f3=f3;
		this.f4=f4;
		this.f5=f5;

	}


}
