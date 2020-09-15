package myProject_LSP;

import jdk.nashorn.internal.runtime.options.Option;
import myProject_LSP.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PolicyHandler{
    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }

    @Autowired
    OrderRepository orderRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverCookQtyChecked_CookCancelUpdate(@Payload CookQtyChecked cookQtyChecked){
        if(cookQtyChecked.isMe()){
            Optional<Order> orderOptional = orderRepository.findById(cookQtyChecked.getOrderId());
            Order order = orderOptional.get();
            //System.out.println("##### listener CookCancelUpdate : " + cookQtyChecked.toJson());
            //order.setId(cookQtyChecked.getOrderId());
            order.setStatus("ORDER : QTY OVER CANCELED");
            orderRepository.save(order);
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverCouponSended_CouponInfoUpdate(@Payload CouponSended couponSended){

        if(couponSended.isMe()){
            System.out.println("##### listener CouponInfoUpdate : " + couponSended.toJson());
            Optional<Order> orderOptional = orderRepository.findById(couponSended.getOrderId());
            Order order = orderOptional.get();
            if("COUPON : COUPON SENDED".equals(couponSended.getStatus())){
                order.setCouponStatus("ORDER : COUPON SENDED SUCCESS");
            }
            orderRepository.save(order);
        }
    }

}