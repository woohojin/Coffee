package org.daCoffee.service;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.daCoffee.model.Member;

@Component
public class MemberDAO {
    @Autowired
    SqlSessionTemplate session;

    private final static String NS = "org.daCoffee.service.MemberDAO.";
    private static Map map = new HashMap<>();

    public int memberInsert(Member member) {
        int num = session.insert(NS + "memberInsert", member);
        return num;
    }

    public void rownumSet() {
        session.selectOne(NS + "rownumSet");
    }

    public int memberCount() {
        int num = session.selectOne(NS + "memberCount");
        return num;
    }

    public int checkMember(String memberId) {
        int num = session.selectOne(NS + "checkMember", memberId);
        return num;
    }

    public int checkDisabledMember(String memberId) {
        int num = session.selectOne(NS + "checkDisabledMember", memberId);
        return num;
    }

    public List<Member> memberList(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Member> list = session.selectList(NS + "memberList", map);
        return list;
    }

    public List<Member> memberListAll() {
        List<Member> list = session.selectList(NS + "memberListAll");
        return list;
    }

    public Member memberSelectOne(String memberId) {
        Member mem = session.selectOne(NS + "memberSelectOne", memberId);
        return mem;
    }

    public int disabledMemberSelectOne(String memberId) {
        int num = session.selectOne(NS + "disabledMemberSelectOne", memberId);
        return num;
    }

    public List<Member> memberFindId(String memberName, String memberEmail) {
        map.clear();
        map.put("memberName", memberName);
        map.put("memberEmail", memberEmail);
        List<Member> list = session.selectList(NS + "memberFindId", map);
        return list;
    }

    public String memberFindPassword(String memberId, String memberEmail) {
        map.clear();
        map.put("memberId", memberId);
        map.put("memberEmail", memberEmail);
        String str = session.selectOne(NS + "memberFindPassword", map);
        return str;
    }

    public List<Member> memberListByMemberCompanyName(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Member> list = session.selectList(NS + "memberListByMemberCompanyName", map);
        return list;
    }

    public List<Member> memberListDescByMemberCompanyName(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Member> list = session.selectList(NS + "memberListDescByMemberCompanyName", map);
        return list;
    }

    public List<Member> memberListByMemberFranCode(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Member> list = session.selectList(NS + "memberListByMemberFranCode", map);
        return list;
    }

    public List<Member> memberListDescByMemberFranCode(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Member> list = session.selectList(NS + "memberListDescByMemberFranCode", map);
        return list;
    }

    public List<Member> memberListByMemberId(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Member> list = session.selectList(NS + "memberListByMemberId", map);
        return list;
    }

    public List<Member> memberListDescByMemberId(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Member> list = session.selectList(NS + "memberListDescByMemberId", map);
        return list;
    }

    public List<Member> memberListByMemberName(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Member> list = session.selectList(NS + "memberListByMemberName", map);
        return list;
    }

    public List<Member> memberListDescByMemberName(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Member> list = session.selectList(NS + "memberListDescByMemberName", map);
        return list;
    }

    public List<Member> memberListByMemberTel(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Member> list = session.selectList(NS + "memberListByMemberTel", map);
        return list;
    }

    public List<Member> memberListDescByMemberTel(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Member> list = session.selectList(NS + "memberListDescByMemberTel", map);
        return list;
    }

    public List<Member> memberListByMemberCompanyTel(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Member> list = session.selectList(NS + "memberListByMemberCompanyTel", map);
        return list;
    }

    public List<Member> memberListDescByMemberCompanyTel(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Member> list = session.selectList(NS + "memberListDescByMemberCompanyTel", map);
        return list;
    }

    public List<Member> memberListByMemberTier(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Member> list = session.selectList(NS + "memberListByMemberTier", map);
        return list;
    }

    public List<Member> memberListDescByMemberTier(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Member> list = session.selectList(NS + "memberListDescByMemberTier", map);
        return list;
    }

    public List<Member> memberListByMemberDate(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Member> list = session.selectList(NS + "memberListByMemberDate", map);
        return list;
    }

    public List<Member> memberListDescByMemberDate(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Member> list = session.selectList(NS + "memberListDescByMemberDate", map);
        return list;
    }

    public List<Member> memberListByMemberDisable(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Member> list = session.selectList(NS + "memberListByMemberDisable", map);
        return list;
    }

