/*
 * 
 *  ユーザーのユニフォーム一覧
 * 
 *  担当:塚田
 *  最終更新:2026/06/24-10:30
 * 
 * 
 */

package jp.co.f1.spring.UniController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.PersistenceContext;
import jakarta.persistence.EntityManager;
import jakarta.annotation.PostConstruct;

import jp.co.f1.spring.Repository.UniRepository;
import jp.co.f1.spring.Dao.UniDao;
import jp.co.f1.spring.Entity.Uniform;

@Controller
public class UserUniList {
	
	// EntityManager自動インスタンス化
	@Autowired
	private EntityManager entityManager;

	// DAO自動インスタンス化
	@Autowired
	private UniDao UniDao;

	@PostConstruct
	public void init() {
		UniDao = new UniDao(entityManager);
	}

	// Repositoryインターフェースを自動インスタンス化
	@Autowired
	private UniRepository uniforminfo;

	//「/userUniformList」へアクセスがあった場合
	@GetMapping("/userUniformList")
	
	public ModelAndView userUniList(ModelAndView mav) {
		
		// 書籍情報を全件取得する
		Iterable<Uniform> uniform_list = uniforminfo.findAll();

		// Viewに渡す変数をModelに格納
		mav.addObject("uniform_list", uniform_list);

		// 画面に出力するViewを指定
		mav.setViewName("view/users/userUniformList");

		return mav;
	}
	
}