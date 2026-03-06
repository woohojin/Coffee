package org.daCoffee.dao;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.daCoffee.dto.ImageDTO;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImageDAO {
  private final SqlSessionTemplate session;

  private final static String NS = "org.daCoffee.dao.ImageDAO.";

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
