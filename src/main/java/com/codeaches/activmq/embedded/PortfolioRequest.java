package com.codeaches.activmq.embedded;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity // This tells Hibernate to make a table out of this class
public class PortfolioRequest {
  @Id
  private Integer id;
  private String payload;
  private String status;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getPayload() {
    return payload;
  }

  public void setPayload(String payload) {
    this.payload = payload;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
