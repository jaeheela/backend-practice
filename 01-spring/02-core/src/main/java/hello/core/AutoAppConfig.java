package hello.core;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import static org.springframework.context.annotation.ComponentScan.*;

//컴포넌트 스캔과 의존관계 자동 주입 시작하기
// => 지금까지 스프링 빈을 등록할 때는 자바 코드의 @Bean이나 XML의 <bean> 등을 통해서 설정 정보에 직접 등록할 스프링 빈을 나열했다.
// => 예제에서는 몇개가 안되었지만, 이렇게 등록해야 할 스프링 빈이 수십, 수백개가 되면 일일이 등록하기도 귀찮고, 설정 정보도 커지고, 누락하는 문제도 발생한다.
// => 역시 개발자는 반복을 싫어한다.(무엇보다 귀찮다 ᅲᅲ)
// => 그래서 스프링은 설정 정보가 없어도 자동으로 스프링 빈을 등록하는 컴포넌트 스캔이라는 기능을 제공한다.
// => 또 의존관계도 자동으로 주입하는 @Autowired 라는 기능도 제공한다.
// => 코드로 컴포넌트 스캔과 의존관계 자동 주입을 알아보자.
// => 먼저 기존 AppConfig.java는 과거 코드와 테스트를 유지하기 위해 남겨두고, 새로운 AutoAppConfig.java를 만들자.
// => 컴포넌트 스캔을 사용하려면 먼저 @ComponentScan 을 설정 정보에 붙여주면 된다.
// => 기존의 AppConfig와는 다르게 @Bean으로 등록한 클래스가 하나도 없다!
// => 참고로 컴포넌트 스캔을 사용하면 @Configuration 이 붙은 설정 정보도 자동으로 등록되기 때문에,
// => AppConfig, TestConfig 등 앞서 만들어두었던 설정 정보도 함께 등록되고, 실행되어 버린다.
// => 그래서 excludeFilters 를 이용해서 설정정보는 컴포넌트 스캔 대상에서 제외했다.
// => 보통 설정 정보를 컴포넌트 스캔 대상에서 제외하지는 않지만, 기존 예제 코드를 최대한 남기고 유지하기 위해서 이 방법을 선택했다.
// => 컴포넌트 스캔은 이름 그대로 @Component 애노테이션이 붙은 클래스를 스캔해서 스프링 빈으로 등록한다. @Component 를 붙여주자.
// => 참고로 @Configuration 이 컴포넌트 스캔의 대상이 된 이유도 @Configuration 소스코드를 열어보면 @Component 애노테이션이 붙어있기 때문이다.
// => 이제 각 클래스가 컴포넌트 스캔의 대상이 되도록 @Component 애노테이션을 붙여주자.
// => MemoryMemberRepository @Component 추가
// => RateDiscountPolicy @Component 추가
// => MemberServiceImpl @Component, @Autowired 추가
// => OrderServiceImpl @Component, @Autowired 추가

//탐색 위치와 기본 스캔 대상
// => 모든 자바 클래스를 다 컴포넌트 스캔하면 시간이 오래 걸린다. 그래서 꼭 필요한 위치부터 탐색하도록 시작 위치를 지정할 수 있다.
// => @ComponentScan(basePackages = "hello.core")
// => basePackages : 탐색할 패키지의 시작 위치를 지정한다. 이 패키지를 포함해서 하위 패키지를 모두 탐색한다.
// => basePackages = {"hello.core", "hello.service"} 이렇게 여러 시작 위치를 지정할 수도 있다.
// => basePackageClasses : 지정한 클래스의 패키지를 탐색 시작 위치로 지정한다.
// => 만약 지정하지 않으면 @ComponentScan 이 붙은 설정 정보 클래스의 패키지가 시작 위치가 된다.

//권장하는 방법
// => 개인적으로 즐겨 사용하는 방법은 패키지 위치를 지정하지 않고, 설정 정보 클래스의 위치를 프로젝트 최상단에 두는 것이다.
// => 최근 스프링 부트도 이 방법을 기본으로 제공한다.
// => 예를 들어서 프로젝트가 다음과 같이 구조가 되어 있으면
// => com.hello
// => com.hello.serivce
// => com.hello.repository
// => com.hello 프로젝트 시작 루트, 여기에 AppConfig 같은 메인 설정 정보를 두고, @ComponentScan 애노테이션을 붙이고, basePackages 지정은 생략한다.
// => 이렇게 하면 com.hello 를 포함한 하위는 모두 자동으로 컴포넌트 스캔의 대상이 된다.
// => 그리고 프로젝트 메인 설정 정보는 프로젝트를 대표하는 정보이기 때문에 프로젝트 시작 루트 위치에 두는 것이 좋다 생각한다.
// => 참고로 스프링 부트를 사용하면 스프링 부트의 대표 시작 정보인 @SpringBootApplication 를 이 프로젝트 시작 루트 위치에 두는 것이 관례이다.
// => (그리고 이 설정안에 바로 @ComponentScan 이 들어있다!)

