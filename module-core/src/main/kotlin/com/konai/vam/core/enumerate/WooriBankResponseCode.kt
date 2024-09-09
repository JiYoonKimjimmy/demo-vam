package com.konai.vam.core.enumerate

import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.ErrorCode.*

enum class WooriBankResponseCode(val message: String) {
    `0000`("정상"),
    K111("제휴기관 업무개시 이전"),
    K118("제휴기관 업무종료"),
    K119("제휴기관 입금가능 시간 아님"),
    K114("마감 중 잠시후 거래요망"),
    K115("전문 전송일자 오류"),
    K116("제휴기관 SYSTEM 장애"),
    K401("전문 FORMAT 오류"),
    K402("가상계좌 발견되지 않음"),
    K404("사고신고 계좌"),
    K405("거래중지 계좌"),
    K406("법적제한 계좌"),
    K407("잔액증명서 발급(당일중 입금불가)"),
    K408("입금전 취소전문 수신"),
    K409("금액 오류"),
    K410("입금금액 상이 (1회 불입단위가 있는 경우)"),
    K411("입금회수 초과 (해당 개설점 연락)"),
    K412("입금한도 초과"),
    K413("수표입금 불가"),
    K414("납부할 내역 없음"),
    K415("기납부 처리됨 (과거 납부완료건)"),
    K416("처리중 장애발생(DB 에러) 잠시후 거래요망"),
    K417("의뢰인 성명 오류"),
    K418("수취인 성명 오류"),
    K419("기타 수취 불가"),
    K999("입금불가 제휴기관 문의요망"),
    K701("원거래 없음(취소시)"),
    K702("원거래 계좌번호 상위(취소시)"),
    K703("원거래 금액 상위(취소시)"),
    K704("원거래 기타사항 상위(취소시)"),
    K705("잔액부족(취소시)"),
    K706("원거래 비정상처리(취소시)"),
    K777("취소불가 제휴기관 문의요망(취소시)"),
    ;

    companion object {
        fun of(errorCode: ErrorCode): WooriBankResponseCode {
            return when (errorCode) {
                VIRTUAL_ACCOUNT_BANK_NOT_FOUND -> K116
                VIRTUAL_ACCOUNT_RECHARGE_FAILED -> K116
                WOORI_BANK_MESSAGE_CODE_INVALID -> K401
                VIRTUAL_ACCOUNT_NOT_FOUND -> K402
                RECHARGE_CARD_STATUS_IS_NOT_ACTIVE -> K402
                RECHARGE_TRANSACTION_ALREADY_CANCELED -> K408
                VIRTUAL_ACCOUNT_HAS_NOT_CONNECTED_CARD -> K419
                RECHARGE_TRANSACTION_NOT_FOUND -> K701
                VIRTUAL_ACCOUNT_RECHARGE_CANCEL_FAILED -> K777
                RECHARGE_AMOUNT_EXCEEDED -> K412
                else -> K999
            }
        }
    }
}