    public List<Member> memberListDescByMemberDisable(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Member> list = session.selectList(NS + "memberListDescByMemberDisable", map);
        return list;
    }

    public List<Member> memberSearchListByMemberCompanyName(int pageInt, int limit, String searchText) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        map.put("searchText", searchText);
        List<Member> list = session.selectList(NS + "memberSearchListByMemberCompanyName", map);
        return list;
    }

    public List<Member> memberSearchListByMemberFranCode(int pageInt, int limit, String searchText) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        map.put("searchText", searchText);
        List<Member> list = session.selectList(NS + "memberSearchListByMemberFranCode", map);
        return list;
    }

    public List<Member> memberSearchListByMemberFranCodeByNull(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Member> list = session.selectList(NS + "memberSearchListByMemberFranCodeByNull", map);
        return list;
    }

    public List<Member> memberSearchListByMemberId(int pageInt, int limit, String searchText) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        map.put("searchText", searchText);
        List<Member> list = session.selectList(NS + "memberSearchListByMemberId", map);
        return list;
    }

    public List<Member> memberSearchListByMemberName(int pageInt, int limit, String searchText) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        map.put("searchText", searchText);
        List<Member> list = session.selectList(NS + "memberSearchListByMemberName", map);
        return list;
    }

    public List<Member> memberSearchListByMemberTel(int pageInt, int limit, String searchText) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        map.put("searchText", searchText);
        List<Member> list = session.selectList(NS + "memberSearchListByMemberTel", map);
        return list;
    }

    public List<Member> memberSearchListByMemberCompanyTel(int pageInt, int limit, String searchText) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        map.put("searchText", searchText);
        List<Member> list = session.selectList(NS + "memberSearchListByMemberCompanyTel", map);
        return list;
    }

    public List<Member> memberSearchListByMemberTier(int pageInt, int limit, String searchText) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        map.put("searchText", searchText);
        List<Member> list = session.selectList(NS + "memberSearchListByMemberTier", map);
        return list;
    }

    public List<Member> memberSearchListByMemberDisable(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Member> list = session.selectList(NS + "memberSearchListByMemberDisable", map);
        return list;
    }

    public void memberTierUpdate(String memberId, int memberTier) {
        map.clear();
        map.put("memberId", memberId);
        map.put("memberTier", memberTier);
        session.update(NS + "memberTierUpdate", map);
    }

    public void memberFranCodeUpdate(String memberId, String memberFranCode) {
        map.clear();
        map.put("memberId", memberId);
        map.put("memberFranCode", memberFranCode);
        session.update(NS + "memberFranCodeUpdate", map);
    }

    public void memberTempPasswordUpdate(String memberId, String memberTempPassword) {
        map.clear();
        map.put("memberId", memberId);
        map.put("memberTempPassword", memberTempPassword);
        session.update(NS + "memberTempPasswordUpdate", map);
    }

    public void memberUpdate(Member member) {
        session.update(NS + "memberUpdate", member);
    }

    public void memberUpdateNotPassword(Member member) {
        session.update(NS + "memberUpdateNotPassword", member);
    }

    public void memberAdminUpdate(Member member) {
        session.update(NS + "memberAdminUpdate", member);
    }

    public int memberWithdrawal(String memberId) {
        int num = session.insert(NS + "memberWithdrawal", memberId);
        return num;
    }

    public List<Member> memberWithdrawalList(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Member> list = session.selectList(NS + "memberWithdrawalList", map);
        return list;
    }

    public int memberWithdrawalCount() {
        int num = session.selectOne(NS + "memberWithdrawalCount");
        return num;
    }

    public Member memberDisableSelectOne(String memberId) {
        Member mem = session.selectOne(NS + "memberDisableSelectOne", memberId);
        return mem;
    }

    public void memberDisable(String memberId) {
        session.insert(NS + "memberDisable", memberId);
    }

    public void memberEnable(String memberId) {
        session.insert(NS + "memberEnable", memberId);
    }

    public void memberDelete(String memberId) {
        session.delete(NS + "memberDelete", memberId);
    }

    public void disabledMemberDelete(String memberId) {
        session.delete(NS + "disabledMemberDelete", memberId);
    }

    public void updateDisabledDate(String memberId) {
        session.update(NS + "updateDisabledDate", memberId);
    }

    public void updateDisabledDateToNull(String memberId) {
        session.update(NS + "updateDisabledDateToNull", memberId);
    }

}
