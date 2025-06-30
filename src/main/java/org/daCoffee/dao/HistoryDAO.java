package org.daCoffee.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.daCoffee.dto.HistoryDTO;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HistoryDAO {
  private final SqlSessionTemplate session;

  @Autowired
  public HistoryDAO(SqlSessionTemplate session) {
    this.session = session;
  }

  private final static String NS = "org.daCoffee.dao.HistoryDAO.";
  private static Map map = new HashMap<>();

  public int historyInsert(HistoryDTO historyDTO) {
    int num = session.insert(NS + "historyInsert", historyDTO);
    return num;
  }

  public int historyFranCodeUpdate(String existMemberFranCode, String memberFranCode) {
    map.clear();
    map.put("existMemberFranCode", existMemberFranCode);
    map.put("memberFranCode", memberFranCode);
    int num = session.update(NS + "historyFranCodeUpdate", map);
    return num;
  }

  public int historyUpdate(HistoryDTO historyDTO) {
    int num = session.update(NS + "historyUpdate", historyDTO);
    return num;
  }

  public int historyDelete(String orderId, String productCode) {
    map.clear();
    map.put("orderId", orderId);
    map.put("productCode", productCode);
    int num = session.delete(NS + "historyDelete", map);
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

  public List<HistoryDTO> historyList(int pageInt, int limit) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    List<HistoryDTO> list = session.selectList(NS + "historyList", map);
    return list;
  }

  public List<HistoryDTO> historyListAll() {
    List<HistoryDTO> list = session.selectList(NS + "historyListAll");
    return list;
  }

  public List<HistoryDTO> historyListByHistoryCode(int pageInt, int limit) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    List<HistoryDTO> list = session.selectList(NS + "historyListByHistoryCode", map);
    return list;
  }

  public List<HistoryDTO> historyListDescByHistoryCode(int pageInt, int limit) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    List<HistoryDTO> list = session.selectList(NS + "historyListDescByHistoryCode", map);
    return list;
  }

  public List<HistoryDTO> historyListByMemberId(int pageInt, int limit) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    List<HistoryDTO> list = session.selectList(NS + "historyListByMemberId", map);
    return list;
  }

  public List<HistoryDTO> historyListDescByMemberId(int pageInt, int limit) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    List<HistoryDTO> list = session.selectList(NS + "historyListDescByMemberId", map);
    return list;
  }

  public List<HistoryDTO> historyListByProductCode(int pageInt, int limit) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    List<HistoryDTO> list = session.selectList(NS + "historyListByProductCode", map);
    return list;
  }

  public List<HistoryDTO> historyListDescByProductCode(int pageInt, int limit) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    List<HistoryDTO> list = session.selectList(NS + "historyListDescByProductCode", map);
    return list;
  }

  public List<HistoryDTO> historyListByProductName(int pageInt, int limit) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    List<HistoryDTO> list = session.selectList(NS + "historyListByProductName", map);
    return list;
  }

  public List<HistoryDTO> historyListDescByProductName(int pageInt, int limit) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    List<HistoryDTO> list = session.selectList(NS + "historyListDescByProductName", map);
    return list;
  }

  public List<HistoryDTO> historyListByProductUnit(int pageInt, int limit) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    List<HistoryDTO> list = session.selectList(NS + "historyListByProductUnit", map);
    return list;
  }

  public List<HistoryDTO> historyListDescByProductUnit(int pageInt, int limit) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    List<HistoryDTO> list = session.selectList(NS + "historyListDescByProductUnit", map);
    return list;
  }

  public List<HistoryDTO> historyListByProductPrice(int pageInt, int limit) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    List<HistoryDTO> list = session.selectList(NS + "historyListByProductPrice", map);
    return list;
  }

  public List<HistoryDTO> historyListDescByProductPrice(int pageInt, int limit) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    List<HistoryDTO> list = session.selectList(NS + "historyListDescByProductPrice", map);
    return list;
  }

  public List<HistoryDTO> historyListByQuantity(int pageInt, int limit) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    List<HistoryDTO> list = session.selectList(NS + "historyListByQuantity", map);
    return list;
  }

  public List<HistoryDTO> historyListDescByQuantity(int pageInt, int limit) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    List<HistoryDTO> list = session.selectList(NS + "historyListDescByQuantity", map);
    return list;
  }

  public List<HistoryDTO> historyListByDeliveryAddress(int pageInt, int limit) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    List<HistoryDTO> list = session.selectList(NS + "historyListByDeliveryAddress", map);
    return list;
  }

  public List<HistoryDTO> historyListDescByDeliveryAddress(int pageInt, int limit) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    List<HistoryDTO> list = session.selectList(NS + "historyListDescByDeliveryAddress", map);
    return list;
  }

  public List<HistoryDTO> historyListByOrderDate(int pageInt, int limit) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    List<HistoryDTO> list = session.selectList(NS + "historyListByOrderDate", map);
    return list;
  }

  public List<HistoryDTO> historyListDescByOrderDate(int pageInt, int limit) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    List<HistoryDTO> list = session.selectList(NS + "historyListDescByOrderDate", map);
    return list;
  }

  public List<HistoryDTO> historySearchListByOrderDate(int pageInt, int limit, String startDate, String endDate) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    map.put("startDate", startDate);
    map.put("endDate", endDate);
    List<HistoryDTO> list = session.selectList(NS + "historySearchListByOrderDate", map);
    return list;
  }

  public List<HistoryDTO> historySearchListByHistoryCode(int pageInt, int limit, String searchText) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    map.put("searchText", searchText);
    List<HistoryDTO> list = session.selectList(NS + "historySearchListByHistoryCode", map);
    return list;
  }

  public List<HistoryDTO> historySearchListByMemberId(int pageInt, int limit, String searchText) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    map.put("searchText", searchText);
    List<HistoryDTO> list = session.selectList(NS + "historySearchListByMemberId", map);
    return list;
  }

  public List<HistoryDTO> historySearchListByHistoryCodeAndMemberId(int pageInt, int limit, String searchText, String searchText1) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    map.put("searchText", searchText);
    map.put("searchText1", searchText1);
    List<HistoryDTO> list = session.selectList(NS + "historySearchListByHistoryCodeAndMemberId", map);
    return list;
  }

  public List<HistoryDTO> historySearchListByHistoryCodeWithOrderDate(int pageInt, int limit, String searchText, String startDate, String endDate) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    map.put("searchText", searchText);
    map.put("startDate", startDate);
    map.put("endDate", endDate);
    List<HistoryDTO> list = session.selectList(NS + "historySearchListByHistoryCodeWithOrderDate", map);
    return list;
  }

  public List<HistoryDTO> historySearchListByMemberIdWithOrderDate(int pageInt, int limit, String searchText, String startDate, String endDate) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    map.put("searchText", searchText);
    map.put("startDate", startDate);
    map.put("endDate", endDate);
    List<HistoryDTO> list = session.selectList(NS + "historySearchListByMemberIdWithOrderDate", map);
    return list;
  }

  public List<HistoryDTO> historySearchListByHistoryCodeAndMemberIdWithOrderDate(int pageInt, int limit, String searchText, String searchText1, String startDate, String endDate) {
    map.clear();
    map.put("start", (pageInt - 1) * limit + 1);
    map.put("end", (pageInt * limit));
    map.put("searchText", searchText);
    map.put("searchText1", searchText1);
    map.put("startDate", startDate);
    map.put("endDate", endDate);
    List<HistoryDTO> list = session.selectList(NS + "historySearchListByHistoryCodeAndMemberIdWithOrderDate", map);
    return list;
  }


  public List<HistoryDTO> historySelectMember(String memberId) {
    List<HistoryDTO> list = session.selectList(NS + "historySelectMember", memberId);
    return list;
  }

  public HistoryDTO historySelectOne(String orderId, String productCode) {
    map.clear();
    map.put("orderId", orderId);
    map.put("productCode", productCode);
    HistoryDTO historyDTO = session.selectOne(NS + "historySelectOne", map);
    return historyDTO;
  }

  public List<HistoryDTO> historySelectBetween(String memberId, String startDate, String endDate) {
    map.clear();
    map.put("memberId", memberId);
    map.put("startDate", startDate);
    map.put("endDate", endDate);
    List<HistoryDTO> list = session.selectList(NS + "historySelectBetween", map);
    return list;
  }

}
