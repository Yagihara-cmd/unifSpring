/*
 * 
 *  ユーザー会員登録
 * 
 *  担当:塚田
 *  最終更新:2026/06/24-10:30
 * 
 * 
 */

package jp.co.f1.spring.UniController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import jakarta.persistence.PersistenceContext;
import jakarta.persistence.EntityManager;
import jakarta.annotation.PostConstruct;

import jp.co.f1.spring.Repository.UserRepository;
import jp.co.f1.spring.Dao.UserDao;
import jp.co.f1.spring.Entity.User;

import java.util.Optional;

@Controller
public class UserCreate {

	// EntityManager自動インスタンス化
	@PersistenceContext
	private EntityManager entityManager;

	// DAO自動インスタンス化
	@Autowired
	private UserDao userDao;

	@PostConstruct
	public void init() {
		userDao = new UserDao(entityManager);
	}

	// Repositoryインターフェースを自動インスタンス化
	@Autowired
	private UserRepository userinfo;
	
	//「/userCreate」へアクセスがあった場合
	@GetMapping("/userCreate")
	
	public ModelAndView userCreate(@ModelAttribute User user, ModelAndView mav) {

		// Viewに渡す変数をModelに格納
		mav.addObject("user", user);

		// 画面に出力するViewを指定
		mav.setViewName("view/users/userCreate");

		return mav;
	}

	//「/userCreate」へPOST送信された場合
	@PostMapping(value = "/userCreate")
	
	// POSTデータをUserインスタンスとして受け取る
	public ModelAndView userCreatePost(@ModelAttribute @Validated User user, BindingResult result, ModelAndView mav) {

		//ユーザーの検索
		Optional<User> optionalUser = userinfo.findByUseridAndPassword(user.getUserid(),user.getPassword());
		
		// 入力エラーがある場合
		if (result.hasErrors()) {
			
			// エラーメッセージ
			mav.addObject("message", "入力内容に誤りがあります");

			// 画面に出力するViewを指定
			mav.setViewName("view/users/error");

			// ModelとView情報を渡す
			return mav;
		}
		
		//同じIDまたはパスワードの情報がないか確認
		if (optionalUser.isPresent()) {
			
			mav.addObject("message", "入力IDまたはパスワードは既に登録済みの為、会員登録処理は行えませんでした。");
			
			// 画面に出力するViewを指定
			mav.setViewName("view/users/error");
			
			return mav;			 
		}

		// 入力されたデータをDBに保存
		userinfo.saveAndFlush(user);

		// リダイレクト先を指定
		mav = new ModelAndView("redirect:/userUniformList");

		return mav;
	}

}
