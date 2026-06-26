/*
 * 
 * 管理者ユニフォーム管理画面（セッションデータ蓄積版）
 * 
 *  担当:田岡
 *  最終更新:2026/06/25
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import jp.co.f1.spring.Dao.UniDao;
import jp.co.f1.spring.Entity.Uniform;
import jp.co.f1.spring.Repository.UniRepository;

@Controller
public class GuestOrderCreate {

	@Autowired
	private UniRepository uniforminfo;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private UniDao uniDao;

	// セッションを導入して、画面をまたいでもデータを記憶できるようにします
	@Autowired
	private HttpSession session;

	public static final String LINE_SEPARATOR = System.getProperty("line.separator");

	@SuppressWarnings("unchecked")
	@GetMapping("/guestOrderCreate")
	public ModelAndView showCartForm(HttpServletRequest request, ModelAndView mav) {

		// セッションから現在のカート内IDリストを取得する
		ArrayList<String> uniIdList = (ArrayList<String>) session.getAttribute("session_uni_ids");
		
		// セッションにまだリストがなければ、新しく作成してセッションに登録する
		if (uniIdList == null) {
			uniIdList = new ArrayList<String>();
			session.setAttribute("session_uni_ids", uniIdList);
		}

		// 一覧画面から新しく「uniid」が送られてきた場合、セッションのリストに追加する
		String singleUniId = request.getParameter("uniid");
		if (singleUniId != null && !singleUniId.isEmpty()) {
			uniIdList.add(singleUniId.trim());
		}

		String delNo = request.getParameter("delno");
		if (delNo != null) {
			uniIdList.remove(delNo);
			// データを削除した状態で、再度カート画面を表示
			mav = new ModelAndView("redirect:/guestOrderCreate");
			return mav;
		}

		//HTML側に名前を合わせてリストを準備
		ArrayList<Uniform> uni_list = new ArrayList<Uniform>();
		int total = 0;

		//セッションに保存されているID群をループさせ、DBから最新情報を取得して合計金額を計算
		for (String uniId : uniIdList) {
			Optional<Uniform> optionalUniform = uniforminfo.findById(uniId);

			if (optionalUniform.isPresent()) {
				Uniform uniform = optionalUniform.get();
				uni_list.add(uniform);
				total += uniform.getPrice();
			}
		}

		// HTMLに表示用データを渡す
		mav.addObject("total", total);
		mav.addObject("uni_list", uni_list);

		//表示するHTMLを指定
		mav.setViewName("view/users/guestOrderCreate");
		
		return mav;
	}
}