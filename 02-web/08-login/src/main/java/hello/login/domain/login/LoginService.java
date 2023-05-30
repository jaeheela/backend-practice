package hello.login.domain.login;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    /**
     * @return null 로그인 실패
     */
    public Member login(String loginId, String password) {
        /*
        Optional<Member> findMemberOptional = memberRepository.findByLoginId(loginId);
        Member member = findMemberOptional.get();
        if(member.getPassword().equals(password)) {
            return member;
        } else {
            return null;
        }
        */
        //자바8 Optional stream
        return memberRepository.findByLoginId(loginId)
                .filter(m -> m.getPassword().equals(password)) //같은거 반환
                .orElse(null); //그렇지 않으면 null 반환
    }
}
