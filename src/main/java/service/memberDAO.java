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

    public List<Product> memberListByMemberCompanyName(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Product> list = session.selectList(NS + "memberListByMemberCompanyName", map);
        return list;
    }

    public List<Product> memberListDescByMemberCompanyName(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Product> list = session.selectList(NS + "memberListDescByMemberCompanyName", map);
        return list;
    }

    public List<Product> memberListByMemberFranCode(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Product> list = session.selectList(NS + "memberListByMemberFranCode", map);
        return list;
    }

    public List<Product> memberListDescByMemberFranCode(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Product> list = session.selectList(NS + "memberListDescByMemberFranCode", map);
        return list;
    }

    public List<Product> memberListByMemberId(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Product> list = session.selectList(NS + "memberListByMemberId", map);
        return list;
    }

    public List<Product> memberListDescByMemberId(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Product> list = session.selectList(NS + "memberListDescByMemberId", map);
        return list;
    }

    public List<Product> memberListByMemberName(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Product> list = session.selectList(NS + "memberListByMemberName", map);
        return list;
    }

    public List<Product> memberListDescByMemberName(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Product> list = session.selectList(NS + "memberListDescByMemberName", map);
        return list;
    }

    public List<Product> memberListByMemberTel(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Product> list = session.selectList(NS + "memberListByMemberTel", map);
        return list;
    }

    public List<Product> memberListDescByMemberTel(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Product> list = session.selectList(NS + "memberListDescByMemberTel", map);
        return list;
    }

    public List<Product> memberListByMemberCompanyTel(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Product> list = session.selectList(NS + "memberListByMembeCompanyrTel", map);
        return list;
    }

    public List<Product> memberListDescByMemberCompanyTel(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Product> list = session.selectList(NS + "memberListDescByMemberCompanyTel", map);
        return list;
    }

    public List<Product> memberListByMemberTier(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Product> list = session.selectList(NS + "memberListByMemberTier", map);
        return list;
    }

    public List<Product> memberListDescByMemberTier(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Product> list = session.selectList(NS + "memberListDescByMemberTier", map);
        return list;
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
