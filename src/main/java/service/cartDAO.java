package service;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import model.Cart;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class cartDAO {

  @Autowired
  SqlSessionTemplate session;

  private final static String NS = "cart.";
  private static Map map = new HashMap<>();

  public int cartInsert(Cart cart) {
    int num = session.insert(NS+"cartInsert", cart);
    return num;
  }

  public int cartCount(String memberId) {
    int num = session.selectOne(NS + "cartCount", memberId);
    return num;
  }

  public List<Cart> cartList() {
    List<Cart> list = session.selectList(NS + "cartList");
    return list;
  }

  public List<Cart> cartSelectMember(String memberId) {
    List<Cart> list = session.selectList(NS + "cartSelectMember", memberId);
    return list;
  }

  public int cartDelete(String memberId, String productCode) {
    map.clear();
    map.put("memberId", memberId);
    map.put("productCode", productCode);
    int num = session.insert(NS + "boardDelete", map);
    return num;
  }

  public void cartQuantityUpdate(String memberId, String productCode, int quantity) {
    map.clear();
    map.put("memberId", memberId);
    map.put("productCode", productCode);
    map.put("quantity", quantity);
    session.update(NS + "cartQuantityUpdate", map);
  }

}
