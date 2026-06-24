
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
	public class TestController {

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

		@GetMapping("/test")
		public ModelAndView loginForm(ModelAndView mav, HttpServletRequest request) {

			
			// 画面に出力するViewを指定
			mav.setViewName("view/Test");
			// ModelとView情報を返す
			return mav;
		}

		
	
}
