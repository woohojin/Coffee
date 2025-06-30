package org.daCoffee.dao;

import java.util.HashMap;
import java.util.Map;

import org.daCoffee.dto.CookieDTO;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class CookieDAO {
    private final SqlSessionTemplate session;

    @Autowired
    public CookieDAO(SqlSessionTemplate session) {
        this.session = session;
    }

    private final static String NS = "org.daCoffee.dao.CookieDAO.";
    private static Map map = new HashMap<>();

    public int cookieInsert(String memberId, String token) {
        map.clear();
        map.put("memberId", memberId);
        map.put("token", token);
        int num = session.insert(NS+"cookieInsert", map);
        return num;
    }

    public CookieDTO cookieSelectOne(String memberId) {
        CookieDTO cookieDTO = session.selectOne(NS + "cookieSelectOne", memberId);
        return cookieDTO;
    }

    public int cookieDelete(String memberId) {
        int num = session.delete(NS + "cookieDelete", memberId);
        return num;
    }

}
