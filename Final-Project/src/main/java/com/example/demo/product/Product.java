package com.example.demo.product;

import com.example.demo.user.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Product {

    @Id
    @SequenceGenerator(name = "seq_gen", sequenceName = "seq_product", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_product")
    private int num;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member seller;

    private String name;
    
    private String img1;
    private String img2;
    private String img3;
    private String img4;
    private String img5;
    @Enumerated(EnumType.STRING)
    private Categories categories;    
   
    public enum Categories {
     	의류, 디지털기기, 골동품, 뷰티_잡화, 스포츠용품, 애완용품, 기타
    }
    
    public static Product create(ProductDto dto) {
    	return Product.builder()
    			.num(dto.getNum())
    			.seller(dto.getSeller())
    			.name(dto.getName())
    			.img1(dto.getImg1())
    			.img2(dto.getImg2())
    			.img3(dto.getImg3())
    			.img4(dto.getImg4())
    			.img5(dto.getImg5())
    			.categories(dto.getCategories())
    			.build();
    }
    
    @Builder
	public Product(int num, Member seller, String name, String img1, String img2, String img3, String img4, String img5,
                   Categories categories) {
		this.num = num;
		this.seller = seller;
		this.name = name;
		this.img1 = img1;
		this.img2 = img2;
		this.img3 = img3;
		this.img4 = img4;
		this.img5 = img5;
		this.categories = categories;
	}

}
