package jp.co.f1.spring.UniController;

import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import jp.co.f1.spring.Dao.UserDao;
import jp.co.f1.spring.Entity.User;
import jp.co.f1.spring.Repository.UserRepository;

@Controller
public class UserMyPage {

	@Autowired
	private UserRepository userinfo;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private HttpSession session;
	
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");

	@GetMapping("/userMyPage")
	public ModelAndView userMyPageForm(@ModelAttribute User user, HttpServletRequest request, ModelAndView mav) {

		// セッションからログインユーザーの情報を取得
		User usersession = (User) session.getAttribute("usersession");
		
		// セッションが切れている、またはログインしていない場合はエラー
		if (usersession == null) {
			mav.addObject("errorMessage", "セッション切れの為、マイページを表示出来ませんでした。");
			mav.addObject("cmd", "logout");
			mav.addObject("next", "[ログイン画面へ]");
			mav.setViewName("view/error");
			return mav;
		}

		// セッションにある userid を使って最新のユーザー情報をDBから取得
	    Optional<User> optionalUser = userinfo.findById(usersession.getUserid());

	    //--- エラー処理 ---//
	    if (!optionalUser.isPresent()) {
	        mav.addObject("errorMessage", "更新対象のユーザーが存在しない為、変更画面は表示出来ませんでした。");
	        mav.addObject("cmd", "list");
	        mav.addObject("next", "[一覧表示へ戻る]");
	        mav.setViewName("view/error");
	        return mav;
	    }

	    // 存在するユーザー情報を取得してModelに設定
	    User dbUser = optionalUser.get();
	    mav.addObject("user", dbUser);

	    mav.setViewName("view/users/userMyPage");
	    return mav;
	}

	@PostMapping("/userMyPage")
	public ModelAndView userMyPagePost(@ModelAttribute @Validated User user,
	        BindingResult result, HttpServletRequest request, ModelAndView mav) {

	    // セッションからユーザー情報取得
	    User usersession = (User) session.getAttribute("usersession");

	    // ---エラー処理---
	    // 1. セッション切れの場合
	    if (usersession == null) {
	        mav.addObject("errorMessage", "セッション切れの為、更新できませんでした。");
	        mav.addObject("cmd", "logout");
	        mav.addObject("next", "[ログイン画面へ]");
	        mav.setViewName("view/error");
	        return mav;
	    }

	    // 2. 入力内容にエラーがある場合
	    if (result.hasErrors()) {
	        mav.addObject("message", "入力内容に誤りがあります");
	        // 入力途中のデータ（user）をそのまま保持して画面再表示
	        mav.setViewName("view/users/userMyPage");
	        return mav;
	    }

	    // 3. 更新対象のユーザーがDBに存在するかチェック
	    Optional<User> optionalUser = userinfo.findById(user.getUserid());
	    if (!optionalUser.isPresent()) {
	        mav.addObject("errorMessage", "更新対象のユーザーが存在しないため、更新処理は行えませんでした。");
	        mav.addObject("cmd", "list");
	        mav.addObject("next", "[一覧表示へ戻る]");
	        mav.setViewName("view/error");
	        return mav;
	    }

	    // 入力されたデータをDBに保存
	    userinfo.saveAndFlush(user);
	    
	    mav.setViewName("redirect:/list");
	    return mav;
	}
}