package org.daCoffee.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.daCoffee.dto.CartDTO;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartDAO {

  private final SqlSessionTemplate session;

  private final static String NS = "org.daCoffee.dao.CartDAO.";

  public int cartInsert(CartDTO cartDTO) {
    int num = session.insert(NS + "cartInsert", cartDTO);
    return num;
  }

  public List<String> cartProductCodeList(String memberId) {
    List<String> list = session.selectList(NS + "cartProductCodeList", memberId);
    return list;
  }

  public Integer cartCount(String memberId) {
    Integer num = session.selectOne(NS + "cartCount", memberId);
    return (num != null) ? num : 0;
  }

  public int cartCountByProductType(String memberId, int productType) {
    Map<String, Object> map = new HashMap<>();
    map.put("memberId", memberId);
    map.put("productType", productType);
    int num = session.selectOne(NS + "cartCountByProductType", map);

    return num;
  }

  public Integer checkQuantityByProductCode(String memberId, String productCode) {
    Map<String, Object> map = new HashMap<>();
    map.put("memberId", memberId);
    map.put("productCode", productCode);
    Integer num = session.selectOne(NS + "checkQuantityByProductCode", map);

    return (num != null) ? num : 0;
  }

  public List<Integer> checkQuantityByProductType(String memberId, int productType) {
    Map<String, Object> map = new HashMap<>();
    map.put("memberId", memberId);
    map.put("productType", productType);
    List<Integer> list = session.selectList(NS + "checkQuantityByProductType", map);

    return list;
  }

  public List<CartDTO> cartSelectMember(String memberId) {
    List<CartDTO> list = session.selectList(NS + "cartSelectMember", memberId);
    return list;
  }

  public CartDTO cartSelectOne(String memberId, String productCode) {
    Map<String, Object> map = new HashMap<>();
    map.put("memberId", memberId);
    map.put("productCode", productCode);
    CartDTO cartDTO = session.selectOne(NS + "cartSelectOne", map);

    return cartDTO;
  }

  public int cartSumPrice(String memberId) {
    Integer sumPrice = session.selectOne(NS + "cartSumPrice", memberId);

    return (sumPrice != null) ? sumPrice : 0;
  }

  public int cartDelete(String memberId, String productCode) {
    Map<String, Object> map = new HashMap<>();
    map.put("memberId", memberId);
    map.put("productCode", productCode);
    int num = session.delete(NS + "cartDelete", map);
    return num;
  }

  public int deleteCartByMember(String memberId) {
    int num = session.delete(NS + "deleteCartByMember", memberId);
    return num;
  }

  public void cartQuantityUpdate(String memberId, String productCode, int delta) {
    Map<String, Object> map = new HashMap<>();
    map.put("memberId", memberId);
    map.put("productCode", productCode);
    map.put("delta", delta);
    session.update(NS + "cartQuantityUpdate", map);
  }
}
