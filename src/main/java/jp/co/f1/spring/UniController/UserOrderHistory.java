/*
 * 
 *  注文履歴機能
 * 
 *  担当:芦澤
 *  最終更新:2026/06/26-PM13:30
 * 
 * 
 */

package jp.co.f1.spring.UniController;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import jp.co.f1.spring.Dao.OrderDao;
import jp.co.f1.spring.Entity.Order;
import jp.co.f1.spring.Entity.User;
import jp.co.f1.spring.Repository.OrderRepository;

@Controller
public class UserOrderHistory {

	@Autowired
	private OrderRepository orderinfo;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private OrderDao orderDao;

	@PostConstruct
	public void init() {
		orderDao = new OrderDao(entityManager);
	}

	@Autowired
	private HttpSession session;

	// 改行コード
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");

	/*
	 *    /userOrderHistory
	 */
	@GetMapping("/userOrderHistory")
	public ModelAndView userOrderHistory(ModelAndView mav) {

		User user = (User) session.getAttribute("user");
		
		
		//　セッション切れの場合のエラー回収
		if (user == null) {
			//セッション切れ
			mav.addObject("errorMessage", "セッション切れの為、注文履歴は確認できません。");
			mav.addObject("cmd", "login");
			mav.addObject("next", "[ログイン画面へ]");
			mav.setViewName("view/error");
			return mav;
		}
		
		Iterable<Order> ordered_list = orderinfo.findByUserid(user.getUserid());

		// Viewに渡す変数をModelに格納
		mav.addObject("ordered_list", ordered_list);

		// 画面に出力するViewを指定
		mav.setViewName("view/users/userOrderHistory");
		
		// ModelとView情報を返す
		return mav;

	}
}
