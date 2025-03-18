//package com.sparta.common.utils.page;
//
//import com.sparta.common.exception.ApiBusinessException;
//import com.sparta.common.exception.codes.CommonExceptionCode;
//import org.springframework.data.domain.Pageable;
//
//public class PageableUtils {
//  private static final int FIRST_PAGE_NUMBER = 1;
//
//  public static Pageable validatePageable(Pageable pageable) {
//    validatePageNumber(pageable.getPageNumber());
//    validatePageSize(pageable.getPageSize());
//
//    return pageable.previousOrFirst();
//  }
//
//  private static void validatePageSize(int pageSize) {
//    if(!PageSize.isValidSize(pageSize)){
//      throw new ApiBusinessException(CommonExceptionCode.INVALID_PAGE_SIZE);
//
//    }
//  }
//
//  private static void validatePageNumber(int pageNumber) {
//    if(pageNumber < FIRST_PAGE_NUMBER) {
//      throw new ApiBusinessException(CommonExceptionCode.INVALID_PAGE_NUMBER);
//    }
//  }
//
//}
