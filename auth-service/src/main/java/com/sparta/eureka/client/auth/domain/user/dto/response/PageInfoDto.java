package com.sparta.eureka.client.auth.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
public class PageInfoDto {

  private int page;
  private int size;
  private long totalElements;
  private int totalPages;
}
