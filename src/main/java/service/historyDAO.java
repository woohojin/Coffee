package service;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import model.History;
import model.Product;
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
    int num = session.insert(NS + "historyInsert", history);
    return num;
  }

  public void rownumSet() {
    session.selectOne(NS + "rownumSet");
  }

  public int historyCount() {
    int num = session.selectOne(NS + "historyCount");
    return num;
  }

  public int historyCountBetween(String memberId, String startDate, String endDate) {
    map.clear();
    map.put("memberId", memberId);
    map.put("startDate", startDate);
    map.put("endDate", endDate);
    int num = session.selectOne(NS + "historyCountBetween", map);
    return num;
  }

  public List<History> historyList(int pageInt, int limit) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    List<History> list = session.selectList(NS + "historyList", map);
    return list;
  }

  public List<History> historyListAll() {
    List<History> list = session.selectList(NS + "historyListAll");
    return list;
  }

  public List<History> historyListByHistoryCode(int pageInt, int limit) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    List<History> list = session.selectList(NS + "historyListByHistoryCode", map);
    return list;
  }

  public List<History> historyListDescByHistoryCode(int pageInt, int limit) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    List<History> list = session.selectList(NS + "historyListDescByHistoryCode", map);
    return list;
  }

  public List<History> historyListByMemberId(int pageInt, int limit) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    List<History> list = session.selectList(NS + "historyListByMemberId", map);
    return list;
  }

  public List<History> historyListDescByMemberId(int pageInt, int limit) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    List<History> list = session.selectList(NS + "historyListDescByMemberId", map);
    return list;
  }

  public List<History> historyListByProductCode(int pageInt, int limit) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    List<History> list = session.selectList(NS + "historyListByProductCode", map);
    return list;
  }

  public List<History> historyListDescByProductCode(int pageInt, int limit) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    List<History> list = session.selectList(NS + "historyListDescByProductCode", map);
    return list;
  }

  public List<History> historyListByProductName(int pageInt, int limit) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    List<History> list = session.selectList(NS + "historyListByProductName", map);
    return list;
  }

  public List<History> historyListDescByProductName(int pageInt, int limit) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    List<History> list = session.selectList(NS + "historyListDescByProductName", map);
    return list;
  }

  public List<History> historyListByProductUnit(int pageInt, int limit) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    List<History> list = session.selectList(NS + "historyListByProductUnit", map);
    return list;
  }

  public List<History> historyListDescByProductUnit(int pageInt, int limit) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    List<History> list = session.selectList(NS + "historyListDescByProductUnit", map);
    return list;
  }

  public List<History> historyListByProductPrice(int pageInt, int limit) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    List<History> list = session.selectList(NS + "historyListByProductPrice", map);
    return list;
  }

  public List<History> historyListDescByProductPrice(int pageInt, int limit) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    List<History> list = session.selectList(NS + "historyListDescByProductPrice", map);
    return list;
  }

  public List<History> historyListByQuantity(int pageInt, int limit) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    List<History> list = session.selectList(NS + "historyListByQuantity", map);
    return list;
  }

  public List<History> historyListDescByQuantity(int pageInt, int limit) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    List<History> list = session.selectList(NS + "historyListDescByQuantity", map);
    return list;
  }

  public List<History> historyListByDeliveryAddress(int pageInt, int limit) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    List<History> list = session.selectList(NS + "historyListByDeliveryAddress", map);
    return list;
  }

  public List<History> historyListDescByDeliveryAddress(int pageInt, int limit) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    List<History> list = session.selectList(NS + "historyListDescByDeliveryAddress", map);
    return list;
  }

  public List<History> historyListByOrderDate(int pageInt, int limit) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    List<History> list = session.selectList(NS + "historyListByOrderDate", map);
    return list;
  }

  public List<History> historyListDescByOrderDate(int pageInt, int limit) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    List<History> list = session.selectList(NS + "historyListDescByOrderDate", map);
    return list;
  }

  public List<History> historySearchListByOrderDate(int pageInt, int limit, String startDate, String endDate) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    map.put("startDate", startDate);
    map.put("endDate", endDate);
    List<History> list = session.selectList(NS + "historySearchListByOrderDate", map);
    return list;
  }

  public List<History> historySearchListByHistoryCode(int pageInt, int limit, String searchText) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    map.put("searchText", searchText);
    List<History> list = session.selectList(NS + "historySearchListByHistoryCode", map);
    return list;
  }

  public List<History> historySearchListByMemberId(int pageInt, int limit, String searchText) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    map.put("searchText", searchText);
    List<History> list = session.selectList(NS + "historySearchListByMemberId", map);
    return list;
  }

  public List<History> historySearchListByHistoryCodeAndMemberId(int pageInt, int limit, String searchText, String searchText1) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    map.put("searchText", searchText);
    map.put("searchText1", searchText1);
    List<History> list = session.selectList(NS + "historySearchListByHistoryCodeAndMemberId", map);
    return list;
  }

  public List<History> historySearchListByHistoryCodeWithOrderDate(int pageInt, int limit, String searchText, String startDate, String endDate) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    map.put("searchText", searchText);
    map.put("startDate", startDate);
    map.put("endDate", endDate);
    List<History> list = session.selectList(NS + "historySearchListByHistoryCodeWithOrderDate", map);
    return list;
  }

  public List<History> historySearchListByMemberIdWithOrderDate(int pageInt, int limit, String searchText, String startDate, String endDate) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    map.put("searchText", searchText);
    map.put("startDate", startDate);
    map.put("endDate", endDate);
    List<History> list = session.selectList(NS + "historySearchListByMemberIdWithOrderDate", map);
    return list;
  }

  public List<History> historySearchListByHistoryCodeAndMemberIdWithOrderDate(int pageInt, int limit, String searchText, String searchText1, String startDate, String endDate) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    map.put("searchText", searchText);
    map.put("searchText1", searchText1);
    map.put("startDate", startDate);
    map.put("endDate", endDate);
    List<History> list = session.selectList(NS + "historySearchListByHistoryCodeAndMemberIdWithOrderDate", map);
    return list;
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

  public List<History> historySelectBetween(String memberId, String startDate, String endDate) {
    map.clear();
    map.put("memberId", memberId);
    map.put("startDate", startDate);
    map.put("endDate", endDate);
    List<History> list = session.selectList(NS + "historySelectBetween", map);
    return list;
  }

}
