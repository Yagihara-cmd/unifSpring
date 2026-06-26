/*
 * 
 *  管理者用入金・発送変更機能
 * 
 *  担当:芦澤
 *  最終更新:2026/06/25-17:34
 * 
 * 
 */

package jp.co.f1.spring.UniController;

import java.util.Optional;

import jakarta.annotation.PostConstruct;
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

import jp.co.f1.spring.Dao.OrderDao;
import jp.co.f1.spring.Entity.Order;
import jp.co.f1.spring.Repository.OrderRepository;

@Controller
public class OrderUpdate {

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
	 *   /orderUpdate  GET
	 */
	@GetMapping("/orderUpdate")
	public ModelAndView orderUpdate(@ModelAttribute Order order, HttpServletRequest request, ModelAndView mav) {

		String strOrderNo = request.getParameter("orderno");
	    int orderno = Integer.parseInt(strOrderNo);
		
		// 現在の状況を取得
		Optional<Order> optional_order = orderinfo.findByOrderno(orderno);

		// データが存在しない場合
		if (!optional_order.isPresent()) {
			// エラーメッセージ
			mav.addObject("errorMessage", "更新対象が存在しない為、変更画面は表示出来ませんでした。");
			mav.addObject("cmd", "maglist");
			mav.addObject("next", "[一覧表示へ戻る]");
			// 画面に出力するViewを指定
			mav.setViewName("view/error");
			// ModelとView情報を返す
			return mav;
		}
		
		// 値によって表示する文字列を変更
		String paymentstatus = "";
		if(order.getPaymentstatus().equals("0")) {
			paymentstatus = "入金待ち";
		}else {
			paymentstatus = "入金確認済";
		}
		
		String shippingstatus = "";
		if(order.getShippingstatus().equals("0")) {
			shippingstatus = "未";
		}else if(order.getShippingstatus().equals("1")) {
			shippingstatus = "発送準備中";
		}else {
			shippingstatus = "発送済";
		}
		
		Order old_order = optional_order.get();

		// 存在する場合、Modelに格納
		mav.addObject("old_order", old_order);
		mav.addObject("order", order);
		mav.addObject("pay", paymentstatus);
		mav.addObject("ship", shippingstatus);

		// 画面に出力するViewを指定
		mav.setViewName("view/admin/orderUpdate");

		// ModelとView情報を返す
		return mav;
	}

	/*
	 *   /orderUpdate  POST
	 */
	@PostMapping("/orderUpdate")
	public ModelAndView orderUpdatePost(@ModelAttribute @Validated(Order.class) Order order,
			HttpServletRequest request, BindingResult result, ModelAndView mav) {

		String strOrderNo = request.getParameter("orderno");
		int orderno = Integer.parseInt(strOrderNo);
		
		// 現在の状況を取得
		Optional<Order> optional_order = orderinfo.findByOrderno(orderno);

		// データが存在しない場合
		if (!optional_order.isPresent()) {
			// エラーメッセージ
			mav.addObject("errorMessage", "更新対象が存在しない為、変更画面は表示出来ませんでした。");
			mav.addObject("cmd", "maglist");
			mav.addObject("next", "[一覧表示へ戻る]");
			// 画面に出力するViewを指定
			mav.setViewName("view/error");
			// ModelとView情報を返す
			return mav;
		}

		// 変更前の購入情報
		Order old_order = optional_order.get();
		
		// 入力されたデータをDBに保存
		orderinfo.saveAndFlush(order);

		// リダイレクト先を指定
		mav = new ModelAndView("redirect:/adminUniformList");

		// ModelとView情報を返す
		return mav;
	}

}
