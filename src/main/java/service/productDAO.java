package service;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import model.Member;
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

    public int productCountByTier(int memberTier) {
        int num = session.selectOne(NS + "productCount", memberTier);
        return num;
    }

    public int productSearchCount(String searchText) {
        int num = session.selectOne(NS + "productSearchCount", searchText);
        return num;
    }

    public int productSearchCountByTier(int memberTier, String searchText) {
        map.clear();
        map.put("memberTier", memberTier);
        map.put("searchText", searchText);
        int num = session.selectOne(NS + "productSearchCount", map);
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

    public List<Product> productListByTier(int pageInt, int limit, int memberTier) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        map.put("memberTier", memberTier);
        List<Product> list = session.selectList(NS + "productListByTier", map);
        return list;
    }

    public List<Product> productSearchListByTier(int pageInt, int limit, int memberTier, String searchText) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        map.put("memberTier", memberTier);
        map.put("searchText", searchText);
        List<Product> list = session.selectList(NS + "productSearchListByTier", map);
        return list;
    }

    public Product productSelectOne(String productCode) {
        Product product = session.selectOne(NS + "productSelectOne", productCode);
        return product;
    }

}
