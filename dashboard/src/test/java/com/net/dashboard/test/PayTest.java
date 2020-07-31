package com.net.dashboard.test;/**
 * @Author chen_jie
 * @Date 2020/7/24 ${Time}
 * @Version 1.0
 * @Description:
 */

import com.net.dashboard.config.ContentKey;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Token;
import com.stripe.param.PaymentIntentCreateParams;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 *@Author chen_jie
 *@Version 1.0
 *@time 2020/7/24
 **/
/*@SpringBootTest
@RunWith(SpringRunner.class)
@MapperScan("com.net.dashboard.dao")*/
public class PayTest {

    @Test
    public void test1()throws Exception{
       /* Stripe.apiKey = ContentKey.STRIPE_PASSWORD;
        Map<String, Object> card = new HashMap<>();
        card.put("number", "4242424242424242");
        card.put("exp_month", 7);
        card.put("exp_year", 2021);
        card.put("cvc", "314");
        Map<String, Object> params = new HashMap<>();
        params.put("card", card);

        Token token = Token.create(params);
*/

    }

    @Test
    public  void test2() throws Exception{
       /* Stripe.apiKey=ContentKey.STRIPE_PASSWORD;
        PaymentIntentCreateParams params= PaymentIntentCreateParams.builder()
                .setAmount(1000L)
                .setCurrency("usd")
                .addPaymentMethodType("card")
                .setReceiptEmail("jenny.rosen@example.com")
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);
        System.out.println(params);*/
    }

    @Test
    public void test3() throws Exception{
       /* Map<String, Object> payParams = new HashMap<>();
        Stripe.apiKey=ContentKey.STRIPE_PASSWORD;
        payParams.put("amount", 1);
        payParams.put("currency", "usd");
        payParams.put("description", "Charge for 2322821847@qq.com");
        payParams.put("customer","tok_1H8PjVEGJegCZvCPy9tDMa8G");
        Charge charge = Charge.create(payParams);*/
    }
    @Test
    public void test4(){
      /*  Map<String, Object> customerParams = new HashMap<String, Object>();
        Stripe.apiKey=ContentKey.STRIPE_PASSWORD;
        customerParams.put("description", "Customer for chao");
        customerParams.put("source", "tok_1H8PjVEGJegCZvCPy9tDMa8G");

        Customer c = null;
        try {
            c = Customer.create(customerParams);
            System.out.println(c);
        } catch (StripeException e) {
            e.printStackTrace();
        }*/
    }
}
