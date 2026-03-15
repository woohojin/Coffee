package org.daCoffee.service;

import lombok.RequiredArgsConstructor;
import org.daCoffee.dto.request.admin.OrderHistoryRequestDTO;
import org.daCoffee.entity.OrderHistory;
import org.daCoffee.repository.OrderHistoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderHistoryService {

  private final OrderHistoryRepository orderHistoryRepository;

  // 컬럼명 → Entity 필드명 변환
  private String resolveColumn(String column) {
    return switch (column) {
      case "order_id"       -> "orderId";
      case "member_id"      -> "memberId";
      case "product_code"   -> "productCode";
      case "product_name"   -> "productName";
      case "product_unit"   -> "productUnit";
      case "product_price"  -> "productPrice";
      case "quantity"       -> "quantity";
      case "deliveryAddress"-> "deliveryAddress";
      case "order_date"     -> "orderDate";
      default               -> "orderNum";
    };
  }

  // 주문 저장
  @Transactional
  public void save(OrderHistory orderHistory) {
    orderHistoryRepository.save(orderHistory);
  }

  // 특정 회원의 주문 목록 조회
  @Transactional(readOnly = true)
  public List<OrderHistory> findByMemberId(String memberId) {
    return orderHistoryRepository.findByMemberIdOrderByOrderDateDesc(memberId);
  }

  // 특정 회원의 날짜 범위 주문 목록
  @Transactional(readOnly = true)
  public List<OrderHistory> findByMemberIdBetween(String memberId, LocalDateTime start, LocalDateTime end) {
    return orderHistoryRepository.findByMemberIdAndOrderDateBetween(memberId, start, end);
  }

  // 단건 조회
  @Transactional(readOnly = true)
  public Optional<OrderHistory> findByOrderIdAndProductCode(String orderId, String productCode) {
    return orderHistoryRepository.findByOrderIdAndProductCode(orderId, productCode);
  }

  // ===================== Admin =====================

  // 전체 주문 목록 페이징 + 정렬
  @Transactional(readOnly = true)
  public Page<OrderHistory> findAllPaged(int pageInt, int limit, String column, String order) {
    Sort sort = Sort.by(
      "desc".equals(order) ? Sort.Direction.DESC : Sort.Direction.ASC,
      resolveColumn(column)
    );
    PageRequest pageable = PageRequest.of(pageInt - 1, limit, sort);
    return orderHistoryRepository.findAll(pageable);
  }

  // 주문 검색 (조건 조합)
  @Transactional(readOnly = true)
  public Page<OrderHistory> searchOrders(String orderId, String memberId,
                                         LocalDateTime startDate, LocalDateTime endDate,
                                         int pageInt, int limit) {
    PageRequest pageable = PageRequest.of(pageInt - 1, limit);
    boolean hasOrderId  = orderId != null && !orderId.isEmpty();
    boolean hasMemberId = memberId != null && !memberId.isEmpty();
    boolean hasDate     = startDate != null && endDate != null;

    if (hasOrderId && hasMemberId && hasDate) {
      return orderHistoryRepository.findByOrderIdContainingAndMemberIdContainingAndOrderDateBetween(
        orderId, memberId, startDate, endDate, pageable);
    } else if (hasOrderId && hasMemberId) {
      return orderHistoryRepository.findByOrderIdContainingAndMemberIdContaining(orderId, memberId, pageable);
    } else if (hasOrderId && hasDate) {
      return orderHistoryRepository.findByOrderIdContainingAndOrderDateBetween(orderId, startDate, endDate, pageable);
    } else if (hasMemberId && hasDate) {
      return orderHistoryRepository.findByMemberIdContainingAndOrderDateBetween(memberId, startDate, endDate, pageable);
    } else if (hasOrderId) {
      return orderHistoryRepository.findByOrderIdContaining(orderId, pageable);
    } else if (hasMemberId) {
      return orderHistoryRepository.findByMemberIdContaining(memberId, pageable);
    } else if (hasDate) {
      return orderHistoryRepository.findByOrderDateBetween(startDate, endDate, pageable);
    }
    return orderHistoryRepository.findAll(pageable);
  }

  // 주문 수정
  @Transactional
  public boolean adminUpdate(String adminName, OrderHistoryRequestDTO dto) {
    Optional<OrderHistory> result = orderHistoryRepository.findByOrderIdAndProductCode(dto.getOrderId(), dto.getProductCode());
    if (result.isEmpty()) return false;

    result.get().adminUpdate(dto.getMemberFranCode(), dto.getMemberName(), dto.getMemberCompanyName(),
      dto.getDeliveryAddress(), dto.getDetailDeliveryAddress(), dto.getDeliveryCode(), adminName);
    return true;
  }

  // 주문 삭제
  @Transactional
  public boolean adminDelete(String orderId, String productCode) {
    int deleted = orderHistoryRepository.deleteByOrderIdAndProductCode(orderId, productCode);
    return deleted > 0;
  }
}
