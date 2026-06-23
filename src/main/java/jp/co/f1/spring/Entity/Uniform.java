package jp.co.f1.spring.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="uniforminfo")
public class Uniform {

	@Id
	@Column(length = 8)
	private String uniid;
	
	@Column(length = 20)
	private String uniname;
	
	private Integer price;
	
	private Integer stock;

	public String getUniid() {
		return uniid;
	}

	public void setUniid(String uniid) {
		this.uniid = uniid;
	}

	public String getUniname() {
		return uniname;
	}

	public void setUniname(String uniname) {
		this.uniname = uniname;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getStook() {
		return stock;
	}

	public void setStook(Integer stook) {
		this.stock = stook;
	}
	
	
}
