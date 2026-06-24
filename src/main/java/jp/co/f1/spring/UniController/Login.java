/**
 * ログイン画面
 * 
 * 担当:　友久
 * 
 * 最終編集：八木原　20260624-AM10
 * 
 * 
 */

package jp.co.f1.spring.UniController;

import java.util.ArrayList;
import java.util.Optional;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import jp.co.f1.spring.Entity.Uniform;
import jp.co.f1.spring.Entity.User;
import jp.co.f1.spring.Dao.UserDao;
import jp.co.f1.spring.Repository.UniRepository;
import jp.co.f1.spring.Repository.UserRepository;
import jp.co.f1.spring.Entity.Uniform;

@Controller
public class Login {

	@Autowired
	private UserRepository userinfo;

	@Autowired
	private UniRepository uniforminfo;

	//EntityManager自動インスタンス化
	@PersistenceContext
	private EntityManager entityManager;

	//DAO自動インスタンス化
	@Autowired
	private UserDao userDao;

	@PostConstruct
	public void init() {
		userDao = new UserDao(entityManager);
	}

	//セッション使用
	@Autowired
	private HttpSession session;

	@GetMapping("/login")
	public ModelAndView loginForm(ModelAndView mav, HttpServletRequest request) {

		//渡す値の初期化
		String strUser = null;
		String strPassword = null;

		// クッキーを取得
		Cookie[] cookies = request.getCookies();

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("user".equals(cookie.getName())) {
					strUser = cookie.getValue();
				} else if ("password".equals(cookie.getName())) {
					strPassword = cookie.getValue();
				}
			}
		}

		// Modelにクッキーの値を追加
		mav.addObject("strUser", strUser);
		mav.addObject("strPassword", strPassword);

		// 画面に出力するViewを指定
		mav.setViewName("view/login");
		// ModelとView情報を返す
		return mav;
	}

	/*
	 * 「login」へPOST送信された場合
	 */
	@PostMapping("/login")
	public ModelAndView loginPost(@ModelAttribute @Validated User user, BindingResult result,
			ModelAndView mav, HttpServletRequest request, HttpServletResponse response, Model model) {

		//ユーザー検索
		Optional<User> optional_user = userinfo.findByUseridAndPassword(user.getUserid(), user.getPassword());

		//Useridまたはpassが無効であった場合
		if (!(optional_user.isPresent())) {
			mav.addObject("errorMessage", "入力されたUSERIDは存在しませんでした。");
			mav.setViewName("view/login");
			return mav;

		}

		if (optional_user.isPresent()) {
			//クッキーの登録
			Cookie userCookie = new Cookie("user", user.getUserid());
			userCookie.setMaxAge(60 * 60 * 24 * 5); // 5日（秒）に設定
			response.addCookie(userCookie);

			Cookie passCookie = new Cookie("password", user.getPassword());
			passCookie.setMaxAge(60 * 60 * 24 * 5);
			response.addCookie(passCookie);

			//セッションに登録
			user = optional_user.get();
			session.setAttribute("user", user);

			if (user.getAuthority().equals("0")) {
				//mav.setViewName("view/users/userUniformList");
				mav = new ModelAndView("redirect:/userUniformList");

			}
			if (user.getAuthority().equals("1")) {
				mav = new ModelAndView("redirect:/adminUniformList");
			}

		}
		return mav;

	}
}