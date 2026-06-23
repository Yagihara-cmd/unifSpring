/*
 *  会員が注文をするときの
 *  注文内容表示機能
 *  
 *  
 *  
 */

package jp.co.f1.spring.UniController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.servlet.ModelAndView;

import jp.co.f1.spring.Entity.Uniform;
import jp.co.f1.spring.Entity.Order;
import jp.co.f1.spring.Entity.User;
import jp.co.f1.spring.Repository.UniRepository;
import jp.co.f1.spring.Dao.UniDao;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.PersistenceContext;
import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.Optional;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserOrderCreate {

	//Repository
	@Autowired
	private UniRepository uniforminfo;

	@PersistenceContext
	private EntityManager entityManager;

	//Daoの自動インスタンス化
	@Autowired
	private UniDao UniDao;

	@PostConstruct
	public void init() {
		UniDao = new UniDao(entityManager);
	}

	@GetMapping(value = "/UserOrderCreate")
	public ModelAndView UserOrderCreate(HttpServletRequest request, ModelAndView mav) {

		//セッションオブジェクトの生成
		HttpSession session = request.getSession();

		//セッションからUserの値を取得・セッション切れならばエラーに遷移
		User user = (User) session.getAttribute("user");

		if (user == null) {
			//セッション切れ
			mav.addObject("errorMessage", "セッション切れの為、カート状況は確認できません。");
			mav.addObject("cmd", "logout");
			mav.addObject("next", "[ログイン画面へ]");
			mav.setViewName("view/error");
			return mav;
		}
		//カウント変数の宣言
		int i = 0;

		Order order1 = new Order();

		//セッションからOrderListを取得する
		ArrayList<Order> orderList = (ArrayList<Order>) session.getAttribute("order_list");

		if (request.getParameter("delno") != null) { //delnoの値がある場合
			// orderList全件から"delno"と同じISBNの値が見つかるまで繰り返す

			Loop: while (i < orderList.size()) {
				order1 = orderList.get(i);

				if (order1.getUniid().equals(request.getParameter("delno"))) { //isbnとdelnoが一致したとき

					break Loop;

				}
				i++;
			}
			orderList.remove(orderList.indexOf(order1));
			/**
			 * リダイレクト先の指定をする
			 * 
			 */
			mav = new ModelAndView("redirect:/userOrderCreate");
		}

		/**
		 * 
		 * 
		 * Orderについてdelnoがなかった時
		 * 
		 * 
		 */
		int total = 0;

		ArrayList<Uniform> uniform_list = new ArrayList<Uniform>();

		//カウント変数の宣言
		int j = 0;
		for (Order order : orderList) {
			Optional<Uniform> optionalUniform = uniforminfo.findByUniid(order.getUniid());
			Uniform uniform = optionalUniform.get(); //Bookオブジェクトで受け取っておくと金額計算の際に可読性が上がる
			uniform_list.add(uniform);

			//total += Integer.parseInt(uniform_list.get(j).getPrice()) * order.getQuantity();

			total += uniform_list.get(j).getPrice() * order.getQuantity();
			j++;
		}
		mav.addObject("total", total);
		mav.addObject("uniform_list", uniform_list);
		mav.addObject("orderList", orderList);

		/**
		 * 
		 * 画面のリダイレクト先の設定をする
		 */
		mav.setViewName("view/users/userOrderCreate");
		return mav;
	}
}
