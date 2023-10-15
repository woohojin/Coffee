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

    public int productCount(int memberTier) {
        map.clear();
        map.put("memberTier", memberTier);
        int num = session.selectOne(NS + "productCount", map);
        return num;
    }

    public int productSearchCount(int memberTier, String searchText) {
        map.clear();
        map.put("memberTier", memberTier);
        map.put("searchText", searchText);
        int num = session.selectOne(NS + "productCount", map);
        return num;
    }

    public void productSet() {
        session.selectOne(NS + "productSet");
    }

    public List<Product> productList(int pageInt, int limit, int memberTier) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        map.put("memberTier", memberTier);
        List<Product> list = session.selectList(NS + "productList", map);
        return list;
    }

    public List<Product> productSearchList(int pageInt, int limit, int memberTier, String searchText) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        map.put("memberTier", memberTier);
        map.put("searchText", searchText);
        List<Product> list = session.selectList(NS + "productSearchList", map);
        return list;
    }

}
