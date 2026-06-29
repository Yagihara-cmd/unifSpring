package jp.co.f1.spring.UniController;

import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import jp.co.f1.spring.Entity.Order;
import jp.co.f1.spring.Repository.OrderRepository;

@Controller
public class OrderDetail {

   @Autowired
   private OrderRepository orderRepository;

   // 「/orderDetail」へアクセスがあった場合
   @GetMapping("/orderDetail")
   public ModelAndView orderDetail(HttpServletRequest request, ModelAndView mav) {

       //HTML側からもらった注文Noを取得
       String orderno = request.getParameter("orderno");
       int orderstr = Integer.parseInt(orderno);

       //注文Noをもとに注文情報を検索
       Optional<Order> optionalOrder = orderRepository.findByOrderno(orderstr);
       
       //注文情報が存在しない場合
       if (!(optionalOrder.isPresent())) {

           mav.addObject("errorMessage", "表示対象の注文情報が存在しない為、詳細情報は表示出来ませんでした。");
           mav.addObject("cmd", "orderList");
           mav.addObject("next", "[受注管理一覧へ戻る]");

           //画面に出力するViewを指定
           mav.setViewName("view/error");

           return mav;
       }

       //Viewに渡す変数をModelに格納
       mav.addObject("order", optionalOrder.get());

       //画面に出力するViewを指定
       mav.setViewName("view/admin/orderDetail");

       return mav;
   }
}