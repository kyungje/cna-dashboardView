package local;

import local.config.kafka.KafkaProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class MyPageViewHandler {


    @Autowired
    private MyPageRepository myPageRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenOrdered_then_CREATE_1 (@Payload Ordered ordered) {
        try {
            if (ordered.isMe()) {
                // view 객체 생성
                MyPage myPage = new MyPage();
                // view 객체에 이벤트의 Value 를 set 함
                myPage.setOrderId(ordered.getId());
                myPage.setPrdId(ordered.getProductId());
                myPage.setQty(ordered.getQty());
                // view 레파지 토리에 save
                myPageRepository.save(myPage);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void whenShipped_then_UPDATE_1(@Payload Shipped shipped) {
        try {
            if (shipped.isMe()) {
                // view 객체 조회
                Optional<MyPage> myPageOptional = myPageRepository.findById(shipped.getId());
                if( myPageOptional.isPresent()) {
                    MyPage myPage = myPageOptional.get();
                    // view 객체에 이벤트의 eventDirectValue 를 set 함
                    myPage.setDeliveryId(shipped.getId());
                    myPage.setOrderId(shipped.getOrderId());
                    myPage.setStatus(shipped.getStatus());
                    // view 레파지 토리에 save
                    myPageRepository.save(myPage);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenDeliveryCanceled_then_UPDATE_2(@Payload DeliveryCanceled deliveryCanceled) {
        try {
            if (deliveryCanceled.isMe()) {
                // view 객체 조회
                Optional<MyPage> myPageOptional = myPageRepository.findById(deliveryCanceled.getId());
                if( myPageOptional.isPresent()) {
                    MyPage myPage = myPageOptional.get();
                    // view 객체에 이벤트의 eventDirectValue 를 set 함
                    myPage.setDeliveryId(deliveryCanceled.getId());
                    myPage.setStatus(deliveryCanceled.getStatus());
                    // view 레파지 토리에 save
                    myPageRepository.save(myPage);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenOrderCanceled_then_DELETE_1(@Payload OrderCanceled orderCanceled) {
        try {
            if (orderCanceled.isMe()) {
                // view 레파지 토리에 삭제 쿼리
                myPageRepository.deleteById(orderCanceled.getId());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}