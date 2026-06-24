/*
 * 
 * 管理者ユニフォーム管理画面
 * 
 *  担当:八木原
 *  最終更新:2026/06/24-AM10
 * 
 * 
 */

package jp.co.f1.spring.UniController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.servlet.ModelAndView;

import jp.co.f1.spring.Entity.Uniform;
import jp.co.f1.spring.Entity.User;
import jp.co.f1.spring.Repository.UniRepository;
import jp.co.f1.spring.Dao.UniDao;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.PersistenceContext;
import jakarta.persistence.EntityManager;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class AdminUniList {

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

	@GetMapping("adminUniformList")
	public ModelAndView adminUniList(HttpServletRequest request, ModelAndView mav) {
		//セッションオブジェクトの生成
		HttpSession session = request.getSession();

		//セッションからUserの値を取得
		User user = (User) session.getAttribute("user");

		//userをセッションに登録
		session.setAttribute("user", user);

		//uniforminfoテーブルから全件取得
		Iterable<Uniform> uniform_list = uniforminfo.findAll();

		//Viewに渡す変数をModelに格納
		mav.addObject("uniform_list", uniform_list);

		//画面出力するviewを指定
		mav.setViewName("view/admin/adminUniformList");

		//ModelとVIewを返す
		return mav;

	}

}
