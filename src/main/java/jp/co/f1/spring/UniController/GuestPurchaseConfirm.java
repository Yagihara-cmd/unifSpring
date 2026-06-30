package jp.co.f1.spring.UniController;

import java.util.ArrayList;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import jp.co.f1.spring.Entity.Order;
import jp.co.f1.spring.Entity.Uniform;
import jp.co.f1.spring.Repository.OrderRepository;
import jp.co.f1.spring.Repository.UniRepository;

@Controller
public class GuestPurchaseConfirm {

	@Autowired
	private UniRepository uniforminfo;

	@Autowired
	private OrderRepository orderinfo;

	@Autowired
	private HttpSession session;

	@Autowired
	private MailSender mailSender;

	public static final String LINE_SEPARATOR = System.getProperty("line.separator");

	//「/guestPurchaseConfirm」へGET送信された場合
	@PostMapping("/guestPurchaseConfirm")
	public ModelAndView guestPurchaseConfirm(HttpServletRequest request, ModelAndView mav) {

		//ゲストのメールアドレスを画面から取得
		String guestEmail = request.getParameter("guestEmail");
		String guestName = request.getParameter("guestName");

		//ユニフォーム一覧
		ArrayList<Uniform> uni_list = new ArrayList<Uniform>();

		//セッションからカート情報を取得
		ArrayList<Order> uniIdList = (ArrayList<Order>) session.getAttribute("session_uni_ids");

		//カートの中身がない場合
		if (uniIdList == null) {
			mav.addObject("errorMessage", "カートの中に何も無かったので購入は出来ません。");
			mav.addObject("cmd", "menu");
			mav.addObject("next", "[ゲスト情報入力画面へ戻る]");
			mav.setViewName("view/users/guestDataInput");
			return mav;
		}

		//合計金額用変数の初期化
		int total = 0;

		//オーダーリストから1件ずつ取り出す
		for (Order uniId : uniIdList) {

			orderinfo.saveAndFlush(uniId);

			Optional<Uniform> optionalUniform = uniforminfo.findByUniid(uniId.getUniid());
			
			
				Uniform uniform = optionalUniform.get();
				
				
				int newStock = uniform.getStock() - uniId.getQuantity();
				
				uniform.setStock(newStock);
				
				uniforminfo.saveAndFlush(uniform);
				
				total += uniform.getPrice();
				uni_list.add(uniform);
			}

		

		//Viewに渡す変数をModelに格納
		mav.addObject("total", total);
		mav.addObject("uni_list", uni_list);

		//メール送信
		try {
			SimpleMailMessage msg = new SimpleMailMessage();

			msg.setTo(guestEmail);

			String insertMessage = guestName + "様" + LINE_SEPARATOR + LINE_SEPARATOR;
			insertMessage += "ユニフォームのご購入ありがとうございます。" + LINE_SEPARATOR;
			insertMessage += "以下内容でご注文を受け付けましたので、ご連絡いたします。"
					+ LINE_SEPARATOR + LINE_SEPARATOR;

			for (int j = 0; j < uni_list.size(); j++) {
				Uniform uni = uni_list.get(j);
				insertMessage += uni.getUniid() + "\t" + uni.getUniname() + "\t" + uni.getPrice() + "円" + ""
						+ LINE_SEPARATOR;
			}

			insertMessage += "合計" + total + "円" + LINE_SEPARATOR + LINE_SEPARATOR;
			insertMessage += "ご利用ありがとうございました。" + LINE_SEPARATOR;

			msg.setSubject("ユニフォーム購入情報");
			msg.setText(insertMessage);
			mailSender.send(msg);

		} catch (MailSendException e) {
			mav.addObject("errorMessage", "メールの送信ができませんでした。");
			mav.addObject("cmd", "menu");
			mav.addObject("next", "[ゲスト情報入力画面へ]");
			mav.setViewName("view/users/guestDataInput");
			return mav;
		}

		//セッション情報削除
		session.removeAttribute("session_uni_ids");

		//画面に出力するViewを指定
		mav.setViewName("view/users/guestPurchaseConfirm");

		return mav;
	}
}