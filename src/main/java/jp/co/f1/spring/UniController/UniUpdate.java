/*
 * 
 * 管理者ユニフォーム変更画面
 * 
 *  担当:友久
 *  最終更新:2026/06/24-AM11
 * 
 * 
 */

package jp.co.f1.spring.UniController;

import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import jp.co.f1.spring.Entity.Uniform;
import jp.co.f1.spring.Entity.User;
import jp.co.f1.spring.Dao.UniDao;
import jp.co.f1.spring.Repository.UniRepository;

public class UniUpdate {
	
	// Repositoryインターフェースを自動インスタンス化
		@Autowired
		private UniRepository uniforminfo;

		@PersistenceContext
		private EntityManager entityManager;

		@Autowired
		private UniDao UniDao;

//		@PostConstruct
//		public void init() {
//			bookDao = new BookDao(entityManager);
//		}

		@Autowired
		private HttpSession session;		
	/*
	 * 「/update」へアクセスがあった場合
	 */
	@GetMapping("/uniformUpdate")
	public ModelAndView updateForm(@ModelAttribute Uniform uni, HttpServletRequest request, ModelAndView mav) {

		//書籍検索
		Optional<Uniform> optionalUni = uniforminfo.findByUniid(request.getParameter("uniid"));

		//---エラー処理 ---//
		// 書籍が存在しない場合
		if (!optionalUni.isPresent()) {
			// エラーメッセージ
			mav.addObject("errorMessage", "変更対象の商品が存在しない為、変更画面は表示出来ませんでした。");
			mav.addObject("cmd", "list");
			mav.addObject("next", "[一覧表示へ戻る]");
			// 画面に出力するViewを指定
			mav.setViewName("view/error");
			// ModelとView情報を返す
			return mav;
		}

		// 書籍が存在する場合、old_uniとしてModelに追加
		Uniform old_uni = optionalUni.get();
		mav.addObject("old_uni", old_uni);
		mav.addObject("uni", uni);

		// 画面に出力するViewを指定
		mav.setViewName("view/admin/uniformUpdate");
		// ModelとView情報を返す
		return mav;
	}

	/*
	 * 「/update」へPOST送信された場合
	 */
	@PostMapping("/uniformUpdate")
	public ModelAndView updatePost(@ModelAttribute Uniform uni,BindingResult result, HttpServletRequest request, ModelAndView mav) {

		//セッションからユーザー情報取得
		User user = (User) session.getAttribute("user");

		// 書籍検索
		Optional<Uniform> optionalUni = uniforminfo.findByUniid(request.getParameter("uniid"));

		// 書籍が存在する場合、old_bookとしてModelに追加
		Uniform old_uni = optionalUni.get(); // Optionalから値を取得

		//---エラー処理---
		//セッション切れの場合
		if (user == null) {
			//エラーメッセージ
			mav.addObject("errorMessage", "セッション切れの為、更新できませんでした。");
			mav.addObject("cmd", "logout");
			mav.addObject("next", "[ログイン画面へ]");
			// 画面に出力するViewを指定
			mav.setViewName("view/error");
			// ModelとView情報を返す
			return mav;
		}
		// 書籍が存在しない場合
		if (!optionalUni.isPresent()) {
			mav.addObject("errorMessage", "変更対象の商品が存在しないため、変更処理は行えませんでした。");
			mav.addObject("cmd", "list");
			mav.addObject("next", "[一覧表示へ戻る]");
			mav.setViewName("view/error");
			return mav;
		}
		// 入力内容にエラーがある場合
		if (result.hasErrors()) {
			//エラーメッセージ
			mav.addObject("message", "入力内容に誤りがあります");
			// バリデーションエラー後は、入力内容を再表示する
			mav.addObject("old_uni", old_uni);
			mav.setViewName("view/admin/uniformUpdate");
			return mav;
		}

		// 入力されたデータをDBに保存
		uniforminfo.saveAndFlush(uni);

		// リダイレクト先を指定
		mav.setViewName("view/admin/adminUniformList");
		// ModelとView情報を返す
		return mav;
	}
}
