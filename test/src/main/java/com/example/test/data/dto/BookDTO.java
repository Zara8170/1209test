package com.example.test.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDTO {

  private Long id;
  private String title;
  private String imgsrc;
  private Long categoryId;
  private String categoryName;
}
