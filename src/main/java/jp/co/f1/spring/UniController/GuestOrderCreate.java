package jp.co.f1.spring.UniController;

import java.util.ArrayList;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import jp.co.f1.spring.Dao.UniDao;
import jp.co.f1.spring.Entity.Order;
import jp.co.f1.spring.Entity.Uniform;
import jp.co.f1.spring.Entity.User;
import jp.co.f1.spring.Repository.UniRepository;


@Controller
public class GuestOrderCreate {

	@Autowired
	private UniRepository uniforminfo;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private UniDao uniDao;

	@Autowired
	private HttpSession session;

	public static final String LINE_SEPARATOR = System.getProperty("line.separator");

	@GetMapping("/guestOrderCreate")
	//GETデータをBookインスタンスとして受け取る
	public ModelAndView showCartForm(HttpServletRequest request, ModelAndView mav) {

		//セッションからユーザー情報取得
		User usersession = (User) session.getAttribute("usersession");

		//セッション切れの場合
		if (usersession == null) {
			//Viewに渡す変数をModelに格納
			mav.addObject("cmd", "logout");
			mav.addObject("next", "[ログイン画面へ]");
			mav.addObject("errorMessage", "セッション切れの為、カート状況は確認出来ません");
			// 画面に出力するViewを指定
			mav.setViewName("view/error");
			// ModelとView情報を返す
			return mav;
		}

		//セッションからorder情報全件取得
		@SuppressWarnings("unchecked")
		ArrayList<Order> orderList = (ArrayList<Order>) session.getAttribute("order_list");

		if (orderList == null) {
			orderList = new ArrayList<Order>();
		}

		if (request.getParameter("delno") != null) {
			//該当書籍検索
			//カウント用変数
			int i = 0;
			//order初期化
			Order order = new Order();
			boolean isFound = false; 

			//繰り返して取り出す
			while (i < orderList.size()) {
				order = orderList.get(i);
				if (order.getUniid().equals(request.getParameter("delno"))) {
					isFound = true; 
					break;
				}
				i++;
			}

			if (isFound) {
				orderList.remove(order); 
			}

			//リダイレクト先を指定
			mav = new ModelAndView("redirect:/showCart");
			return mav;
		}

		//カートの書籍リストと合計金額用変数を用意
		ArrayList<Uniform> uniform_list = new ArrayList<Uniform>();
		int total = 0;

		if (orderList != null) {
			//オーダーリストから書籍情報取り出す
			for (Order order : orderList) {

				//書籍検索
				Optional<Uniform> optionalUniform = uniforminfo.findById(order.getUniid());

				if (optionalUniform.isPresent()) {
					Uniform uniform = optionalUniform.get();

					//book_list作成
					uniform_list.add(uniform);

					//合計金額を計算
					total += uniform.getPrice();
				}
			}

		}

		//Viewに渡す変数をModelに格納
		mav.addObject("total", total);
		mav.addObject("uniform_list", uniform_list);

		//画面に出力するViewを指定
		mav.setViewName("view/guestOrderCreate");
		//ModelとView情報を返す
		return mav;
	}
}