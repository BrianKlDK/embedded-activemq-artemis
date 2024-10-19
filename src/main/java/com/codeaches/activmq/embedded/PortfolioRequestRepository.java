package com.codeaches.activmq.embedded;

import org.springframework.data.repository.CrudRepository;

import com.codeaches.activmq.embedded.PortfolioRequest;

// This will be AUTO IMPLEMENTED by Spring into a Bean called portfolioRequestRepository
// CRUD refers Create, Read, Update, Delete

public interface PortfolioRequestRepository extends CrudRepository<PortfolioRequest, Integer> {
    
}