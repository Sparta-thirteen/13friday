package com.sparta.eureka.client.auth.common.util.page;

import com.sparta.eureka.client.auth.common.exception.ApiBusinessException;
import com.sparta.eureka.client.auth.common.exception.codes.GlobalExceptionCode;
import org.springframework.data.domain.Pageable;

public class PageableUtils {
  private static final int FIRST_PAGE_NUMBER = 1;

  public static Pageable validatePageable(Pageable pageable) {
    validatePageNumber(pageable.getPageNumber());
    validatePageSize(pageable.getPageSize());

    return pageable.previousOrFirst();
  }

  private static void validatePageSize(int pageSize) {
    if(!PageSize.isValidSize(pageSize)){
      throw new ApiBusinessException(GlobalExceptionCode.INVALID_PAGE_SIZE);

    }
  }

  private static void validatePageNumber(int pageNumber) {
    if(pageNumber < FIRST_PAGE_NUMBER) {
      throw new ApiBusinessException(GlobalExceptionCode.INVALID_PAGE_NUMBER);
    }
  }

}
