package service;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import model.Product;

@Component
public class productDAO {
    @Autowired
    SqlSessionTemplate session;

    private final static String NS = "product.";
    private static Map map = new HashMap<>();

    public int productInsert(Product product) {
        int num = session.insert(NS+"productInsert", product);
        return num;
    }

    public int productCount() {
        int num = session.selectOne(NS + "productCount");
        return num;
    }

    public int productLeaseCount() {
        int num = session.selectOne(NS + "productLeaseCount");
        return num;
    }

    public void productSet() {
        session.selectOne(NS + "productSet");
    }

    public List<Product> productList(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Product> list = session.selectList(NS + "productList", map);
        return list;
    }

    public List<Product> productLeaseList(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Product> list = session.selectList(NS + "productLeaseList", map);
        return list;
    }


}
