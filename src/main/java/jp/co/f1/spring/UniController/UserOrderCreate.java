/*
 *  会員が注文をするときの
 *  注文内容表示機能
 *  
 *  担当:八木原
 *  
 *  最終編集: 20260624
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

	@PostConstruct
	public void init() {
		UniDao = new UniDao(entityManager);
	}

	@GetMapping(value = "/UserOrderCreate")
	public ModelAndView UserOrderCreate(HttpServletRequest request, ModelAndView mav) {

		/**
		 * 押下された商品を「orderlist」に格納して
		 * リスト表示に続くための記述
		 * 
		 */
		//セッションオブジェクトの生成
		HttpSession session = request.getSession();

		//セッションからUserの値を取得・セッション切れならばエラーに遷移
		User user = (User) session.getAttribute("user");

		//userのせっしょんがある場合
		if (user == null) {
			//セッション切れ
			mav.addObject("errorMessage", "セッション切れの為、カート状況は確認できません。");
			mav.addObject("cmd", "logout");
			mav.addObject("next", "[ログイン画面へ]");
			mav.setViewName("view/error");
			return mav;
		}

		//uniidのパラメータを取得し詳細を取得,オブジェクトに格納
		Optional<Uniform> optionalUniform = uniforminfo.findByUniid(request.getParameter("uniid"));
		

		//order情報をOrderに格納するためのオブジェクト宣言
		Order order = new Order();
		//uniid
		order.setUniid(request.getParameter("uniid"));
		//userid	
		order.setUserid(request.getParameter("userid"));

		//数量を格納
		order.setQuantity(Integer.parseInt(request.getParameter("quantity")));

		//時刻を取得
		Date date = new Date();
		order.setDate(date);

		//OrderList配列の宣言
		ArrayList<Order> order_list = new ArrayList<Order>();

		/**
		 * セッションから取得したorderlistが空であれば1件目として保存する
		 * 空でなければorderListに追加する。
		 * 後のセッション削除の際に変数内にデータが残ってしまうのを防ぐために、
		 * メソッド内でorderlistを定義する
		 * 
		 */
		if ((ArrayList<Order>) order_list != null) {
			order_list = (ArrayList<Order>) (order_list);
		}
		order_list.add(order);

		/**
		 * 以下カートを表示する記述
		 */

		//セッションからOrderListを取得する
		ArrayList<Order> orderList = (ArrayList<Order>) (order_list);

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

		for (Order order1 : orderList) {
			Optional<Uniform> optionalUniform1 = uniforminfo.findByUniid(order.getUniid());
			Uniform uniform = optionalUniform.get(); //uniformオブジェクトで受け取っておくと金額計算の際に可読性が上がる
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
		 * 
		 * 
		 */
		mav.setViewName("view/users/userOrderCreate");
		return mav;
	}
}
