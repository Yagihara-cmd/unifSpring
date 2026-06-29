package jp.co.f1.spring.UniController;

import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import jp.co.f1.spring.Entity.Uniform;
import jp.co.f1.spring.Entity.User;
import jp.co.f1.spring.Repository.UniRepository;
import jp.co.f1.spring.Repository.UserRepository;

@Controller
public class GuestUniList {

	@Autowired
	private UserRepository userinfo;

	@Autowired
	private UniRepository uniforminfo;

	@PersistenceContext
	private EntityManager entityManager;

	// 改行コード
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");

	@GetMapping("/guestUniformList")
	public ModelAndView guestUniList(ModelAndView mav, HttpServletRequest request) {

		//セッションオブジェクトの生成
		HttpSession session = request.getSession();

		User user = (User) session.getAttribute("user");

		Optional<User> optional_guest = userinfo.findByUseridAndPassword("00000", "0000");
		user = optional_guest.get();

		session.setAttribute("user", user);

		// uniforminfoテーブルから全件取得
		Iterable<Uniform> uni_list = uniforminfo.findAll();

		// Viewに渡す変数をModelに格納
		mav.addObject("uni_list", uni_list);

		// 画面に出力するViewを指定
		mav.setViewName("view/users/guestUniformList");

		// ModelとView情報を返す
		return mav;
	}

}
