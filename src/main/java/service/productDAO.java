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

    public void rownumSet() {
        session.selectOne(NS + "rownumSet");
    }

    public List<Product> productList(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Product> list = session.selectList(NS + "productList", map);
        return list;
    }

    public List<Product> productListByMemberTier(int pageInt, int limit, int memberTier) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        map.put("memberTier", memberTier);
        List<Product> list = session.selectList(NS + "productListByMemberTier", map);
        return list;
    }

    public List<Product> productSearchListByMemberTier(int pageInt, int limit, int memberTier, String searchText) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        map.put("memberTier", memberTier);
        map.put("searchText", searchText);
        List<Product> list = session.selectList(NS + "productSearchListByMemberTier", map);
        return list;
    }

    public List<Product> productSearchListByProductCode(int pageInt, int limit, String searchText) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        map.put("searchText", searchText);
        List<Product> list = session.selectList(NS + "productSearchListByProductCode", map);
        return list;
    }

    public List<Product> productSearchListByProductName(int pageInt, int limit, String searchText) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        map.put("searchText", searchText);
        List<Product> list = session.selectList(NS + "productSearchListByProductName", map);
        return list;
    }

    public List<Product> productSearchListByProductType(int pageInt, int limit, String searchText) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        map.put("searchText", searchText);
        List<Product> list = session.selectList(NS + "productSearchListByProductType", map);
        return list;
    }

    public List<Product> productSearchListByProductPrice(int pageInt, int limit, String searchText) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        map.put("searchText", searchText);
        List<Product> list = session.selectList(NS + "productSearchListByProductPrice", map);
        return list;
    }

    public List<Product> productSearchListByProductUnit(int pageInt, int limit, String searchText) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        map.put("searchText", searchText);
        List<Product> list = session.selectList(NS + "productSearchListByProductUnit", map);
        return list;
    }

    public List<Product> productSearchListByProductCountry(int pageInt, int limit, String searchText) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        map.put("searchText", searchText);
        List<Product> list = session.selectList(NS + "productSearchListByProductCountry", map);
        return list;
    }

    public List<Product> productSearchListByProductSpecies(int pageInt, int limit, String searchText) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        map.put("searchText", searchText);
        List<Product> list = session.selectList(NS + "productSearchListByProductSpecies", map);
        return list;
    }

    public List<Product> productSearchListByProductCompany(int pageInt, int limit, String searchText) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        map.put("searchText", searchText);
        List<Product> list = session.selectList(NS + "productSearchListByProductCompany", map);
        return list;
    }

    public List<Product> productSearchListByProductTier(int pageInt, int limit, String searchText) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        map.put("searchText", searchText);
        List<Product> list = session.selectList(NS + "productSearchListByProductTier", map);
        return list;
    }

    public List<Product> productListByProductType(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Product> list = session.selectList(NS + "productListByProductType", map);
        return list;
    }

    public List<Product> productListDescByProductType(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Product> list = session.selectList(NS + "productListDescByProductType", map);
        return list;
    }

    public List<Product> productListByProductCode(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Product> list = session.selectList(NS + "productListByProductCode", map);
        return list;
    }

    public List<Product> productListDescByProductCode(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Product> list = session.selectList(NS + "productListDescByProductCode", map);
        return list;
    }

    public List<Product> productListByProductName(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Product> list = session.selectList(NS + "productListByProductName", map);
        return list;
    }

    public List<Product> productListDescByProductName(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Product> list = session.selectList(NS + "productListDescByProductName", map);
        return list;
    }

    public List<Product> productListByProductUnit(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Product> list = session.selectList(NS + "productListByProductUnit", map);
        return list;
    }

    public List<Product> productListDescByProductUnit(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Product> list = session.selectList(NS + "productListDescByProductUnit", map);
        return list;
    }

    public List<Product> productListByProductPrice(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Product> list = session.selectList(NS + "productListByProductPrice", map);
        return list;
    }

    public List<Product> productListDescByProductPrice(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Product> list = session.selectList(NS + "productListDescByProductPrice", map);
        return list;
    }

    public List<Product> productListByProductCountry(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Product> list = session.selectList(NS + "productListByProductCountry", map);
        return list;
    }

    public List<Product> productListDescByProductCountry(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Product> list = session.selectList(NS + "productListDescByProductCountry", map);
        return list;
    }

    public List<Product> productListByProductSpecies(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Product> list = session.selectList(NS + "productListByProductSpecies", map);
        return list;
    }

    public List<Product> productListDescByProductSpecies(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Product> list = session.selectList(NS + "productListDescByProductSpecies", map);
        return list;
    }

    public List<Product> productListByProductCompany(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Product> list = session.selectList(NS + "productListByProductCompany", map);
        return list;
    }

    public List<Product> productListDescByProductCompany(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Product> list = session.selectList(NS + "productListDescByProductCompany", map);
        return list;
    }

    public List<Product> productListByProductTier(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Product> list = session.selectList(NS + "productListByProductTier", map);
        return list;
    }

    public List<Product> productListDescByProductTier(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<Product> list = session.selectList(NS + "productListDescByProductTier", map);
        return list;
    }

    public Product productSelectOne(String productCode) {
        Product product = session.selectOne(NS + "productSelectOne", productCode);
        return product;
    }

}
