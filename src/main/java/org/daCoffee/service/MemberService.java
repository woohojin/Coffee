package org.daCoffee.service;

import lombok.RequiredArgsConstructor;
import org.daCoffee.dto.request.MemberUpdateRequestDTO;
import org.daCoffee.dto.request.admin.MemberRequestDTO;
import org.daCoffee.entity.Member;
import org.daCoffee.entity.MemberWithdrawal;
import org.daCoffee.repository.CartRepository;
import org.daCoffee.repository.MemberRepository;
import org.daCoffee.repository.MemberWithdrawalRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    memberRepository.updateMemberTier(memberId, memberTier);
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

  // 회원 검색 (컬럼 + 키워드 기준)
  @Transactional(readOnly = true)
  public Page<Member> searchMembers(String column, String keyword, int pageInt, int limit) {
    PageRequest pageable = PageRequest.of(pageInt - 1, limit);
    return switch (column) {
      case "memberCompanyName" -> memberRepository.findByMemberCompanyNameContaining(keyword, pageable);
      case "memberId"          -> memberRepository.findByMemberIdContaining(keyword, pageable);
      case "memberName"        -> memberRepository.findByMemberNameContaining(keyword, pageable);
      case "memberTel"         -> memberRepository.findByMemberTelContaining(keyword, pageable);
      case "memberCompanyTel"  -> memberRepository.findByMemberCompanyTelContaining(keyword, pageable);
      case "memberTier"        -> memberRepository.findByMemberTier(Integer.parseInt(keyword), pageable);
      default                  -> memberRepository.findAll(pageable);
    };
  }

  // 회원 정보 수정
  @Transactional
  public void adminUpdate(String adminName, MemberRequestDTO dto) {
    Member member = memberRepository.findById(dto.getMemberId())
      .orElseThrow(() -> new IllegalArgumentException("회원 없음: " + dto.getMemberId()));

    member.adminUpdate(dto.getMemberName(), dto.getMemberCompanyName(), dto.getMemberTel(),
      dto.getMemberCompanyTel(), dto.getMemberAddress(), dto.getMemberDetailAddress(),
      dto.getMemberDeliveryAddress(), dto.getMemberDetailDeliveryAddress(),
      dto.getMemberEmail(), dto.getMemberFranCode(), dto.getMemberTier(), adminName);
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
