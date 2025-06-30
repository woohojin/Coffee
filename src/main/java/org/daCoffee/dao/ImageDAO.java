package org.daCoffee.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.daCoffee.dto.ImageDTO;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ImageDAO {
  private final SqlSessionTemplate session;

  @Autowired
  public ImageDAO(SqlSessionTemplate session) {
    this.session = session;
  }

  private final static String NS = "org.daCoffee.dao.ImageDAO.";
  private static Map map = new HashMap<>();

  public int insertProductImage(ImageDTO imageDTO) {
    int num = session.insert(NS + "insertProductImage", imageDTO);
    return num;
  }

  public int updateProductImage(ImageDTO imageDTO) {
    int num = session.update(NS + "updateProductImage", imageDTO);
    return num;
  }

  public List<ImageDTO> selectProductImage(String productCode) {
    List<ImageDTO> list = session.selectList(NS + "selectProductImage", productCode);
    return list;
  }

  public String selectDetailImage(String productCode) {
    String imageName = session.selectOne(NS + "selectDetailImage", productCode);
    return imageName;
  }

}
