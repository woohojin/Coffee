package service;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import model.Product;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import model.Member;

@Component
public class memberDAO {
    @Autowired
    SqlSessionTemplate session;

    private final static String NS = "member.";
    private static Map map = new HashMap<>();

    public int memberInsert(Member member) {
        int num = session.insert(NS+"memberInsert", member);
        return num;
    }

    public void memberSet() {
        session.selectOne(NS + "memberSet");
    }

    public int memberCount() {
        int num = session.selectOne(NS + "memberCount");
        return num;
    }

    public List<Member> memberList(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Member> list = session.selectList(NS + "memberList", map);
        return list;
    }

    public Member memberSelectOne(String memberId) {
        Member mem = session.selectOne(NS + "memberSelectOne", memberId);
        return mem;
    }

    public void memberTierUpdate(String memberId, int memberTier) {
        map.clear();
        map.put("memberId", memberId);
        map.put("memberTier", memberTier);
        session.update(NS + "memberTierUpdate", map);
    }

    public void memberUpdate(Member member) {
        session.update(NS + "memberUpdate", member);
    }

    public void memberUpdateNotPassword(Member member) {
        session.update(NS + "memberUpdateNotPassword", member);
    }

}
