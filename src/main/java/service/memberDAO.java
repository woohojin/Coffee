package service;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import model.product;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import model.member;

@Component
public class memberDAO {
    @Autowired
    SqlSessionTemplate session;

    private final static String NS = "member.";
    private static Map map = new HashMap<>();

    public List<member> memberList() {
        List<member> list = session.selectList(NS + "memberList");
        return list;
    }

    public member memberCheckOne(String memberId) {
        member mem = session.selectOne(NS + "memberCheckOne", map);
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
