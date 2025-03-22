package com.sparta.eureka.client.auth.domain.shippingmanager.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchShippingManagerResponseDto {
  private List<ReadResponseDto> content;
  private PageInfoDto pageInfo;
}
