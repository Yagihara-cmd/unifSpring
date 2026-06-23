package jp.co.f1.spring.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;

@Entity
public class CheckUser {

	//ユーザー名
	@Id
	@NotEmpty
	@Column(length = 20)
	private String userid;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	//旧パスワード
	@Column(length = 100)
	private String oldPassword;

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassWord(String oldPassWord) {
		this.oldPassword = oldPassWord;
	}

	//新パスワード
	@Column(length = 100)
	private String newPassword;

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	//新パスワード（確認用
	@Column(length = 100)
	private String ConfirmPassword;

	public String getConfirmPassword() {
		return ConfirmPassword;
	}

	public void setConfirmPassword(String ConfirmPassword) {
		this.ConfirmPassword = ConfirmPassword;
	}

	//メールアドレス
	@Column(length = 100)
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	//権限
	@Column(length = 11)
	private String authority;

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

}
