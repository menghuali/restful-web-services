package aloha.spring.restful_web_services.versionning;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonController {

    @GetMapping("/v1/person")
    public PersonV1 getPersonV1_URL() {
        return new PersonV1("Bruce Wayne");
    }

    @GetMapping("/v2/person")
    public PersonV2 getPersonV2_URL() {
        return new PersonV2("Bruce", "Wayne");
    }

    @GetMapping(path = "/person", params = "version=1")
    public PersonV1 getPersonV1_Param() {
        return new PersonV1("Bruce Wayne");
    }

    @GetMapping(path = "/person", params = "version=2")
    public PersonV2 getPersonV2_Param() {
        return new PersonV2("Bruce", "Wayne");
    }

    @GetMapping(path = "/person/header", headers = "X-API-VERSION=1")
    public PersonV1 getPersonV1_Header() {
        return new PersonV1("Bruce Wayne");
    }

    @GetMapping(path = "/person/header", headers = "X-API-VERSION=2")
    public PersonV2 getPersonV2_Header() {
        return new PersonV2("Bruce", "Wayne");
    }

    @GetMapping(path = "/person/media_type", produces = "application/vnd.company.app-v1+json")
    public PersonV1 getPersonV1_MediaType() {
        return new PersonV1("Bruce Wayne");
    }

    @GetMapping(path = "/person/media_type", produces = "application/vnd.company.app-v2+json")
    public PersonV2 getPersonV2_MediaType() {
        return new PersonV2("Bruce", "Wayne");
    }

}
