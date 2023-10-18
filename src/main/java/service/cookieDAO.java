package service;

import java.util.HashMap;
import java.util.Map;

import model.CookieDTO;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class cookieDAO {
    @Autowired
    SqlSessionTemplate session;

    private final static String NS = "cookie.";
    private static Map map = new HashMap<>();

    public int cookieInsert(String memberId, String token) {
        map.clear();
        map.put("memberId", memberId);
        map.put("token", token);
        int num = session.insert(NS+"cookieInsert", map);
        return num;
    }

    public CookieDTO cookieSelectOne(String memberId) {
        CookieDTO cookie = session.selectOne(NS + "cookieSelectOne", memberId);
        return cookie;
    }

}
