package com.specificgroup.crud_app.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UpdateRequest {
    String id;
    String name;
    String age;
    String specialization;
    String phone;
    String email;
}
