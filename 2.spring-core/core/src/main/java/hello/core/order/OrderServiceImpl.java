package hello.core.order;
import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//주문과 할인 도메인 개발 - 주문 서비스 구현체

// => 각 클래스가 컴포넌트 스캔의 대상이 되도록 OrderServiceImpl에 @Component 추가
@Component
public class OrderServiceImpl implements OrderService {
    //private final MemberRepository memberRepository = new MemoryMemberRepository();
    //private final DiscountPolicy discountPolicy = new FixDiscountPolicy(); //정액 할인 정책 구현체 이용
    //private final DiscountPolicy discountPolicy = new RateDiscountPolicy(); //정률 할인 정책 구현체 이용

    // => 설계 변경으로 OrderServiceImpl 은 FixDiscountPolicy 를 의존하지 않는다! 단지 DiscountPolicy 인터페이스만 의존한다.
    // => OrderServiceImpl 입장에서 생성자를 통해 어떤 구현 객체가 들어올지(주입될지)는 알 수 없다.
    // => OrderServiceImpl 의 생성자를 통해서 어떤 구현 객체을 주입할지는 오직 외부( AppConfig )에서 결정한다.
    // => OrderServiceImpl 은 이제부터 실행에만 집중하면 된다.
    // => OrderServiceImpl 에는 MemoryMemberRepository , FixDiscountPolicy 객체의 의존관계가 주입된다.
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy; //추상에만 의존하도록 변경(인터페이스에만 의존)

    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy
            discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }


    // => 주문 생성 요청이 오면, 회원 정보를 조회 >> 할인 정책 적용 >> 다음 주문 객체를 생성해서 반환한다.
    // => 메모리 회원 리포지토리와, 고정 금액 할인 정책을 구현체로 생성한다.
    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);
        return new Order(memberId, itemName, itemPrice, discountPrice);
    }

    //@Configuration과 싱글톤의 관계를 알기 위해  OrderServiceImpl에 테스트하기 위한 코드추가
    // => 테스트를 위해 MemberRepository를 조회할 수 있는 기능을 추가한다.
    // => 기능 검증을 위해 잠깐 사용하는 것이니 인터페이스에 조회기능까지 추가하지는 말자.
    // => ConfigurationSingletonTest에 이용됨
    //테스트 용도
    public MemberRepository getMemberRepository() { return memberRepository; }
}