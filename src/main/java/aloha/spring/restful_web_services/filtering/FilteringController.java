package aloha.spring.restful_web_services.filtering;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

@RestController
public class FilteringController {

    // Dynamic mapping
    @GetMapping("/filtering")
    public MappingJacksonValue filtering() {
        SomeBean someBean = new SomeBean("value1", "value2", "value3", "value4", "value5");
        MappingJacksonValue mapped = new MappingJacksonValue(someBean);
        FilterProvider filters = new SimpleFilterProvider().addFilter("SomeBeanFilter",
                SimpleBeanPropertyFilter.filterOutAllExcept("field1", "field5"));
        mapped.setFilters(filters);
        return mapped;
    }

    // Static mapping
    @GetMapping("/filtering-list")
    public List<SomeBean> getMethodName() {
        return Arrays.asList(
                new SomeBean("value1", "value2", "value3", "value4", "value5"),
                new SomeBean("value6", "value7", "value8", "value9", "value10"));
    }

}
