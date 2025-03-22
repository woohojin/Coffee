package org.daCoffee.service;

import java.util.HashMap;
import java.util.Map;

import org.daCoffee.model.Cookie;
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

    private final static String NS = "org.daCoffee.service.CookieDAO.";
    private static Map map = new HashMap<>();

    public int cookieInsert(String memberId, String token) {
        map.clear();
        map.put("memberId", memberId);
        map.put("token", token);
        int num = session.insert(NS+"cookieInsert", map);
        return num;
    }

    public Cookie cookieSelectOne(String memberId) {
        Cookie cookie = session.selectOne(NS + "cookieSelectOne", memberId);
        return cookie;
    }

    public int cookieDelete(String memberId) {
        int num = session.delete(NS + "cookieDelete", memberId);
        return num;
    }

}
