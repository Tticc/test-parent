package com.tester.testerstarter.point;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.health.Status;

@Endpoint(id = "readiness")
public class ReadinessPoint {
    private HealthEndpoint health;

    public ReadinessPoint(HealthEndpoint health) {
        this.health = health;
    }

    @ReadOperation
    public Status readiness() {
        return this.health.health().getStatus();
    }

}
