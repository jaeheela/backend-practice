package hello.core.beanfind;
import hello.core.AppConfig;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import static org.assertj.core.api.Assertions.*;

//스프링 빈 조회 - 기본
// => 스프링 컨테이너에서 스프링 빈을 찾는 가장 기본적인 조회 방법 : ac.getBean(빈이름, 타입) , ac.getBean(타입)
// => 조회 대상 스프링 빈이 없으면 예외 발생 : NoSuchBeanDefinitionException: No bean named 'xxxxx' available
// => 참고로 구체 타입으로 조회하면 변경시 유연성이 떨어진다.
public class ApplicationContextBasicFindTest {
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    @DisplayName("빈 이름으로 조회")
    void findBeanByName() {
        MemberService memberService = ac.getBean("memberService", MemberService.class);
        assertThat(memberService).isInstanceOf(MemberServiceImpl.class); //ok
    }
    @Test
    @DisplayName("이름 없이 타입만으로 조회")
    void findBeanByType() {
        MemberService memberService = ac.getBean(MemberService.class);
        assertThat(memberService).isInstanceOf(MemberServiceImpl.class); //ok
    }

    @Test
    @DisplayName("구체 타입(구현체)으로 조회")
    void findBeanByName2() {
        MemberServiceImpl memberService = ac.getBean("memberService", MemberServiceImpl.class);
        assertThat(memberService).isInstanceOf(MemberServiceImpl.class); //ok
    }

    @Test
    @DisplayName("빈 이름으로 조회X")
    void findBeanByNameX() {
        //ac.getBean("xxxxx", MemberService.class); //error
        Assertions.assertThrows(NoSuchBeanDefinitionException.class, () ->
                ac.getBean("xxxxx", MemberService.class)); //ac.getBean("xxxxx", MemberService.class) 로직을 실행하면 NoSuchBeanDefinitionException 예외가 터져야 로직 성공
    }
}
