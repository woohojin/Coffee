package service;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

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

    public int memberInsert(Member mem) {
        int num = session.insert(NS+"memberInsert", mem);
        return num;
    }
    public List<Member> memberList() {
        List<Member> list = session.selectList(NS + "memberList");
        return list;
    }

    public Member memberSelectOne(String memberId) {
        Member mem = session.selectOne(NS + "memberSelectOne", memberId);
        return mem;
    }

    public int memberCheckTierById(String memberId) {
        int num = session.selectOne(NS + "memberCheckTier0");
        return num;
    }

    public int memberCheckTierByTier(int memberTier) {
        map.clear();
        map.put("memberTier", memberTier);
        int num = session.selectOne(NS + "memberCheckTier1");
        return num;
    }

}
