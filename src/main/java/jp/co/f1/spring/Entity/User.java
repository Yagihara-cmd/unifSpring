package jp.cp.f1.spring.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "userinfo")
public class user {

	@Id
	@Column(length = 20)
	private String userid;
	
	@Column(length = 20 )
	private String username;
	
	@Column(length = 100)
	private String useraddress;
	
	@Column(length = 100)
	private String email;
	
	@Column(length = 20)
	private String password;
	
	@Column(length = 1)
	private String authority;
}