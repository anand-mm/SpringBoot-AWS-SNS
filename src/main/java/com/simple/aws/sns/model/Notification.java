package com.simple.aws.sns.model;

import lombok.Data;

@Data
public class Notification {

    private String subject;
    private String message;
}
