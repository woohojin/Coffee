package org.daCoffee.dao;

import java.util.HashMap;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.daCoffee.dto.CookieDTO;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CookieDAO {
    private final SqlSessionTemplate session;

    private final static String NS = "org.daCoffee.dao.CookieDAO.";

    public int cookieInsert(String memberId, String token) {
        Map<String, Object> map = new HashMap<>();
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
