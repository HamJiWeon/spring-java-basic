package hello.core.order;

import hello.core.annotation.MainDiscountPolicy;
import hello.core.discount.DiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceImpl implements OrderService{

    /*
    OrderServiceImpl 은 DiscountPolicy 뿐 만 아니라 FixDiscountPolicy 에도 의존하고 있었다.
    ==> DIP 원칙 위반

    FixDiscountPolicy 에서 RateDiscountPolicy 로 변경 시 OrderServiceImpl 도 같이 변경해줘야 한다.
    ==> OCP 원칙 위반

    인터페이스에만 의존하도록 변경
    */
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    // 생성자가 하나만 있으면 "@Autowired"가 자동으로 주입되므로 생략 가능.
    public OrderServiceImpl(MemberRepository memberRepository, @MainDiscountPolicy DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discount = discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discount);
    }

    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
