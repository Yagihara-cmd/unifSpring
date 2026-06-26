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

	    Optional<User> optionalUser = userinfo.findById(request.getParameter("userId"));

	    //---エラー処理 ---//
	    // ユーザーが存在しない場合
	    if (!optionalUser.isPresent()) {
	        // エラーメッセージ
	        mav.addObject("errorMessage", "更新対象のユーザーが存在しない為、変更画面は表示出来ませんでした。");
	        mav.addObject("cmd", "list");
	        mav.addObject("next", "[一覧表示へ戻る]");
	        // 画面に出力するViewを指定
	        mav.setViewName("view/error");
	        // ModelとView情報を返す
	        return mav;
	    }

	    // ユーザーが存在する場合、old_userとしてModelに追加
	    User old_user = optionalUser.get();
	    mav.addObject("old_user", old_user);
	    mav.addObject("user", user);

	    // 画面に出力するViewを指定
	    mav.setViewName("view/users/userMyPage");
	    // ModelとView情報を返す
	    return mav;
	}

	/*
	 * 「/userMyPage」へPOST送信された場合
	 */
	@PostMapping("/userMyPage")
	public ModelAndView userMyPagePost(@ModelAttribute @Validated User user,
	        BindingResult result, HttpServletRequest request, ModelAndView mav) {

	    // セッションからユーザー情報取得
	    User usersession = (User) session.getAttribute("usersession");

	  
	    Optional<User> optionalUser = userinfo.findById(request.getParameter("userId"));

	    // ---エラー処理---
	    // セッション切れの場合
	    if (usersession == null) {
	        // エラーメッセージ
	        mav.addObject("errorMessage", "セッション切れの為、更新できませんでした。");
	        mav.addObject("cmd", "logout");
	        mav.addObject("next", "[ログイン画面へ]");
	        // 画面に出力するViewを指定
	        mav.setViewName("view/error");
	        // ModelとView情報を返す
	        return mav;
	    }

	    // ユーザーが存在しない場合
	    if (!optionalUser.isPresent()) {
	        mav.addObject("errorMessage", "更新対象のユーザーが存在しないため、更新処理は行えませんでした。");
	        mav.addObject("cmd", "list");
	        mav.addObject("next", "[一覧表示へ戻る]");
	        mav.setViewName("view/error");
	        return mav;
	    }

	    // ユーザーが存在する場合、old_userとしてModelに追加
	    User old_user = optionalUser.get(); 

	    // 入力内容にエラーがある場合
	    if (result.hasErrors()) {
	        // エラーメッセージ
	        mav.addObject("message", "入力内容に誤りがあります");
	        // バリデーションエラー後は、入力内容を再表示する
	        mav.addObject("old_user", old_user);
	        mav.setViewName("view/users/userMyPage");
	        return mav;
	    }

	    // 入力されたデータをDBに保存
	    userinfo.saveAndFlush(user);
	    

	    // リダイレクト先を指定
	    mav.setViewName("redirect:/list");
	    // ModelとView情報を返す
	    return mav;
	}
}
