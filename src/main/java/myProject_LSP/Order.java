package myProject_LSP;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;

@Entity
@Table(name="Order_table")
public class Order {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Integer restaurantId;
    private Integer restaurantMenuId;
    private Integer customerId;
    private Integer qty;
    private Long modifiedDate;
    private String status;
    //수정
    private String couponStatus;

    @PrePersist
    public void onPrePersist(){

        if(!"ORDER : COOK CANCELED".equals(this.getStatus())){


            this.setStatus("ORDER : ORDERED");
        }
        this.setModifiedDate(System.currentTimeMillis());
    }

    @PostPersist
    public void onPostPersist(){
        Ordered ordered = new Ordered();
        BeanUtils.copyProperties(this, ordered);

        System.out.println(ordered.getStatus()+ "#######################33");
        if(!"ORDER : COOK CANCELED".equals(ordered.getStatus())){
            ordered.publishAfterCommit();

            /*수정*/
            myProject_LSP.external.Coupon coupon = new myProject_LSP.external.Coupon();
            coupon.setOrderId(this.getId());
            coupon.setStatus("ORDER : COUPON SEND");

            OrderApplication.applicationContext.getBean(myProject_LSP.external.CouponService.class).couponSend(coupon);
        }

    }

    @PreRemove
    public void onPreRemove(){
        OrderCancelled orderCancelled = new OrderCancelled();
        this.setStatus("ORDER : ORDER CANCELED");
        BeanUtils.copyProperties(this, orderCancelled);

        orderCancelled.publishAfterCommit();

        //Following code causes dependency to external APIs
        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.

        myProject_LSP.external.Cancellation cancellation = new myProject_LSP.external.Cancellation();
        // mappings goes here
        //추가부분
        cancellation.setOrderId(this.getId());
        //cancellation.setCustomerId(this.getCustomerId());
        BeanUtils.copyProperties(this, cancellation);

        //cancellation.setStatus("ORDER : ORDER CANCELED");


        OrderApplication.applicationContext.getBean(myProject_LSP.external.CancellationService.class)
            .cancel(cancellation);


    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }
    public Integer getRestaurantMenuId() {
        return restaurantMenuId;
    }

    public void setRestaurantMenuId(Integer restaurantMenuId) {
        this.restaurantMenuId = restaurantMenuId;
    }
    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }
    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
    public Long getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Long modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getCouponStatus() {
        return couponStatus;
    }

    public void setCouponStatus(String couponStatus) {
        this.couponStatus = couponStatus;
    }
}
