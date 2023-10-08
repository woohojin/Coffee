package service;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import model.product;

@Component
public class productDAO {
    @Autowired
    SqlSessionTemplate session;

    private final static String NS = "product.";
    private static Map map = new HashMap<>();

    public int productCount() {
        int num = session.selectOne(NS + "productCount");
        return num;
    }

    public int productSet() {
        session.selectOne(NS + "productSet");
        return 0;
    }

    public List<product> productList(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<product> list = session.selectList(NS + "productList", map);
        return list;
    }

    public List<product> productLeaseList(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<product> list = session.selectList(NS + "productLeaseList", map);
        return list;
    }


}
