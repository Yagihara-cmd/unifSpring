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

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class Sales {

    //注文番号
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 20)
    private int orderno;

    public int getOrderno() {
        return orderno;
    }

    public void setOrderno(int orderno) {
        this.orderno = orderno;

    }

    //ユーザー名
    @Column(length = 8)
    private String userid;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    //ユニフォーム名
    @Column(length = 8)
    private String uniid;

    public String getUniid() {
        return uniid;
    }

    public void setUniid(String uniid) {
        this.uniid = uniid;
    }

    //数量
    @Column(length = 20)
    private int quantity;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    //注文日時
    @Column(length = 20)
    @JsonFormat(pattern = "yyyy/MM/dd")

    private Date date;

    public Date getDate() {
        return date;

    }

    public void setDate(Date date) {
        this.date = date;
    }

    //入金状況
    @Column(length = 11)
    private String paymentstatus;

    public String getPaymentstatus() {
        return paymentstatus;
    }

    public void setPaymentstatus(String paymentstatus) {
        this.paymentstatus = paymentstatus;
    }

    //発送状況
    @Column(length = 11)
    private String shippingstatus;

    public String getShippingstatus() {
        return shippingstatus;
    }

    public void setShippingstatus(String shippingstatus) {
        this.shippingstatus = shippingstatus;
    }

    //Uniformオブジェクト
    @ManyToOne(optional = false)
    @JoinColumn(name = "uniid", referencedColumnName = "uniid", insertable = false, updatable = false)
    private Uniform uniid;

    public Uniform getUniform() {
        return uniid;
    }

    public void setUniform(Uniform uniid) {
        this.uniid = uniid;
    }

}
