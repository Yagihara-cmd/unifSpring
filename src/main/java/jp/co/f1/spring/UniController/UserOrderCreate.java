/*
 *  会員が注文をするときの
 *  注文内容表示機能
 *  
 *  担当:八木原
 *  
 *  最終編集: 20260625
 *  
 *  セッションを使用して複数カートに入れられない。
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
import jp.co.f1.spring.Entity.Uniform;
import jp.co.f1.spring.Dao.UniDao;
import jp.co.f1.spring.Dao.OrderDao;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.PersistenceContext;
import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.Date;
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

	// Repositoryインターフェースを自動インスタンス化
	@Autowired
	private UniRepository Orderforminfo;

	@PostConstruct
	public void init() {
		UniDao = new UniDao(entityManager);
	}

	@GetMapping(value = "/UserOrderCreate")
	public ModelAndView UserOrderCreate(HttpServletRequest request, ModelAndView mav) {

		/**
		 * 
		 * 押下された商品を「orderlist」に格納して
		 * リスト表示に続くための記述
		 * 
		 */
		//セッションオブジェクトの生成
		HttpSession session = request.getSession();

		//セッションからUserの値を取得・セッション切れならばエラーに遷移
		User user = (User) session.getAttribute("user");

		//セッションからOrderListを取得する
		ArrayList<Order> order_list = new ArrayList<Order>();

		if ((ArrayList<Order>) session.getAttribute("order_list") != null) {
			order_list = (ArrayList<Order>) session.getAttribute("order_list");
		}

		//userのセッションがある場合
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

		Order order2 = new Order();

		if (request.getParameter("delno") != null) { //delnoの値がある場合
			// orderList全件から"delno"と同じISBNの値が見つかるまで繰り返す

			Loop: while (i < order_list.size()) {
				order2 = order_list.get(i);

				if (order2.getUniid().equals(request.getParameter("delno"))) { //isbnとdelnoが一致したとき

					break Loop;

				}
				i++;
			}
			order_list.remove(order_list.indexOf(order2));
			mav = new ModelAndView("redirect:/UserOrderCreate");
			return mav;
		}

		if (request.getParameter("uniid") != null) {
			//uniidのパラメータを取得し詳細を取得,オブジェクトに格納
			Optional<Uniform> uniform = uniforminfo.findByUniid(request.getParameter("uniid"));

			//order情報をOrderに格納するためのオブジェクト宣言
			Order order = new Order();

			//uniid
			order.setUniid(request.getParameter("uniid"));

			//userid	
			order.setUserid(user.getUserid());

			//数量を格納
			//order.setQuantity(Integer.parseInt(request.getParameter("quantity")));

			//数量を1に固定
			order.setQuantity(1);

			//時刻を取得
			Date date = new Date();
			order.setDate(date);

			//発送状況、入金状況の設定
			order.setShippingstatus("0");

			order.setPaymentstatus("0");

			//OrderListにorderのオブジェクトを追加する
			order_list.add(order);

			int total = 0;
			int j = 0;
			ArrayList<Uniform> uniform_list = new ArrayList<Uniform>();

			for (Order order1 : order_list) {
				Optional<Uniform> optionalUniform = uniforminfo.findByUniid(order.getUniid());
				Uniform uniform1 = optionalUniform.get(); //uniformオブジェクトで受け取っておくと金額計算の際に可読性が上がる
				uniform_list.add(uniform1);
				total += uniform_list.get(j).getPrice() * order.getQuantity();
				j++;
			}
			session.setAttribute("order_list", order_list);

			mav.addObject("total", total);
		}


		//mav.addObject("uniform_list", uniform_list);
		mav.addObject("order_list", order_list);

		/**
		 * 
		 * 画面のリダイレクト先の設定をする
		 * 
		 * 
		 */
		mav.setViewName("view/users/userOrderCreate");

		return mav;
	}
}