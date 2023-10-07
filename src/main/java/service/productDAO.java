package service;

import java.util.HashMap;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class productDAO {
    @Autowired
    SqlSessionTemplate session;

    private final static String NS = "product.";
    private static Map map = new HashMap<>();

    public int boardCount() {
        int num = session.selectOne(NS+"boardCount");
        return num;
    }

}
