/*
 * 
 * 	ゲスト用ユニフォーム一覧機能
 * 
 *  担当:芦澤
 *  最終更新:2026/06/24-AM10:30
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

import jp.co.f1.spring.Dao.UniDao;
import jp.co.f1.spring.Entity.Uniform;
import jp.co.f1.spring.Repository.UniRepository;

@Controller
public class GuestUniList {

	@Autowired
	private UniRepository uniforminfo;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private UniDao uniDao;
	
	@PostConstruct
	public void init() {
		uniDao = new UniDao(entityManager);
	}
	
	@Autowired
	private HttpSession session;

	// 改行コード
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");

	
	/*
	 *   /guestUniList
	 */
	@GetMapping("/guestUniformList")
	public ModelAndView guestUniList(ModelAndView mav) {
		
		// uniforminfoテーブルから全件取得
		Iterable<Uniform> uni_list = uniforminfo.findAll();
		
		// Viewに渡す変数をModelに格納
		mav.addObject("uni_list", uni_list);
		
		// 画面に出力するViewを指定
		mav.setViewName("view/users/guestUniformList");
		
		// ModelとView情報を返す
		return mav;
	}

}
