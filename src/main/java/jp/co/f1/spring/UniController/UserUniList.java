package jp.co.f1.spring.UniController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.factory.annotation.Autowired;


import jp.co.f1.spring.Repository.UniRepository;
import jp.co.f1.spring.Entity.Uniform;

@Controller
public class UserUniList {

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