package pages;

import org.fluentlenium.core.FluentPage;

import components.Drawer;

import static org.fest.assertions.fluentlenium.FluentLeniumAssertions.assertThat;
import static org.fluentlenium.core.filter.FilterConstructor.*;

public class Dashboard extends FluentPage {

	@Override
	public String getUrl() {
		return "/";
	}
	
	@Override
	public void isAt() {
		assertThat(find("h1", withText("ダッシュボード"))).hasSize(1);
	}
	
	public Drawer drawer() {
		return Drawer.from(this);
	}
}
