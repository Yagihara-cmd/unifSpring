package jp.co.f1.spring.UniController;

import java.util.Optional;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import jp.co.f1.spring.Dao.UserDao;
import jp.co.f1.spring.Entity.User;
import jp.co.f1.spring.Repository.UserRepository;

@Controller
public class AdminCreate {

	@Autowired
	private UserRepository userinfo;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private UserDao userDao;

	@PostConstruct
	public void init() {
		userDao = new UserDao(entityManager);
	}

	@Autowired
	private HttpSession session;

	// 改行コード
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");

	/*
	 *  /adminCreate  GET
	 */
	/*@GetMapping("/adminCreate")
	public ModelAndView adminCreate(@ModelAttribute User user, ModelAndView mav) {
	
		// Viewに渡す変数をModelに格納
		mav.addObject("user", user);
	
		// 画面に出力するViewを指定
		mav.setViewName("view/login");
	
		// ModelとView情報を返す
		return mav;
	}*/

	/*
	 *  /adminCreate  POST
	 */
	@PostMapping("/adminCreate")
	private ModelAndView adminCreatePost(@ModelAttribute User user,
			BindingResult result, ModelAndView mav) {

		// ユーザーを検索
		Optional<User> optionalUser = userinfo.findById(user.getUserid());

		//---エラー処理---
		// 入力エラーがある場合
		if (result.hasErrors()) {
			// エラーメッセージ
			mav.addObject("message", "入力内容に誤りがあります");

			// 画面に出力するViewを指定
			mav.setViewName("view/adminCreate");
			// ModelとView情報を返す
			return mav;
		}
		// 重複チェック
		if (optionalUser.isPresent()) {
			// エラーメッセージ
			mav.addObject("message", "入力IDは既に登録済みの為、管理者登録は行えませんでした。");

			// 画面に出力するViewを指定
			mav.setViewName("view/adminCreate");
			// ModelとView情報を返す
			return mav;
		}

		// 入力されたデータをDBに保存
		userinfo.saveAndFlush(user);

		// リダイレクト先を指定
		mav = new ModelAndView("redirect:/login");

		// ModelとView情報を返す
		return mav;
	}
}
