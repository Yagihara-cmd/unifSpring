/*
 * 
 * 会員購入確認画面
 * 
 *  担当:友久
 *  最終更新:2026/06/24-AM11
 * 
 * 
 */

package jp.co.f1.spring.UniController;

import java.util.ArrayList;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import jp.co.f1.spring.Entity.Uniform;
import jp.co.f1.spring.Entity.Order;
import jp.co.f1.spring.Entity.User;
import jp.co.f1.spring.Repository.UniRepository;
import jp.co.f1.spring.Repository.OrderRepository;

@Controller
public class UserPurchaseConfirm {
	
	// Repositoryインターフェースを自動インスタンス化
		@Autowired
		private UniRepository uniforminfo;

		@Autowired
		private OrderRepository orderinfo;

		@PersistenceContext
		private EntityManager entityManager;

		@Autowired
		private HttpSession session;

		@Autowired
		private MailSender mailSender;

		public static final String LINE_SEPARATOR = System.getProperty("line.separator");
		
	/*
	 * 「buyConfirm」へGET送信された場合
	 */
	@GetMapping("/userPurchaseConfirm")
	//GETデータをBookインスタンスとして受け取る
	public ModelAndView buyConfirmForm(HttpServletRequest request, ModelAndView mav) {

		//セッションからユーザー情報取得
		User user = (User) session.getAttribute("user");

		//Viewに渡す用のbook_listを初期化
		ArrayList<Uniform> uni_list = new ArrayList<Uniform>();

		//セッションからorder情報全件取得
		ArrayList<Order> order_list = (ArrayList<Order>) session.getAttribute("order_list");

		//---エラー処理---
		//セッション切れの場合
		if (user == null) {
			//Viewに渡す変数をModelに格納
			mav.addObject("errorMessage", "セッション切れの為、購入は出来ません。");
			mav.addObject("cmd", "/login");
			mav.addObject("next", "[ログイン画面へ]");
			// 画面に出力するViewを指定
			mav.setViewName("view/error");
			// ModelとView情報を返す
			return mav;
		}
		//カートの中身がない場合
		if (order_list.isEmpty()) {
			//エラーメッセージ
			mav.addObject("errorMessage", "カートの中に何も無かったので購入は出来ません。");
			mav.addObject("cmd", "/userUniformList");
			mav.addObject("next", "[一覧へ戻る]");
			//画面に出力するViewを指定
			mav.setViewName("view/error");
			//ModelとView情報を返す
			return mav;
		}
		//合計計算
		//合計金額用変数の初期化
		int total = 0;

		//オーダーリストから1件ずつ取り出す
		for (Order order : order_list) {
			//入力されたデータをDB（orderinfo)に保存
			orderinfo.saveAndFlush(order);

			//該当書籍取り出す
			Optional<Uniform> uniList = uniforminfo.findByUniid(order.getUniid());
			Uniform Uniform = uniList.get();

			//合計を計算する
			total += Uniform.getPrice();

			//book_list作成
			uni_list.add(Uniform);
		}

		//Viewに渡す変数をModelに格納
		mav.addObject("total", total);
		mav.addObject("uni_list", uni_list);

		//メール送信
		//pom.xmlにspring-boot-starter-mailを挿入
		try {

			SimpleMailMessage msg = new SimpleMailMessage();
			msg.setFrom("test.sender@kanda-it-school-system.com");
			msg.setTo(user.getEmail());

			String insertMessage = user.getUserid() + "様" + LINE_SEPARATOR + LINE_SEPARATOR;
			insertMessage += "ユニフォームのご購入ありがとうございます。" + LINE_SEPARATOR;
			insertMessage += "以下内容でご注文を受け付けましたので、ご連絡致します。" + LINE_SEPARATOR + LINE_SEPARATOR;
			for (int j = 0; j < uni_list.size(); j++) {
				Uniform uni = uni_list.get(j);
				insertMessage += uni.getUniid() + "\t" + uni.getUniname() + "\t" + uni.getPrice() + "円" + ""
						+ LINE_SEPARATOR;
			}
			insertMessage += "合計" + total + "円" + LINE_SEPARATOR + LINE_SEPARATOR;
			insertMessage += "ご利用ありがとうございました。" + LINE_SEPARATOR;

			msg.setSubject("ユニフォーム購入情報");// Set Title
			msg.setText(insertMessage);// Set Message
			mailSender.send(msg);

		} catch (MailSendException e) {
			//エラーメッセージ
			mav.addObject("errorMessage", "メールの送信ができませんでした。");
			mav.addObject("cmd", "login");
			mav.addObject("next", "[メニュー画面へ]");
			//画面に出力するViewを指定
			mav.setViewName("view/error");
			//ModelとView情報を返す
			return mav;
		}

		//セッション情報削除
		session.removeAttribute("order_list");

		//画面に出力するViewを指定
		mav.setViewName("view/users/userPurchaseConfirm");
		//ModelとView情報を返す
		return mav;
	}
}