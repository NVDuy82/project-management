package com.example.reportservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ReportProjectResponse {
    BasicInformationProjectResponse basic;
    ManagementInformationProjectResponse management;
    QuantityInformationProjectResponse quantity;
}
