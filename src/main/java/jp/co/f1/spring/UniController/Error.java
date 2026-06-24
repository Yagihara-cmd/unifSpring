/*
 * 
 * エラー管理画面
 * 
 *  担当:友久
 *  最終更新:2026/06/24-AM11
 * 
 * 
 */

package jp.co.f1.spring.UniController;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

public class Error {
	
	/**
	 * Exception発生時の処理メソッド.
	 */
	@ExceptionHandler(Exception.class)
	public ModelAndView ExceptionHandler(Exception e) {

		ModelAndView mav = new ModelAndView();

		mav.addObject("errorMessage", "エラー内容：" + e.getMessage());
		mav.addObject("cmd", "logout");
		mav.addObject("next", "[ログイン画面へ]");
		//画面に出力するViewを指定
		mav.setViewName("view/error");
		//ModelとView情報を返す
		return mav;
	}
}
