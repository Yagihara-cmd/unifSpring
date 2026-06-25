package jp.co.f1.spring.Entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "orderinfo")
public class Order {

	@Id
	@Column(length = 20)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int orderno;

	@Column(length = 20)
	private String userid;

	@Column(length = 8)
	private String uniid;

	@Column(length = 20)
	private int quantity;

	@Column(length = 20)
	private Date date;

	@Column(length = 1)
	private String paymentstatus;

	@Column(length = 1)
	private String shippingstatus;

	public int getOrderno() {
		return orderno;
	}

	public void setOrderno(int orderno) {
		this.orderno = orderno;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUniid() {
		return uniid;
	}

	public void setUniid(String uniid) {
		this.uniid = uniid;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getPaymentstatus() {
		return paymentstatus;
	}

	public void setPaymentstatus(String paymentstatus) {
		this.paymentstatus = paymentstatus;
	}
	
	public String getShippingstatus() {
		return shippingstatus;
	}

	public void setShippingstatus(String shippingstatus) {
		this.shippingstatus = shippingstatus;
	}

	@ManyToOne
	@JoinColumn(name = "uniid", insertable = false, updatable = false)
	// 自身のカラム、相手のカラム（デフォルト：参照されるテーブルの主キー列と同じ名前。なので今回は省略可）
	private Uniform uniform;

	public Uniform getUniform() {
		return uniform;
	}

	public void setUniform(Uniform Uniform) {
		this.uniform = Uniform;
	}

}