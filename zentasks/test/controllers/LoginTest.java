package controllers;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.callAction;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.fakeGlobal;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.session;
import static play.test.Helpers.status;

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
}
