/*
 * 
 * 管理者ユニフォーム管理画面
 * 
 *  担当:田岡
 *  最終更新:2026/06/24-AM10
 * 
 * 
 */
package jp.co.f1.spring.UniController;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.Date;
import java.util.Optional;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import jp.co.f1.spring.Dao.OrderDao;
import jp.co.f1.spring.Entity.Order;
import jp.co.f1.spring.Entity.Uniform;
import jp.co.f1.spring.Entity.User;
import jp.co.f1.spring.Repository.OrderRepository;
import jp.co.f1.spring.Repository.UniRepository;
import jp.co.f1.spring.Repository.UserRepository; 

@Controller
public class OrderManagementList {

    // Repository
    @Autowired
    private OrderRepository orderinfo;

    
    @Autowired
    private UniRepository uniforminfo;
    
    @Autowired
    private UserRepository userinfo;

    @PersistenceContext
    private EntityManager entityManager;

    // Daoの自動インスタンス化
    @Autowired
    private OrderDao OrderDao;

    @PostConstruct
    public void init() {
        OrderDao = new OrderDao(entityManager);
    }

    @GetMapping("/orderManagementList")
    public ModelAndView adminUniList(HttpServletRequest request, ModelAndView mav) {
        // セッションオブジェクトの生成
        HttpSession session = request.getSession();

        // セッションからUserの値を取得
        User user = (User) session.getAttribute("user");

        session.setAttribute("user", user);

        Iterable<Order> order_list = orderinfo.findAll();

 
        int thisMonthTotal = 0;
        int lastMonthTotal = 0;

        // 現在の日時をベースに、今月・先月の期間を設定
        YearMonth thisMonth = YearMonth.now();
        LocalDateTime startOfThisMonth = thisMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfThisMonth = thisMonth.atEndOfMonth().atTime(LocalTime.MAX);

        YearMonth lastMonth = thisMonth.minusMonths(1);
        LocalDateTime startOfLastMonth = lastMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfLastMonth = lastMonth.atEndOfMonth().atTime(LocalTime.MAX);

        // 全件の注文データから期間を判定して金額を合算
        for (Order order : order_list) {
            Date orderDate = order.getDate(); 
            
            if (orderDate != null) {
                java.time.ZoneId defaultZoneId = java.time.ZoneId.systemDefault();
                LocalDateTime localOrderDate = orderDate.toInstant()
                        .atZone(defaultZoneId)
                        .toLocalDateTime();

                Optional<Uniform> optionalUniform = uniforminfo.findById(order.getUniid());
                if (optionalUniform.isPresent()) {
                    int price = optionalUniform.get().getPrice();

                    if (!localOrderDate.isBefore(startOfThisMonth) && !localOrderDate.isAfter(endOfThisMonth)) {
                        thisMonthTotal += price;
                    }
                    else if (!localOrderDate.isBefore(startOfLastMonth) && !localOrderDate.isAfter(endOfLastMonth)) {
                        lastMonthTotal += price;
                    }
                }
            }
        }

     // 1. 注文一覧、今月・先月の売上合計を渡す
        mav.addObject("order_list", order_list); 
        mav.addObject("monthTotal", thisMonthTotal); 
        mav.addObject("lastMonthTotal", lastMonthTotal); 

        // 2. ★ユニフォーム情報を全件取得して画面に渡す（これで HTML のリストや .size() も正常に動きます）
        Iterable<Uniform> uniform_list = uniforminfo.findAll();
        mav.addObject("uniform_list", uniform_list);

        
        
        // 3. ★ユーザー情報（会員一覧）を全件取得して画面に渡す
        Iterable<User> user_list = userinfo.findAll();
        mav.addObject("user_list", user_list);

        // 画面出力するviewを指定
        mav.setViewName("view/admin/orderManagementList");

        // ModelとViewを返す
        return mav;
    }
}