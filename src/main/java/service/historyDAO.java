package service;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import model.History;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class historyDAO {

  @Autowired
  SqlSessionTemplate session;

  private final static String NS = "history.";
  private static Map map = new HashMap<>();

  public int historyInsert(History history) {
    int num = session.insert(NS+"historyInsert", history);
    return num;
  }

  public int historyCount(String memberId) {
    int num = session.selectOne(NS + "historyCount", memberId);
    return num;
  }

  public List<History> historySelectMember(String memberId) {
    List<History> list = session.selectList(NS + "historySelectMember", memberId);
    return list;
  }

  public History historySelectOne(String memberId, String historyCode) {
    map.clear();
    map.put("memberId", memberId);
    map.put("historyCode", historyCode);
    History history = session.selectOne(NS + "historySelectOne", map);

    return history;
  }

}
