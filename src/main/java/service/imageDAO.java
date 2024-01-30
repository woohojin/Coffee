package service;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import model.History;
import model.Image;
import model.Member;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import model.Product;

@Component
public class imageDAO {
  @Autowired
  SqlSessionTemplate session;

  private final static String NS = "image.";
  private static Map map = new HashMap<>();

  public int insertProductImage(Image image) {
    int num = session.insert(NS + "insertProductImage", image);
    return num;
  }

  public List<Image> selectProductImage(String productCode) {
    List<Image> list = session.selectList(NS + "selectProductImage", productCode);
    return list;
  }

  public String selectDetailImage(String productCode) {
    String imageName = session.selectOne(NS + "selectDetailImage", productCode);
    return imageName;
  }

}
