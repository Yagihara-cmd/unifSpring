/*
 *  htmlから入力情報（氏名、住所、メアド）を受け取って
 *  遷移先であるゲスト購入画面に情報を渡す。
 *  
 *  遷移元のサーブレット作成待ち
 */

package jp.co.f1.spring.UniController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.stereotype.Controller;

@Controller
public class GuestDataInput {

	@GetMapping("/guestDataInput")
	public ModelAndView guestDataInput(HttpServletRequest request, ModelAndView mav) {

		
	
		
		mav.setViewName("view/users/guestDataInput");
		
		return mav; 
		
	}
}