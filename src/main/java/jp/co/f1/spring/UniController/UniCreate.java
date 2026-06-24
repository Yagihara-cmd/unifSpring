/*
 * 
 * 管理者ユニフォーム管理画面
 * 
 *  担当:田岡
 *  最終更新:2026/06/24-AM10
 * 
 * 
 */
package jp.co.f1.spring.UniController;

import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import jp.co.f1.spring.Dao.UniDao;
import jp.co.f1.spring.Entity.Uniform;
import jp.co.f1.spring.Repository.UniRepository;

@Controller
public class UniCreate {

		

	@Autowired
	private UniRepository uniforminfo;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private UniDao uniDao;
	
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");

	
	@GetMapping("/uniformCreate")
	public ModelAndView uniformCreateForm(@ModelAttribute Uniform uniform, ModelAndView mav) {

		//Viewに渡す変数をModelに格納
		mav.addObject("uniform", uniform);

		// 画面に出力するViewを指定
		mav.setViewName("view/uniformCreate");
		// ModelとView情報を返す
		return mav;
	}

	
	@PostMapping("/uniformCreate")
	public ModelAndView uniformCreatePost(@ModelAttribute @Validated Uniform uniform, BindingResult result,
			ModelAndView mav) {
	
		if (result.hasErrors()) {
			mav.addObject("message", "入力内容に誤りがあります");
			mav.setViewName("view/uniformCreate");
			return mav;
		}

		Optional<Uniform> optionalUniform = uniforminfo.findById(uniform.getUniid());
		
		// 重複チェック
		if (optionalUniform.isPresent()) {
			mav.addObject("message", "入力されたユニフォームIDは既に登録済みの為、登録処理は行えませんでした。");
			mav.setViewName("view/uniformCreate");
			return mav;
		}

		// 入力されたデータをDBに保存
		uniforminfo.saveAndFlush(uniform);

		// リダイレクト先を指定
		mav.setViewName("redirect:/list");
		return mav;
	}
}
