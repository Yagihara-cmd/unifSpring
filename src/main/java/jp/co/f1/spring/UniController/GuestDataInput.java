/*
 *  htmlから入力情報（氏名、住所、メアド）を受け取って
 *  遷移先であるゲスト購入画面に情報を渡す。
 *  
 *  遷移元のサーブレット作成待ち
 */

package jp.co.f1.spring.UniController;

import java.util.ArrayList;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import jp.co.f1.spring.Entity.Order;
import jp.co.f1.spring.Entity.Uniform;
import jp.co.f1.spring.Entity.User;
import jp.co.f1.spring.Repository.UniRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class GuestDataInput {

	@Autowired
	private UniRepository uniforminfo;

	@GetMapping("/guestDataInput")
	public ModelAndView guestDataInput(HttpServletRequest request, ModelAndView mav) {

		mav.setViewName("view/users/guestDataInput");

		return mav;

	}

	// 入力された氏名・住所・メールアドレスを受け取る
	@PostMapping("/guestDataInput")
	public ModelAndView guestDataInputPost(HttpServletRequest request, HttpSession session, ModelAndView mav) {

		User user = (User) session.getAttribute("user");

		String guestName = request.getParameter("guestName");
		String guestAddress = request.getParameter("guestAddress");
		String guestEmail = request.getParameter("guestEmail");
		
		
		//セッションからorder情報全件取得
		ArrayList<Order> order_list = (ArrayList<Order>) session.getAttribute("order_list");

		//ArrayList<Order> order_list = new ArrayList<Order>();

		//合計の金額を管理するtotalの宣言と初期化

		int total = 0;

		ArrayList<Uniform> uniform_list = new ArrayList<Uniform>();

		//Order配列の中身をそれぞれ取り出してDBに登録
		for (Order order : order_list) {
			order.setUserid(user.getUserid());

			

			Optional<Uniform> uniformL = uniforminfo.findByUniid(order.getUniid());

			Uniform uniform = uniformL.get();

			total += uniform.getPrice() * order.getQuantity();

			uniform_list.add(uniform);

		}
		mav.addObject("total", total);
		mav.addObject("order_list", order_list);
		mav.addObject("uniform_list", uniform_list);
		session.setAttribute("guestName", guestName);
		session.setAttribute("guestAddress", guestAddress);
		session.setAttribute("guestEmail", guestEmail);

		mav.setViewName("view/users/guestPurchaseConfirm");

		return mav;
	}

}