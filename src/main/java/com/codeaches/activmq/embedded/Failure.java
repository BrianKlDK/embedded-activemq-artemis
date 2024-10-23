package com.codeaches.activmq.embedded;

import lombok.Data;

@Data
public class Failure implements Type {
    private final String msg;
}