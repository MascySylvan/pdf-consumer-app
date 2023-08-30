package com.javatechie.aws.lambda;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.javatechie.aws.lambda.domain.NormalReponse;
import com.javatechie.aws.lambda.domain.Order;
import com.javatechie.aws.lambda.respository.OrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.Base64;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.json.JSONObject;

@SpringBootApplication
public class SpringbootAwsLambdaApplication {

    @Autowired
    private OrderDao orderDao;

    @Bean
    public Supplier<List<Order>> orders() {
        return () -> orderDao.buildOrders();
    }
    
    @Bean
    public Supplier<JSONObject> getFile() throws Exception {
    	ByteArrayOutputStream b = new ByteArrayOutputStream();
    	String data = "This is a line of text inside the string.";
        byte[] buf = data.getBytes();
        b.write(buf);
        b.close();
        
        JSONObject obj = new JSONObject();

        obj.put("data", Base64.getEncoder().encode(b.toByteArray()));
        obj.put("message", "Sample Message 123");
        
//        NormalReponse res = new NormalReponse();
//        res.setData(b.toByteArray());
//        res.setMessage("Sample Message 123");
        
        return () -> obj;
    }
    
    @Bean
    public ResponseEntity<Object> downloadFile() throws IOException {
    	String text = "sampleeeee";
    	
    	ByteArrayInputStream inputStream = new ByteArrayInputStream(text.getBytes());
        HttpHeaders headers = new HttpHeaders();
        
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", "Sample.txt"));
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        
        ResponseEntity<Object> 
        responseEntity = ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType("application/txt")).body(inputStream);
        
        return responseEntity;
    }

    @Bean
    public Function<APIGatewayProxyRequestEvent, List<Order>> findOrderByName() {
        return (requestEvent) -> orderDao.buildOrders()
        		.stream()
        		.filter(order -> order.getName().equals(requestEvent.getQueryStringParameters().get("orderName")))
        		.collect(Collectors.toList());
    }


    public static void main(String[] args) {
        SpringApplication.run(SpringbootAwsLambdaApplication.class, args);
    }

}
