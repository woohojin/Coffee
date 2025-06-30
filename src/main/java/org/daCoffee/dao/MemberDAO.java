package org.daCoffee.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.daCoffee.dto.MemberDTO;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MemberDAO {
    private final SqlSessionTemplate session;

    @Autowired
    public MemberDAO(SqlSessionTemplate session) {
        this.session = session;
    }

    private final static String NS = "org.daCoffee.dao.MemberDAO.";
    private static Map map = new HashMap<>();

    public int memberInsert(MemberDTO memberDTO) {
        int num = session.insert(NS + "memberInsert", memberDTO);
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

    public List<MemberDTO> memberList(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<MemberDTO> list = session.selectList(NS + "memberList", map);
        return list;
    }

    public List<MemberDTO> memberListAll() {
        List<MemberDTO> list = session.selectList(NS + "memberListAll");
        return list;
    }

    public MemberDTO memberSelectOne(String memberId) {
        MemberDTO mem = session.selectOne(NS + "memberSelectOne", memberId);
        return mem;
    }

    public int disabledMemberSelectOne(String memberId) {
        int num = session.selectOne(NS + "disabledMemberSelectOne", memberId);
        return num;
    }

    public List<MemberDTO> memberFindId(String memberName, String memberEmail) {
        map.clear();
        map.put("memberName", memberName);
        map.put("memberEmail", memberEmail);
        List<MemberDTO> list = session.selectList(NS + "memberFindId", map);
        return list;
    }

    public String memberFindPassword(String memberId, String memberEmail) {
        map.clear();
        map.put("memberId", memberId);
        map.put("memberEmail", memberEmail);
        String str = session.selectOne(NS + "memberFindPassword", map);
        return str;
    }

    public List<MemberDTO> memberListByMemberCompanyName(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<MemberDTO> list = session.selectList(NS + "memberListByMemberCompanyName", map);
        return list;
    }

    public List<MemberDTO> memberListDescByMemberCompanyName(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<MemberDTO> list = session.selectList(NS + "memberListDescByMemberCompanyName", map);
        return list;
    }

    public List<MemberDTO> memberListByMemberFranCode(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<MemberDTO> list = session.selectList(NS + "memberListByMemberFranCode", map);
        return list;
    }

    public List<MemberDTO> memberListDescByMemberFranCode(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<MemberDTO> list = session.selectList(NS + "memberListDescByMemberFranCode", map);
        return list;
    }

    public List<MemberDTO> memberListByMemberId(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<MemberDTO> list = session.selectList(NS + "memberListByMemberId", map);
        return list;
    }

    public List<MemberDTO> memberListDescByMemberId(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<MemberDTO> list = session.selectList(NS + "memberListDescByMemberId", map);
        return list;
    }

    public List<MemberDTO> memberListByMemberName(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<MemberDTO> list = session.selectList(NS + "memberListByMemberName", map);
        return list;
    }

    public List<MemberDTO> memberListDescByMemberName(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<MemberDTO> list = session.selectList(NS + "memberListDescByMemberName", map);
        return list;
    }

    public List<MemberDTO> memberListByMemberTel(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<MemberDTO> list = session.selectList(NS + "memberListByMemberTel", map);
        return list;
    }

    public List<MemberDTO> memberListDescByMemberTel(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<MemberDTO> list = session.selectList(NS + "memberListDescByMemberTel", map);
        return list;
    }

    public List<MemberDTO> memberListByMemberCompanyTel(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<MemberDTO> list = session.selectList(NS + "memberListByMemberCompanyTel", map);
        return list;
    }

    public List<MemberDTO> memberListDescByMemberCompanyTel(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<MemberDTO> list = session.selectList(NS + "memberListDescByMemberCompanyTel", map);
        return list;
    }

    public List<MemberDTO> memberListByMemberTier(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<MemberDTO> list = session.selectList(NS + "memberListByMemberTier", map);
        return list;
    }

    public List<MemberDTO> memberListDescByMemberTier(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<MemberDTO> list = session.selectList(NS + "memberListDescByMemberTier", map);
        return list;
    }

    public List<MemberDTO> memberListByMemberDate(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<MemberDTO> list = session.selectList(NS + "memberListByMemberDate", map);
        return list;
    }

    public List<MemberDTO> memberListDescByMemberDate(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<MemberDTO> list = session.selectList(NS + "memberListDescByMemberDate", map);
        return list;
    }

    public List<MemberDTO> memberListByMemberDisable(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<MemberDTO> list = session.selectList(NS + "memberListByMemberDisable", map);
        return list;
    }

    public List<MemberDTO> memberListDescByMemberDisable(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<MemberDTO> list = session.selectList(NS + "memberListDescByMemberDisable", map);
        return list;
    }

    public List<MemberDTO> memberSearchListByMemberCompanyName(int pageInt, int limit, String searchText) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        map.put("searchText", searchText);
        List<MemberDTO> list = session.selectList(NS + "memberSearchListByMemberCompanyName", map);
        return list;
    }

    public List<MemberDTO> memberSearchListByMemberFranCode(int pageInt, int limit, String searchText) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        map.put("searchText", searchText);
        List<MemberDTO> list = session.selectList(NS + "memberSearchListByMemberFranCode", map);
        return list;
    }

    public List<MemberDTO> memberSearchListByMemberFranCodeByNull(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<MemberDTO> list = session.selectList(NS + "memberSearchListByMemberFranCodeByNull", map);
        return list;
    }

    public List<MemberDTO> memberSearchListByMemberId(int pageInt, int limit, String searchText) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        map.put("searchText", searchText);
        List<MemberDTO> list = session.selectList(NS + "memberSearchListByMemberId", map);
        return list;
    }

    public List<MemberDTO> memberSearchListByMemberName(int pageInt, int limit, String searchText) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        map.put("searchText", searchText);
        List<MemberDTO> list = session.selectList(NS + "memberSearchListByMemberName", map);
        return list;
    }

    public List<MemberDTO> memberSearchListByMemberTel(int pageInt, int limit, String searchText) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        map.put("searchText", searchText);
        List<MemberDTO> list = session.selectList(NS + "memberSearchListByMemberTel", map);
        return list;
    }

    public List<MemberDTO> memberSearchListByMemberCompanyTel(int pageInt, int limit, String searchText) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        map.put("searchText", searchText);
        List<MemberDTO> list = session.selectList(NS + "memberSearchListByMemberCompanyTel", map);
        return list;
    }

    public List<MemberDTO> memberSearchListByMemberTier(int pageInt, int limit, String searchText) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        map.put("searchText", searchText);
        List<MemberDTO> list = session.selectList(NS + "memberSearchListByMemberTier", map);
        return list;
    }

    public List<MemberDTO> memberSearchListByMemberDisable(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<MemberDTO> list = session.selectList(NS + "memberSearchListByMemberDisable", map);
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

    public void memberUpdate(MemberDTO memberDTO) {
        session.update(NS + "memberUpdate", memberDTO);
    }

    public void memberUpdateNotPassword(MemberDTO memberDTO) {
        session.update(NS + "memberUpdateNotPassword", memberDTO);
    }

    public void memberAdminUpdate(MemberDTO memberDTO) {
        session.update(NS + "memberAdminUpdate", memberDTO);
    }

    public int memberWithdrawal(String memberId) {
        int num = session.insert(NS + "memberWithdrawal", memberId);
        return num;
    }

    public List<MemberDTO> memberWithdrawalList(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<MemberDTO> list = session.selectList(NS + "memberWithdrawalList", map);
        return list;
    }

    public int memberWithdrawalCount() {
        int num = session.selectOne(NS + "memberWithdrawalCount");
        return num;
    }

    public MemberDTO memberDisableSelectOne(String memberId) {
        MemberDTO mem = session.selectOne(NS + "memberDisableSelectOne", memberId);
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
