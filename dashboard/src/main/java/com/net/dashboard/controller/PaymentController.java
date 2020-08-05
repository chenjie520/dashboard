package com.net.dashboard.controller;

import com.net.dashboard.dao.IDiscountDao;
import com.net.dashboard.dao.IOrderDao;
import com.net.dashboard.pojo.Discount;
import com.net.dashboard.pojo.Order;
import com.net.dashboard.pojo.Response;
import com.net.dashboard.service.ProxyService;
import com.net.dashboard.service.StripeService;
import com.stripe.model.Coupon;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class PaymentController {

    @Autowired
    private IOrderDao orderDao;
    @Autowired
    private ProxyService proxyService;
    @Autowired
    private IDiscountDao discountDao;
    @Value("${stripe.keys.public}")
    private String API_PUBLIC_KEY;

    private StripeService stripeService;

    public PaymentController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @GetMapping("/")
    public String homepage() {
        return "homepage";
    }

    @GetMapping("/subscription")
    public String subscriptionPage(Model model) {
        model.addAttribute("stripePublicKey", API_PUBLIC_KEY);
        return "subscription";
    }

    @GetMapping("/charge")
    public String chargePage(Model model) {
        model.addAttribute("stripePublicKey", API_PUBLIC_KEY);
        return "charge";
    }

    @RequestMapping("/getKey")
    public @ResponseBody Response getKey(){
        return new Response(true,API_PUBLIC_KEY);
    }
    /*========== REST APIs for Handling Payments ===================*/

    @PostMapping("/create-subscription")
    public @ResponseBody
    Response createSubscription(String email, String token, String plan, String coupon) {
        //validate data
        if (token == null || plan.isEmpty()) {
            return new Response(false, "Stripe payment token is missing. Please, try again later.");
        }

        //create customer first
        String customerId = stripeService.createCustomer(email, token);

        if (customerId == null) {
            return new Response(false, "An error occurred while trying to create a customer.");
        }

        //create subscription
        String subscriptionId = stripeService.createSubscription(customerId, plan, coupon);
        if (subscriptionId == null) {
            return new Response(false, "An error occurred while trying to create a subscription.");
        }

        // Ideally you should store customerId and subscriptionId along with customer object here.
        // These values are required to update or cancel the subscription at later stage.

        return new Response(true, "Success! Your subscription id is " + subscriptionId);
    }

    @PostMapping("/cancel-subscription")
    public @ResponseBody
    Response cancelSubscription(String subscriptionId) {
        boolean status = stripeService.cancelSubscription(subscriptionId);
        if (!status) {
            return new Response(false, "Failed to cancel the subscription. Please, try later.");
        }
        return new Response(true, "Subscription cancelled successfully.");
    }

    @PostMapping("/coupon-validator")
    public @ResponseBody
    Response couponValidator(String code) {
        Coupon coupon = stripeService.retrieveCoupon(code);
        if (coupon != null && coupon.getValid()) {
            String details = (coupon.getPercentOff() == null ? "$" + (coupon.getAmountOff() / 100) : coupon.getPercentOff() + "%") +
                    " OFF " + coupon.getDuration();
            return new Response(true, details);
        } else {
            return new Response(false, "This coupon code is not available. This may be because it has expired or has " +
                    "already been applied to your account.");
        }
    }

    /**
     *
     * @param buyType  购买类型(G)整数（3的倍数）
     * @param email
     * @param token
     * @param discount	折扣码
     * @param dcId
     * @param buyPrice 购买价格 单位美元
     * @return
     */
    @PostMapping("/create-charge")
    public @ResponseBody
    Response createCharge(@Param("buyType")String buyType,@Param("email") String email, @Param("token") String token,@Param("discount")String discount,@Param("dcId")String dcId,@Param("buyPrice")String buyPrice) {
        //discount
        //validate data
        if (token == null) {
            return new Response(false, "Stripe payment token is missing. Please, try again later.");
        }

        if((!"".equals(discount))&&discount!=null) {
            Discount discount1 = new Discount();
            discount1.setNumber(discount);
            discount1.setDcId(dcId);
            List<Discount> list = discountDao.selectDiscountByNumber(discount1);
            if (list == null || list.size() == 0) {
                return new Response(false, "this discount can not use");
            }
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getDcId() != null || (!list.get(i).getDcId().equals(""))) {
                    return new Response(false, "this discount can not use");
                }
            }
            discountDao.updateDiscount(discount1);
        }
        //create charge将美元转换成美分
        String chargeId = stripeService.createCharge(email, token,new Integer((int) (Float.parseFloat(buyPrice)*100)) );
        if (chargeId == null) {
            return new Response(false, "An error occurred while trying to create a charge.");
        }

        // You may want to store charge id along with order information
        Order order=new Order();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);
        order.setDueDate(cal.getTime());
        order.setDiscount(discount);
        order.setBuyPrice(buyPrice);
        order.setBuyDate(new Date());
        order.setDcId(dcId);
        order.setBuyType(buyType);
        orderDao.insertOrder(order);
        proxyService.buyProxies(dcId,new Integer(buyType));
        return new Response(true, "Success! Your charge id is " + chargeId);
    }
}
