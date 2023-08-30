package com.javatechie.aws.lambda;

import java.util.List;

import org.springframework.cloud.function.adapter.aws.SpringBootRequestHandler;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.javatechie.aws.lambda.domain.Order;

public class OrderHandler extends SpringBootRequestHandler<APIGatewayProxyRequestEvent,List<Order> > {
}
