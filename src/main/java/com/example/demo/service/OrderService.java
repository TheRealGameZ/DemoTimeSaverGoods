package com.example.demo.service;

import org.json.JSONObject;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import com.google.gson.Gson;

public class OrderService 
{
    private class Token
    {
        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

    public static void setCheckout()
    {
        JSONObject body = new JSONObject();
        body.put("cartId", CartService.getCart().getCartId());
        
        ResponseEntity<String> response = SalesforceService.salesforceApiCall("/checkouts", body.toString(), HttpMethod.POST);

        if(response.getStatusCode().isError())
        {
            throw new HttpClientErrorException(response.getStatusCode());
        }
    }
    
    public static void cancelCheckout()
    {
        ResponseEntity<String> response = SalesforceService.salesforceApiCall("/checkouts/active", "", HttpMethod.DELETE);

        if(response.getStatusCode().isError())
        {
            throw new HttpClientErrorException(response.getStatusCode());
        }
    }
    
    public static String setAddress(String body)
    {
        String response = SalesforceService.patchRequest("/checkouts/active", body);

        if(response != null)
        {
            System.out.println("SET ADDRESS IS OKAY");
            return response;
        } else {
            return null;
        }
    }
    
    public static String getPaymentToken(String body)
    {
        ResponseEntity<String> response = SalesforceService.salesforceApiCall("/payments/token/", body, HttpMethod.POST);

        if(response.getStatusCode().isError())
        {
            throw new HttpClientErrorException(response.getStatusCode());
        }
        else
        {
            Gson gson = new Gson();
            return gson.fromJson(response.getBody(),Token.class).getToken();
        }
    }
    
    public static void setPayment(String body)
    {
        ResponseEntity<String> response = SalesforceService.salesforceApiCall("/checkouts/active/payments", body, HttpMethod.POST);

        if(response.getStatusCode().isError())
        {
            throw new HttpClientErrorException(response.getStatusCode());
        }
    }

    public static String setOrder()
    {
        ResponseEntity<String> response = SalesforceService.salesforceApiCall("/checkouts/active/orders", "", HttpMethod.POST);

        if(response.getStatusCode().isError())
        {
            throw new HttpClientErrorException(response.getStatusCode());
        } else {
            return response.getBody();
        }
    }    
}