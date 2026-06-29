package jp.co.f1.spring.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;


@Entity
@Table(name = "uniforminfo")
public class Uniform {

	public interface Group1 {
	}

	public interface Group2 {
	}

	@GroupSequence({
			Group1.class,
			Group2.class
	})
	public interface All {
	}

	@Id
	@Column(length = 8)
	private String uniid;

	@Column(length = 20)
	private String uniname;

	@NotEmpty(message = "価格を入力してください", groups = Group1.class)
	@Pattern(groups = Group2.class, regexp = "^\\d+$", message = "価格は数字のみで入力してください。")
	private Integer price;

	@NotEmpty(message = "在庫数を入力してください", groups = Group1.class)
	@Pattern(groups = Group2.class, regexp = "^\\d+$", message = "価格は数字のみで入力してください。")
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

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

}
