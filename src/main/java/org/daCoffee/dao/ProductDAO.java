package org.daCoffee.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.daCoffee.dto.*;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductDAO {
    private final SqlSessionTemplate session;

    @Autowired
    public ProductDAO(SqlSessionTemplate session) {
        this.session = session;
    }

    private final static String NS = "org.daCoffee.dao.ProductDAO.";
    private static Map map = new HashMap<>();

    public int productInsert(ProductDTO productDTO) {
        int num = session.insert(NS + "productInsert", productDTO);
        return num;
    }

    public int productUpdate(ProductDTO productDTO) {
        int num = session.update(NS + "productUpdate", productDTO);
        return num;
    }

    public int beanInsert(ProductDTO productDTO) {
        int num = session.insert(NS + "beanInsert", productDTO);
        return num;
    }

    public int beanUpdate(ProductDTO productDTO) {
        int num = session.update(NS + "beanUpdate", productDTO);
        return num;
    }

    public int mixInsert(ProductDTO productDTO) {
        int num = session.insert(NS + "mixInsert", productDTO);
        return num;
    }

    public int mixUpdate(ProductDTO productDTO) {
        int num = session.update(NS + "mixUpdate", productDTO);
        return num;
    }

    public int cafeInsert(ProductDTO productDTO) {
        int num = session.insert(NS + "cafeInsert", productDTO);
        return num;
    }

    public int productDelete(String productCode) {
        int num = session.delete(NS + "productDelete", productCode);
        return num;
    }

    public int beanDelete(String productCode) {
        int num = session.delete(NS + "beanDelete", productCode);
        return num;
    }

    public int mixDelete(String productCode) {
        int num = session.delete(NS + "mixDelete", productCode);
        return num;
    }

    public int cafeDelete(String productCode) {
        int num = session.delete(NS + "cafeDelete", productCode);
        return num;
    }

    public void productSoldOutUpdate(String productCode, int productSoldOut) {
        map.clear();
        map.put("productCode", productCode);
        map.put("productSoldOut", productSoldOut);
        session.update(NS + "productSoldoutUpdate", map);
    }

    public int productCount() {
        int num = session.selectOne(NS + "productCount");
        return num;
    }

    public int productCountByTierByProductType(int memberTier, int productType) {
        map.clear();
        map.put("memberTier", memberTier);
        map.put("productType", productType);
        int num = session.selectOne(NS + "productCountByTierByProductType", map);
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

    public String productTypeFindByProductCode(String productCode) {
        map.clear();
        map.put("productCode", productCode);
        String str = session.selectOne(NS + "productTypeFindByProductCode", map);
        return str;
    }

    public ProductDTO productSelectOne(String productCode) {
        ProductDTO productDTO = session.selectOne(NS + "productSelectOne", productCode);
        return productDTO;
    }

    public ProductDTO beanSelectOne(String productCode) {
        ProductDTO productDTO = session.selectOne(NS + "beanSelectOne", productCode);
        return productDTO;
    }

    public ProductDTO mixSelectOne(String productCode) {
        ProductDTO productDTO = session.selectOne(NS + "mixSelectOne", productCode);
        return productDTO;
    }

    public ProductDTO cafeSelectOne(String productCode) {
        ProductDTO productDTO = session.selectOne(NS + "cafeSelectOne", productCode);
        return productDTO;
    }

    public List<ProductDTO> productListAll() {
        List<ProductDTO> list = session.selectList(NS + "productListAll");
        return list;
    }

    public void rownumSet() {
        session.selectOne(NS + "rownumSet");
    }

    public List<ProductDTO> productList(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<ProductDTO> list = session.selectList(NS + "productList", map);
        return list;
    }

    public List<ProductDTO> productListByMemberTierByProductType(int pageInt, int limit, int memberTier, int productType) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        map.put("memberTier", memberTier);
        map.put("productType", productType);
        List<ProductDTO> list = session.selectList(NS + "productListByMemberTierByProductType", map);
        return list;
    }

    public List<ProductDTO> productSearchListByMemberTier(int pageInt, int limit, int memberTier, String searchText) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        map.put("memberTier", memberTier);
        map.put("searchText", searchText);
        List<ProductDTO> list = session.selectList(NS + "productSearchListByMemberTier", map);
        return list;
    }

    public List<ProductDTO> productSearchListByProductCode(int pageInt, int limit, String searchText) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        map.put("searchText", searchText);
        List<ProductDTO> list = session.selectList(NS + "productSearchListByProductCode", map);
        return list;
    }

    public List<ProductDTO> productSearchListByProductName(int pageInt, int limit, String searchText) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        map.put("searchText", searchText);
        List<ProductDTO> list = session.selectList(NS + "productSearchListByProductName", map);
        return list;
    }

    public List<ProductDTO> productSearchListByProductType(int pageInt, int limit, String searchText) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        map.put("searchText", searchText);
        List<ProductDTO> list = session.selectList(NS + "productSearchListByProductType", map);
        return list;
    }

    public List<ProductDTO> productSearchListByProductPrice(int pageInt, int limit, String searchText) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        map.put("searchText", searchText);
        List<ProductDTO> list = session.selectList(NS + "productSearchListByProductPrice", map);
        return list;
    }

    public List<ProductDTO> productSearchListByProductUnit(int pageInt, int limit, String searchText) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        map.put("searchText", searchText);
        List<ProductDTO> list = session.selectList(NS + "productSearchListByProductUnit", map);
        return list;
    }

    public List<ProductDTO> productSearchListByProductCountry(int pageInt, int limit, String searchText) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        map.put("searchText", searchText);
        List<ProductDTO> list = session.selectList(NS + "productSearchListByProductCountry", map);
        return list;
    }

    public List<ProductDTO> productSearchListByProductSpecies(int pageInt, int limit, String searchText) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        map.put("searchText", searchText);
        List<ProductDTO> list = session.selectList(NS + "productSearchListByProductSpecies", map);
        return list;
    }

    public List<ProductDTO> productSearchListByProductCompany(int pageInt, int limit, String searchText) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        map.put("searchText", searchText);
        List<ProductDTO> list = session.selectList(NS + "productSearchListByProductCompany", map);
        return list;
    }

    public List<ProductDTO> productSearchListByProductTier(int pageInt, int limit, String searchText) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        map.put("searchText", searchText);
        List<ProductDTO> list = session.selectList(NS + "productSearchListByProductTier", map);
        return list;
    }

    public List<ProductDTO> productSearchListByProductSoldOut(int pageInt, int limit, String searchText) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        map.put("searchText", searchText);
        List<ProductDTO> list = session.selectList(NS + "productSearchListByProductSoldOut", map);
        return list;
    }

    public List<ProductDTO> productListByProductType(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<ProductDTO> list = session.selectList(NS + "productListByProductType", map);
        return list;
    }

    public List<ProductDTO> productListDescByProductType(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<ProductDTO> list = session.selectList(NS + "productListDescByProductType", map);
        return list;
    }

    public List<ProductDTO> productListByProductCode(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<ProductDTO> list = session.selectList(NS + "productListByProductCode", map);
        return list;
    }

    public List<ProductDTO> productListDescByProductCode(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<ProductDTO> list = session.selectList(NS + "productListDescByProductCode", map);
        return list;
    }

    public List<ProductDTO> productListByProductName(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<ProductDTO> list = session.selectList(NS + "productListByProductName", map);
        return list;
    }

    public List<ProductDTO> productListDescByProductName(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<ProductDTO> list = session.selectList(NS + "productListDescByProductName", map);
        return list;
    }

    public List<ProductDTO> productListByProductUnit(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<ProductDTO> list = session.selectList(NS + "productListByProductUnit", map);
        return list;
    }

    public List<ProductDTO> productListDescByProductUnit(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<ProductDTO> list = session.selectList(NS + "productListDescByProductUnit", map);
        return list;
    }

    public List<ProductDTO> productListByProductPrice(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<ProductDTO> list = session.selectList(NS + "productListByProductPrice", map);
        return list;
    }

    public List<ProductDTO> productListDescByProductPrice(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<ProductDTO> list = session.selectList(NS + "productListDescByProductPrice", map);
        return list;
    }

    public List<ProductDTO> productListByProductCountry(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<ProductDTO> list = session.selectList(NS + "productListByProductCountry", map);
        return list;
    }

    public List<ProductDTO> productListDescByProductCountry(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<ProductDTO> list = session.selectList(NS + "productListDescByProductCountry", map);
        return list;
    }

    public List<ProductDTO> productListByProductSpecies(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<ProductDTO> list = session.selectList(NS + "productListByProductSpecies", map);
        return list;
    }

    public List<ProductDTO> productListDescByProductSpecies(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<ProductDTO> list = session.selectList(NS + "productListDescByProductSpecies", map);
        return list;
    }

    public List<ProductDTO> productListByProductCompany(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<ProductDTO> list = session.selectList(NS + "productListByProductCompany", map);
        return list;
    }

    public List<ProductDTO> productListDescByProductCompany(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<ProductDTO> list = session.selectList(NS + "productListDescByProductCompany", map);
        return list;
    }

    public List<ProductDTO> productListByProductTier(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<ProductDTO> list = session.selectList(NS + "productListByProductTier", map);
        return list;
    }

    public List<ProductDTO> productListDescByProductTier(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<ProductDTO> list = session.selectList(NS + "productListDescByProductTier", map);
        return list;
    }

    public List<ProductDTO> productListByProductSoldOut(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<ProductDTO> list = session.selectList(NS + "productListByProductSoldOut", map);
        return list;
    }

    public List<ProductDTO> productListDescByProductSoldOut(int pageInt, int limit) {
        map.clear();
        map.put("start", (pageInt - 1) * limit + 1);
        map.put("end", (pageInt * limit));
        List<ProductDTO> list = session.selectList(NS + "productListDescByProductSoldOut", map);
        return list;
    }

}
