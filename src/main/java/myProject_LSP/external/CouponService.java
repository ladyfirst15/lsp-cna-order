
package myProject_LSP.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

/*수정*/
@FeignClient(name="coupon", url="${api.url.coupon}")
public interface CouponService {

    @RequestMapping(method= RequestMethod.POST, path="/coupons")
    public void couponSend(@RequestBody Coupon coupon);

}