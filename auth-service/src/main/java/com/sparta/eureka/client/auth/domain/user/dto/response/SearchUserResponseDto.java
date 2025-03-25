package com.sparta.eureka.client.auth.domain.user.dto.response;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class SearchUserResponseDto {

  private List<ReadResponseDto> content;
  private PageInfoDto pageInfo;
}
