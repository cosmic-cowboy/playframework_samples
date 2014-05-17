package pages;

import org.fluentlenium.core.FluentPage;
import static org.fest.assertions.fluentlenium.FluentLeniumAssertions.assertThat;
import static org.fluentlenium.core.filter.FilterConstructor.*;

import controllers.routes;

public class Login extends FluentPage{
	@Override
	public String getUrl() {
		return routes.Application.login().url();
	}
	
	@Override
	public void isAt() {
		assertThat(find("h1", withText("サインイン"))).hasSize(1);
	}
	
	public void login(String email, String password){
		fill("input").with(email, password);
		click("button");
	}
}
