package jp.cp.f1.spring.Entity;


@Entity
@Table(name="uniforminfo")
public class uniform {

	@Id
	@Column(length = 20)
	private String uniid;
	
	
}
