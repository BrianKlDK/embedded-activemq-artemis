package com.codeaches.activmq.embedded;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
@Getter
public class Failure implements Type {
    private String responseText;
}