package com.example.bootiful.Controller;

import com.example.bootiful.model.ResponseMessage;
import com.example.bootiful.model.Restaurant;
import com.example.bootiful.model.WebMessage;
import com.example.bootiful.model.ErrMessage;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@RestController
public class RestaurantsController {
    @RequestMapping("/restaurants")
    public ResponseEntity<ResponseMessage> restaurant(@RequestParam(name = "name", defaultValue = "") String name) throws IOException {

        //String q = "1109+N+Highland+St%2c+Arlington+VA";
        //name = "1109 N Highland St, Arlington, VA 22201";

        WebMessage wmsg = new WebMessage(String.format(name));
        if(name.compareTo("") == 0) {
            return new ResponseEntity<ResponseMessage>(new ErrMessage("/restaurants"), HttpStatus.BAD_REQUEST);
        }


        double lat,lng;
        ResponseEntity<String> geocode_res = geocoding_address(name);

        //get lat and lng
        ObjectMapper mapper = new ObjectMapper();
        JsonNode geo_root = mapper.readTree(geocode_res.getBody());
        JsonNode location = geo_root.get("results").get(0).get("location");
        lat = location.get("lat").asDouble();
        lng = location.get("lng").asDouble();

        ResponseEntity<String> zomato_res = zomato_search(lat, lng);
        format_zomato_msg(wmsg,zomato_res);

        ResponseEntity<ResponseMessage> res = new ResponseEntity<ResponseMessage>(wmsg, HttpStatus.OK);
        return  res;
    }
    private void format_zomato_msg(WebMessage wmsg, ResponseEntity<String> zomato_res) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        JsonNode zomato_root = mapper.readTree(zomato_res.getBody());
        JsonNode list = zomato_root.get("nearby_restaurants");

        for(JsonNode restaurant: list){
            if(restaurant != null) {
                //System.out.println(restaurant.toString());
                JsonNode r = restaurant.get("restaurant");
                Restaurant newR = new Restaurant(r.get("name").asText(), r.get("location").get("address").asText(),
                        r.get("user_rating").get("aggregate_rating").asDouble());
                newR.setCuisine(r.get("cuisines").asText());
                wmsg.add_restaurant(newR);
            }
        }

    }
    private ResponseEntity<String> geocoding_address(String q){
        RestTemplate restTemplate = new RestTemplate();

        //TODO:modify this line every time.
        String geocode_api_key = "7a7507f1bb55b8b9855aa52eb1b4d4ab5eebb48";

        String geocodeURL = "https://api.geocod.io/v1.3/geocode";
        String geocodeParams = "?q="+q+"&api_key="+geocode_api_key;
        geocodeURL += geocodeParams;

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        ResponseEntity<String> geocode_res = restTemplate.exchange(geocodeURL, HttpMethod.GET, entity, String.class);

        return geocode_res;
    }

    private ResponseEntity<String> zomato_search(double lat, double lon){
        RestTemplate restTemplate = new RestTemplate();

        //TODO:modify this line every time.
        String zomatoheader = "a306e5b122c379d5f1c09439f1e80ae4";

        String zomatoURL = "https://developers.zomato.com/api/v2.1/geocode";
        String zomatoParams = "?lat="+lat+"&lon="+lon;
        zomatoURL += zomatoParams;

        HttpHeaders headers = new HttpHeaders();
        headers.add("user-key",zomatoheader);
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        ResponseEntity<String> zomato_res = restTemplate.exchange(zomatoURL, HttpMethod.GET, entity, String.class);

        return zomato_res;

    }

}
