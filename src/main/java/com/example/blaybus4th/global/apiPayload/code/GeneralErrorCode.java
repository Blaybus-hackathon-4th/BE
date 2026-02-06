package com.example.blaybus4th.global.apiPayload.code;

import com.example.blaybus4th.global.apiPayload.dto.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GeneralErrorCode implements BaseErrorCode {

	_INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 처리 중 오류가 발생했습니다."),
	_BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400_1", "잘못된 요청입니다."),

	// 인증 관련
	INVALID_AUTHORIZATION_HEADER(HttpStatus.UNAUTHORIZED, "AUTH401_1", "올바르지 않은 Authorization 헤더입니다."),
	INVALID_TOKEN_FORMAT(HttpStatus.UNAUTHORIZED, "AUTH401_2", "토큰 형식이 올바르지 않습니다."),
	ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH401_3", "기한이 만료된 Access 토큰입니다."),
	UNSUPPORTED_PROVIDER(HttpStatus.UNAUTHORIZED, "AUTH401_4", "지원하지 않는 소셜 로그인 제공자입니다."),

    // 노트 관련
    FAIL_TO_DELETE_NOTE(HttpStatus.INTERNAL_SERVER_ERROR, "NOTE500_1", "노트 삭제에 실패했습니다."),
    FAIL_TO_CREATE_NOTE(HttpStatus.INTERNAL_SERVER_ERROR, "NOTE500_2", "노트 생성에 실패했습니다."),
    FAIL_TO_UPDATE_NOTE(HttpStatus.INTERNAL_SERVER_ERROR, "NOTE500_3", "노트 수정에 실패했습니다."),
    FAIL_TO_RETRIEVE_NOTES(HttpStatus.INTERNAL_SERVER_ERROR, "NOTE500_4", "노트 조회에 실패했습니다."),

    // 멤버
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER404_1", "존재하지 않는 회원입니다."),

    // 오브젝트
    OBJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "OBJECT404_1", "존재하지 않는 오브젝트입니다."),

    // 직렬화 오류
    JSON_PROCESSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "JSON500_1" , "JSON 직렬화/역직렬화 처리 중 오류가 발생했습니다."),;

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;

	@Override
	public ErrorReasonDTO getReason() {
		return ErrorReasonDTO.builder()
			.message(message)
			.code(code)
			.isSuccess(false)
			.build();
	}

	@Override
	public ErrorReasonDTO getReasonHttpStatus() {
		return ErrorReasonDTO.builder()
			.message(message)
			.code(code)
			.isSuccess(false)
			.httpStatus(httpStatus)
			.build();
	}
}