//컴포넌트 스캔 기본 대상
// => 컴포넌트 스캔은 @Component 뿐만 아니라 다음과 내용도 추가로 대상에 포함한다.
// => @Component : 컴포넌트 스캔에서 사용
// => @Controlller : 스프링 MVC 컨트롤러에서 사용
// => @Service : 스프링 비즈니스 로직에서 사용
// => @Repository : 스프링 데이터 접근 계층에서 사용
// => @Configuration : 스프링 설정 정보에서 사용
// => 해당 클래스의 소스 코드를 보면 @Component 를 포함하고 있는 것을 알 수 있다.

// => 참고로 사실 애노테이션에는 상속관계라는 것이 없다.
// => 그래서 이렇게 애노테이션이 특정 애노테이션을 들고 있는 것을 인식할 수 있는 것은 자바 언어가 지원하는 기능은 아니고, 스프링이 지원하는 기능이다.
// => 컴포넌트 스캔의 용도 뿐만 아니라 다음 애노테이션이 있으면 스프링은 부가 기능을 수행한다.
// => @Controller : 스프링 MVC 컨트롤러로 인식
// => @Repository : 스프링 데이터 접근 계층으로 인식하고, 데이터 계층의 예외를 스프링 예외로 변환해준다.
// => @Configuration : 앞서 보았듯이 스프링 설정 정보로 인식하고, 스프링 빈이 싱글톤을 유지하도록 추가 처리를 한다.
// => @Service : 사실 @Service 는 특별한 처리를 하지 않는다. 대신 개발자들이 핵심 비즈니스 로직이 여기에 있겠구나 라고 비즈니스 계층을 인식하는데 도움이 된다.
// => 참고로 useDefaultFilters 옵션은 기본으로 켜져있는데, 이 옵션을 끄면 기본 스캔 대상들이 제외된다.
// => 그냥 이런 옵션이 있구나 정도 알고 넘어가자.

//필터
// => includeFilters : 컴포넌트 스캔 대상을 추가로 지정한다.
// => excludeFilters : 컴포넌트 스캔에서 제외할 대상을 지정한다.
// => 빠르게 예제로 확인해보자. 모든 코드는 테스트 코드에 추가

//중복 등록과 충돌
// => 컴포넌트 스캔에서 같은 빈 이름을 등록하면 어떻게 될까? 다음 두가지 상황이 있다.
// => 1. 자동 빈 등록 vs 자동 빈 등록 : 컴포넌트 스캔에 의해 자동으로 스프링 빈이 등록되는데, 그 이름이 같은 경우 스프링은 오류를 발생시킨다. - ConflictingBeanDefinitionException 예외 발생
// => 2. 수동 빈 등록 vs 자동 빈 등록 : 만약 수동 빈 등록과 자동 빈 등록에서 빈 이름이 충돌되면 어떻게 될까?
// => 이 경우 수동 빈 등록이 우선권을 가진다. (수동 빈이 자동 빈을 오버라이딩 해버린다.)
// => 수동 빈 등록시 남는 로그 : Overriding bean definition for bean 'memoryMemberRepository' with a different , definition: replacing
// => 물론 개발자가 의도적으로 이런 결과를 기대했다면, 자동 보다는 수동이 우선권을 가지는 것이 좋다.
// => 하지만 현실은 개발자가 의도적으로 설정해서 이런 결과가 만들어지기 보다는 여러 설정들이 꼬여서 이런 결과가 만들어지는 경우가 대부분이다!
// => 그러면 정말 잡기 어려운 버그가 만들어진다. 항상 잡기 어려운 버그는 애매한 버그다.
// => 그래서 최근 스프링 부트에서는 수동 빈 등록과 자동 빈 등록이 충돌나면 오류가 발생하도록 기본 값을 바꾸었다.
// => 수동 빈 등록, 자동 빈 등록 오류시 스프링 부트 에러
// => Consider renaming one of the beans or enabling overriding by setting spring.main.allow-bean-definition-overriding=true
// => 스프링 부트인 CoreApplication 을 실행해보면 오류를 볼 수 있다.

//컴포넌트 스캔 대상에 추가할 애노테이션
@Configuration
@ComponentScan( excludeFilters = @Filter(type = FilterType.ANNOTATION, classes = Configuration.class), basePackages = "hello.core")
public class AutoAppConfig {

    //수동 빈 등록 vs 자동 빈 등록 충돌나기 위해 추가
    /*
    @Bean(name = "memoryMemberRepository")
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }
    */
}
