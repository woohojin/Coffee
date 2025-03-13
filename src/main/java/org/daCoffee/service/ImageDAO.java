package org.daCoffee.service;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.daCoffee.model.Image;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ImageDAO {
  @Autowired
  SqlSessionTemplate session;

  private final static String NS = "org.daCoffee.service.ImageDAO.";
  private static Map map = new HashMap<>();

  public int insertProductImage(Image image) {
    int num = session.insert(NS + "insertProductImage", image);
    return num;
  }

  public int updateProductImage(Image image) {
    int num = session.update(NS + "updateProductImage", image);
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
