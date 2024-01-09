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

  public List<Cart> cartSelectMember(String memberId) {
    List<Cart> list = session.selectList(NS + "cartSelectMember", memberId);
    return list;
  }

  public Cart cartSelectOne(String memberId, String productCode) {
    map.clear();
    map.put("memberId", memberId);
    map.put("productCode", productCode);
    Cart cart = session.selectOne(NS + "cartSelectOne", map);

    return cart;
  }

  public String cartSumPrice(String memberId) {
    String sumPrice = session.selectOne(NS + "cartSumPrice", memberId);
    if(sumPrice == null) {
      sumPrice = "0";
    }
    return sumPrice;
  }

  public int cartDelete(String memberId, String productCode) {
    map.clear();
    map.put("memberId", memberId);
    map.put("productCode", productCode);
    int num = session.insert(NS + "cartDelete", map);
    return num;
  }

  public void cartQuantityUpdate(String memberId, String productCode, int quantity) {
    map.clear();
    map.put("memberId", memberId);
    map.put("productCode", productCode);
    map.put("quantity", quantity);
    session.update(NS + "cartQuantityUpdate", map);
  }

//  public void cartGrindingUpdate(String memberId, String productCode, int productGrinding) {
//    map.clear();
//    map.put("memberId", memberId);
//    map.put("productCode", productCode);
//    map.put("productGrinding", productGrinding);
//    session.update(NS + "cartGrindingUpdate", map);
//  }

}
