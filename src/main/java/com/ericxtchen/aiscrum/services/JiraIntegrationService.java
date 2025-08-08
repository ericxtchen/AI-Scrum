package com.ericxtchen.aiscrum.services;

import com.ericxtchen.aiscrum.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JiraIntegrationService {
    @Autowired
    private UserRepository userRepository;
}
