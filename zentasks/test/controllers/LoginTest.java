package controllers;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import play.libs.Yaml;
import play.mvc.Result;
import play.test.WithApplication;

import com.avaje.ebean.Ebean;
import com.google.common.collect.ImmutableMap;

public class LoginTest extends WithApplication {

	@Before
	public void setUp() {
		start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
		Ebean.save((List) Yaml.load("test-data.yml"));
	}
	
	@Test
	public void authenticateSuccess() {
		Result result = callAction(
				controllers.routes.ref.Application.authenticate(),
		        fakeRequest().withFormUrlEncodedBody(ImmutableMap.of(
		            "email", "bob@example.com",
		            "password", "badpassword"))
		);
        assertThat(status(result)).isEqualTo(400);
        assertThat(session(result).get("email")).isNull();
	}
	
	@Test
	public void authenticated(){
		Result result = callAction(
				controllers.routes.ref.Application.index(),
				fakeRequest().withSession("email", "bob@example.com")
		);
		assertThat(status(result)).isEqualTo(200);
	}
	
	@Test
	public void notAuthenticated() {
		Result result = callAction(
				controllers.routes.ref.Application.index(),
				fakeRequest()
		);
		assertThat(status(result)).isEqualTo(303);
		assertThat(header("Location", result)).isEqualTo("/login");
	}
}
