package org.daCoffee.service;

import lombok.RequiredArgsConstructor;
import org.daCoffee.dto.request.MemberUpdateRequestDTO;
import org.daCoffee.dto.request.admin.MemberRequestDTO;
import org.daCoffee.entity.Member;
import org.daCoffee.entity.MemberTier;
import org.daCoffee.entity.MemberWithdrawal;
import org.daCoffee.repository.CartRepository;
import org.daCoffee.repository.MemberRepository;
import org.daCoffee.repository.MemberWithdrawalRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
  private final MemberRepository memberRepository;
  private final MemberWithdrawalRepository memberWithdrawalRepository;
  private final CartRepository cartRepository;

  // DB 컬럼명 → Entity 필드명 변환
  private String resolveColumn(String column) {
    return switch (column) {
      case "member_tier" -> "memberTier";  // DB컬럼 → Entity 필드
      case "member_id"   -> "memberId";
      case "member_date" -> "memberDate";
      default            -> "memberTier";
    };
  }

  // 회원 단건 조회
  @Transactional(readOnly = true)
  public Optional<Member> findById(String memberId) {
    return memberRepository.findById(memberId);
  }

  // 회원 존재 여부 확인
  @Transactional(readOnly = true)
  public boolean existsById(String memberId) {
    return memberRepository.existsById(memberId);
  }

  // 회원 비활성화 여부 확인
  @Transactional(readOnly = true)
  public boolean isDisabled(String memberId) {
    return memberRepository.countDisabledMember(memberId) > 0;
  }

  // 회원 저장 (가입/수정)
  @Transactional
  public void save(Member member) {
    memberRepository.save(member);
  }

  // 회원 삭제
  @Transactional
  public void delete(String memberId) {
    memberRepository.deleteById(memberId);
  }

  // 아이디 찾기
  @Transactional(readOnly = true)
  public List<String> findMemberIdByNameAndEmail(String memberName, String memberEmail) {
    return memberRepository.findMemberIdByNameAndEmail(memberName, memberEmail);
  }

  // 비밀번호 찾기
  @Transactional(readOnly = true)
  public Optional<String> findPasswordByMemberIdAndEmail(String memberId, String memberEmail) {
    return memberRepository.findPasswordByMemberIdAndEmail(memberId, memberEmail);
  }

  // 임시 비밀번호 업데이트
  @Transactional
  public void updatePassword(String memberId, String encodedPassword) {
    memberRepository.updateMemberPassword(memberId, encodedPassword);
  }

  // 회원 탈퇴
  @Transactional
  public void withdrawMember(String memberId) {
    Member member = memberRepository.findById(memberId)
      .orElseThrow(() -> new IllegalArgumentException("회원 없음: " + memberId));

    MemberWithdrawal withdrawal = MemberWithdrawal.builder()
      .memberId(member.getMemberId())
      .memberCompanyName(member.getMemberCompanyName())
      .memberTel(member.getMemberTel())
      .memberCompanyTel(member.getMemberCompanyTel())
      .memberWithdrawalDate(LocalDate.now())
      .build();

    memberWithdrawalRepository.save(withdrawal);
    cartRepository.deleteAllByMemberId(memberId);
    memberRepository.deleteById(memberId);
  }

  // 회원 정보 수정 (비밀번호 포함)
  @Transactional
  public void update(String memberId, String encodedPassword, MemberUpdateRequestDTO dto) {
    Member member = memberRepository.findById(memberId)
      .orElseThrow(() -> new IllegalArgumentException("회원 없음: " + memberId));
    member.update(dto.getMemberName(), encodedPassword, dto.getMemberTel(),
      dto.getMemberCompanyTel(), dto.getMemberAddress(), dto.getMemberDetailAddress(),
      dto.getMemberDeliveryAddress(), dto.getMemberDetailDeliveryAddress(),
      dto.getMemberEmail(), dto.getMemberFile());
  }

  // 회원 정보 수정 (비밀번호 제외)
  @Transactional
  public void updateWithoutPassword(String memberId, MemberUpdateRequestDTO dto) {
    Member member = memberRepository.findById(memberId)
      .orElseThrow(() -> new IllegalArgumentException("회원 없음: " + memberId));
    member.updateWithoutPassword(dto.getMemberName(), dto.getMemberTel(),
      dto.getMemberCompanyTel(), dto.getMemberAddress(), dto.getMemberDetailAddress(),
      dto.getMemberDeliveryAddress(), dto.getMemberDetailDeliveryAddress(),
      dto.getMemberEmail(), dto.getMemberFile());
  }

  // ===================== Admin =====================

  // 등급 업데이트
  @Transactional
  public void updateMemberTier(String memberId, int memberTier) {
    memberRepository.updateMemberTier(memberId, MemberTier.fromCode(memberTier));
  }

  // 사업자 코드 업데이트
  @Transactional
  public void updateMemberFranCode(String memberId, String memberFranCode) {
    memberRepository.updateMemberFranCode(memberId, memberFranCode);
  }

  // 회원 비활성화
  @Transactional
  public void disableMember(String memberId) {
    memberRepository.updateDisabledStatus(memberId, true);
  }

  // 회원 활성화
  @Transactional
  public void enableMember(String memberId) {
    memberRepository.updateDisabledStatus(memberId, false);
  }

  // 전체 회원 목록 페이징 + 정렬
  @Transactional(readOnly = true)
  public Page<Member> findAllPaged(int pageInt, int limit, String column, String order) {
    Sort sort = Sort.by(
      "desc".equals(order) ? Sort.Direction.DESC : Sort.Direction.ASC,
      resolveColumn(column)
    );
    PageRequest pageable = PageRequest.of(pageInt - 1, limit, sort);
    return memberRepository.findAll(pageable);
  }

  // 비활성화 회원 목록 페이징
  @Transactional(readOnly = true)
  public Page<Member> findDisabledMembers(int pageInt, int limit) {
    PageRequest pageable = PageRequest.of(pageInt - 1, limit);
    return memberRepository.findDisabledMembers(pageable);
  }

  // 탈퇴 회원 목록 페이징
  @Transactional(readOnly = true)
  public Page<MemberWithdrawal> findWithdrawalMembers(int pageInt, int limit) {
    PageRequest pageable = PageRequest.of(pageInt - 1, limit);
    return memberWithdrawalRepository.findAll(pageable);
  }

  // 회원 검색 (입력된 필드 전부 AND 조합)
  // memberName/memberTel은 DB에 암호화되어 저장되어 있어 SQL LIKE로 부분일치가 불가능하므로,
  // 암호화 안 된 필드로만 DB에서 1차로 거른 뒤 복호화된 값으로 메모리에서 필터링한다.
  @Transactional(readOnly = true)
  public Page<Member> searchMembers(String memberCompanyName, String memberFranCode, String memberId,
                                     String memberName, String memberTel, String memberCompanyTel,
                                     String memberTier, int pageInt, int limit) {
    Specification<Member> spec = Specification.where(null);
    if (memberCompanyName != null && !memberCompanyName.isBlank()) {
      spec = spec.and((root, query, cb) ->
              cb.like(root.get("memberCompanyName"), "%" + memberCompanyName + "%"));
    }
    if (memberFranCode != null && !memberFranCode.isBlank()) {
      spec = spec.and((root, query, cb) ->
              cb.like(root.get("memberFranCode"), "%" + memberFranCode + "%"));
    }
    if (memberId != null && !memberId.isBlank()) {
      spec = spec.and((root, query, cb) ->
              cb.like(root.get("memberId"), "%" + memberId + "%"));
    }
    if (memberCompanyTel != null && !memberCompanyTel.isBlank()) {
      spec = spec.and((root, query, cb) ->
              cb.like(root.get("memberCompanyTel"), "%" + memberCompanyTel + "%"));
    }
    if (memberTier != null && !memberTier.isBlank()) {
      spec = spec.and((root, query, cb) ->
              cb.equal(root.get("memberTier"), MemberTier.fromCode(Integer.parseInt(memberTier))));
    }

    boolean hasEncryptedFilter = (memberName != null && !memberName.isBlank())
            || (memberTel != null && !memberTel.isBlank());

    if (!hasEncryptedFilter) {
      return memberRepository.findAll(spec, PageRequest.of(pageInt - 1, limit));
    }

    List<Member> filtered = memberRepository.findAll(spec).stream()
            .filter(m -> memberName == null || memberName.isBlank()
                    || (m.getMemberName() != null && m.getMemberName().contains(memberName)))
            .filter(m -> memberTel == null || memberTel.isBlank()
                    || (m.getMemberTel() != null && m.getMemberTel().contains(memberTel)))
            .toList();

    int start = Math.min((pageInt - 1) * limit, filtered.size());
    int end = Math.min(start + limit, filtered.size());

    return new PageImpl<>(filtered.subList(start, end), PageRequest.of(pageInt - 1, limit), filtered.size());
  }

  // 회원 정보 수정
  @Transactional
  public void adminUpdate(String adminName, MemberRequestDTO dto) {
    Member member = memberRepository.findById(dto.getMemberId())
      .orElseThrow(() -> new IllegalArgumentException("회원 없음: " + dto.getMemberId()));

    member.adminUpdate(dto.getMemberName(), dto.getMemberCompanyName(), dto.getMemberTel(),
      dto.getMemberCompanyTel(), dto.getMemberAddress(), dto.getMemberDetailAddress(),
      dto.getMemberDeliveryAddress(), dto.getMemberDetailDeliveryAddress(),
      dto.getMemberEmail(), dto.getMemberFranCode(), MemberTier.fromCode(dto.getMemberTier()), adminName);
  }

  // 회원 활성화/비활성화 토글
  @Transactional
  public void toggleDisable(String memberId) {
    Member member = memberRepository.findById(memberId)
      .orElseThrow(() -> new IllegalArgumentException("회원 없음: " + memberId));

    if (!member.isMemberDisabledStatus()) {
      // 비활성화
      member.disable();
      cartRepository.deleteAllByMemberId(memberId);
    } else {
      // 활성화
      member.enable();
    }
  }
}
